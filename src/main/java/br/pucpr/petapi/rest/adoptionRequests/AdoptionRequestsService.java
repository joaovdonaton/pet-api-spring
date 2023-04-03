package br.pucpr.petapi.rest.adoptionRequests;

import br.pucpr.petapi.lib.error.MessageSettings;
import br.pucpr.petapi.lib.error.exceptions.BadRequest;
import br.pucpr.petapi.lib.error.exceptions.ResourceAlreadyExistsException;
import br.pucpr.petapi.lib.error.exceptions.ResourceDoesNotExistException;
import br.pucpr.petapi.lib.error.exceptions.UnauthorizedException;
import br.pucpr.petapi.rest.adoptionRequests.dto.AdoptionRequestInfoDTO;
import br.pucpr.petapi.rest.adoptionRequests.dto.AdoptionRequestRegisterDTO;
import br.pucpr.petapi.rest.adoptionRequests.dto.AdoptionRequestStatusPatchDTO;
import br.pucpr.petapi.rest.adoptionRequests.enums.RequestType;
import br.pucpr.petapi.rest.adoptionRequests.enums.Status;
import br.pucpr.petapi.rest.pets.PetsService;
import br.pucpr.petapi.rest.users.UsersService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class AdoptionRequestsService {
    private final AdoptionRequestsRepository repository;
    private final PetsService petsService;
    private final UsersService usersService;
    private final MessageSettings messageSettings;

    public AdoptionRequestsService(AdoptionRequestsRepository repository, PetsService petsService, UsersService usersService, MessageSettings messageSettings) {
        this.repository = repository;
        this.petsService = petsService;
        this.usersService = usersService;
        this.messageSettings = messageSettings;
    }

    public AdoptionRequest findById(UUID id){
        return repository.findById(id).orElseThrow(
                () -> new ResourceDoesNotExistException(messageSettings.getResourceDoesNotExist(),
                        "Adoption request with ID ["+id+"] does not exist", HttpStatus.NOT_FOUND)
        );
    }

    @Transactional
    public AdoptionRequest createAdoptionRequest(AdoptionRequestRegisterDTO register){
        var currentUser = usersService.getCurrentAuth();

        var pet = petsService.findById(register.getPetId());
        var receiver = pet.getUser();

        if(currentUser.getId().equals(receiver.getId()))
            throw new BadRequest("Cannot send request: user owns the pet with ID ["+pet.getId()+"]");

        for(AdoptionRequest ar: pet.getAdoptionRequests()){
            if(ar.getUserSender().getId().equals(currentUser.getId())){
                throw new ResourceAlreadyExistsException(messageSettings.getResourceAlreadyExists(),
                        "Cannot send request: user has already sent a request to pet ID [" + pet.getId() + "]", HttpStatus.BAD_REQUEST);
            }
        }

        AdoptionRequest ar = new AdoptionRequest();

        //atualizar as collections do lado "one" da relação (métodos auxiliares já atualizam o objeto do AdoptionRequest)
        receiver.addIncomingRequest(ar);
        currentUser.addOutgoingRequest(ar);
        pet.addAdoptionRequest(ar);

        ar.setMessage(register.getMessage());
        ar.setTitle(register.getTitle());
        ar.setStatus(Status.PENDING);

        return repository.save(ar);
    }

    @Transactional
    public void updateStatus(AdoptionRequestStatusPatchDTO adoptionRequestStatusPatchDTO){
        var currentUser = usersService.getCurrentAuth();
        var request = findById((adoptionRequestStatusPatchDTO.getRequestId()));

        if(!currentUser.getId().equals(request.getUserReceiver().getId()))
            throw new UnauthorizedException("User is not the receiver of this request");

        request.setStatus(Status.valueOf(adoptionRequestStatusPatchDTO.getStatus()));

        repository.save(request);
    }

    @Transactional
    public List<AdoptionRequestInfoDTO> listRequests(Status statusFilter, RequestType typeFilter) {
        var currentUser = usersService.getCurrentAuth();
        Set<AdoptionRequest> requests = new HashSet<>();

        if(typeFilter == null || typeFilter == RequestType.INCOMING) requests.addAll(currentUser.getIncomingRequests());
        if(typeFilter == null || typeFilter == RequestType.OUTGOING) requests.addAll(currentUser.getOutgoingRequests());

        var requestsStream = requests.stream();

        if(statusFilter != null)
            requestsStream = requestsStream.filter(r -> r.getStatus().equals(statusFilter));

        return requestsStream.map(ar ->
                new AdoptionRequestInfoDTO(ar.getId(), ar.getPet().getId(), ar.getUserSender().getId(),
                        ar.getUserReceiver().getId(), ar.getTitle(), ar.getMessage(), ar.getStatus())
        ).toList();
    }
}
