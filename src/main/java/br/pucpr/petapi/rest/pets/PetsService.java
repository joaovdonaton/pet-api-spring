package br.pucpr.petapi.rest.pets;

import br.pucpr.petapi.lib.error.MessageSettings;
import br.pucpr.petapi.lib.error.exceptions.ResourceDoesNotExistException;
import br.pucpr.petapi.rest.adoptionProfiles.AdoptionProfile;
import br.pucpr.petapi.rest.adoptionProfiles.AdoptionProfileService;
import br.pucpr.petapi.lib.location.LocationUtils;
import br.pucpr.petapi.rest.pets.dto.PetRegisterDTO;
import br.pucpr.petapi.rest.pets.dto.PetWithDistance;
import br.pucpr.petapi.rest.petTypes.PetTypeService;
import br.pucpr.petapi.rest.users.User;
import br.pucpr.petapi.rest.users.UsersService;
import br.pucpr.petapi.rest.users.dto.UserInfoDTO;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class PetsService {
    private final PetsRepository repository;
    private final UsersService usersService;
    private final PetTypeService petTypeService;
    private final AdoptionProfileService adoptionProfileService;
    private final LocationUtils locationUtils;
    private final MessageSettings messageSettings;

    public PetsService(PetsRepository repository, UsersService usersService, PetTypeService petTypeService, AdoptionProfileService adoptionProfileService, LocationUtils locationUtils, MessageSettings messageSettings) {
        this.repository = repository;
        this.usersService = usersService;
        this.petTypeService = petTypeService;
        this.adoptionProfileService = adoptionProfileService;
        this.locationUtils = locationUtils;
        this.messageSettings = messageSettings;
    }

    public boolean existsByName(String name){
        return repository.existsByName(name);
    }

    public Pet findById(UUID id){
        return repository.findById(id).orElseThrow(() ->
                new ResourceDoesNotExistException(messageSettings.getResourceDoesNotExist(),
                        "Pet with ID ["+id+"] does not exist.", HttpStatus.NOT_FOUND));
    }

    public Pet createPet(PetRegisterDTO petRegisterDTO) {
        UserInfoDTO currentUserInfo = (UserInfoDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User u = usersService.findByUsername(currentUserInfo.getUsername());

        Pet p = new Pet(
                petRegisterDTO.getName(),
                petRegisterDTO.getNickname(),
                petRegisterDTO.getAge(),
                petRegisterDTO.getDescription(),
                u,
                LocalDateTime.now(),
                petTypeService.findPetTypeByName(petRegisterDTO.getType())
        );

        return repository.save(p);
    }

    public List<PetWithDistance> searchPet(int limit, int page, String sortBy, String ascDesc){
        if(sortBy.equals("distance")){
            var pets = findPetsSortByDistance(usersService.getCurrentAuth().getAdoptionProfile(), limit*(page+1), false);
            return pets.stream().skip(page*limit).limit(limit).toList();
        }
        else {
            Pageable pageable = PageRequest.of(page,
                    limit,
                    setSortAscDesc(ascDesc, Sort.by(sortBy)));
            return repository.findAll(pageable).stream().map(p -> PetWithDistance.fromPet(p, -1)).toList();
        }
    }

    /**
     * @param reference perfil para qual as distâncias serão calculadas
     * @param limit limite de pets para retornar
     * @return lista com pets ordenados por distância
     * Busca por níveis de localização. Primeiro realiza a busca nos níveis mais específicos (neste caso,
     * o bairro), e depois expande a busca, e para quando encontrar a quantidade pedida no limit.
     */
    public List<PetWithDistance> findPetsSortByDistance(AdoptionProfile reference, int limit, boolean filterViewed){
        List<PetWithDistance> result = new ArrayList<>();

        for (int i = 0; i < AdoptionProfile.getLevels()+1; i++){
            for (var p : findAllByLevel(i, reference)){
                if(result.stream().noneMatch(pet -> p.getId().equals(pet.getId()))) {
                    // remover os já visualizados, caso filterViewed seja true (para o matcher)
                    if(filterViewed && reference.getViewedPetIds().stream().anyMatch(id -> id.equals(p.getId()))) continue;

                    result.add(PetWithDistance.fromPet(p,
                            locationUtils.getDirectDistanceBetweenProfiles(p.getUser().getAdoptionProfile(), reference)));
                }

                if(result.size() == limit) break;
            }

            if(result.size() == limit) break;
        }

        return result.stream().sorted(Comparator.comparingInt(PetWithDistance::getDistance)).toList();
    }

    /**
     * segue as mesmas regras do método AdoptionProfileService.findAllByLevel
     */
    public List<Pet> findAllByLevel(int level, AdoptionProfile reference){
        List<Pet> result = new ArrayList<>();
        var profiles = adoptionProfileService.findAllByLevel(level, reference);

        for (var p: profiles){
            result.addAll(p.getUser().getPets());
        }

        return result;
    }

    // encontra todos os pets do usuário logado
    public List<Pet> findAllPetsBelongingToUser(){
        UserInfoDTO currentUserInfo = (UserInfoDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var pets = usersService.findByUsername(currentUserInfo.getUsername()).getPets().stream().toList();

        return pets;
    }

    private Sort setSortAscDesc(String ascDesc, Sort s){
        return ascDesc.equals("asc") ? s.ascending() : s.descending();
    }
}
