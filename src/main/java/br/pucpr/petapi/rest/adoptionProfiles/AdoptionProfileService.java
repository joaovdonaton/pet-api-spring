package br.pucpr.petapi.rest.adoptionProfiles;

import br.pucpr.petapi.lib.error.MessageSettings;
import br.pucpr.petapi.rest.adoptionProfiles.dto.AdoptionProfileRegisterDTO;
import br.pucpr.petapi.rest.adoptionProfiles.dto.AdoptionProfileUpdateDTO;
import br.pucpr.petapi.rest.adoptionProfiles.dto.AdoptionProfileWithDistanceDTO;
import br.pucpr.petapi.lib.error.exceptions.ResourceAlreadyExistsException;
import br.pucpr.petapi.lib.error.exceptions.ResourceDoesNotExistException;
import br.pucpr.petapi.lib.location.LocationUtils;
import br.pucpr.petapi.rest.users.User;
import br.pucpr.petapi.rest.users.UsersService;
import br.pucpr.petapi.rest.users.dto.UserInfoDTO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
public class AdoptionProfileService {
    private final UsersService usersService;
    private final LocationUtils locationUtils;
    private final AdoptionProfileRepository repository;
    private final MessageSettings messageSettings;

    public AdoptionProfileService(UsersService usersService, LocationUtils locationUtils, AdoptionProfileRepository repository, MessageSettings messageSettings) {
        this.usersService = usersService;
        this.locationUtils = locationUtils;
        this.repository = repository;
        this.messageSettings = messageSettings;
    }

    public AdoptionProfile createAdoptionProfile(AdoptionProfileRegisterDTO adoptionProfileRegisterDTO){
        UserInfoDTO currentUserInfo = (UserInfoDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = usersService.findById(currentUserInfo.getId());

        if(currentUser.getAdoptionProfile() != null)
            throw new ResourceAlreadyExistsException(messageSettings.getResourceDoesNotExist(),
                    currentUser.getUsername() + " already has an adoption profile.",
                    HttpStatus.BAD_REQUEST);

        AdoptionProfile ap = new AdoptionProfile(
            currentUser,
                adoptionProfileRegisterDTO.getCep(),
                adoptionProfileRegisterDTO.getDescription(),
                adoptionProfileRegisterDTO.isNewPetOwner(),
                Set.of(),
                adoptionProfileRegisterDTO.getPreferredPetTypes()
        );

        ap = completeLocationData(ap, adoptionProfileRegisterDTO.getCep());

        return repository.save(ap);
    }

    public void deleteAdoptionProfile() {
        UserInfoDTO currentUserInfo = (UserInfoDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = usersService.findById(currentUserInfo.getId());

        AdoptionProfile ap = currentUser.getAdoptionProfile();

        if(ap == null) throw new ResourceDoesNotExistException(messageSettings.getResourceDoesNotExist(),
                "User does not have an AdoptionProfile", HttpStatus.NOT_FOUND);

        repository.delete(ap);
    }

    public void updateAdoptionProfile(AdoptionProfileUpdateDTO adoptionProfileUpdateDTO) {
        User currentUser = usersService.getCurrentAuth();

        AdoptionProfile ap = currentUser.getAdoptionProfile();

        if(ap == null)
            throw new ResourceDoesNotExistException(messageSettings.getResourceDoesNotExist(),
                    currentUser.getUsername() + " does not have an adoption profile.",
                    HttpStatus.NOT_FOUND);

        String cep = adoptionProfileUpdateDTO.getCep();
        if(cep != null){
            ap = completeLocationData(ap, cep);
        }

        String description = adoptionProfileUpdateDTO.getDescription();
        if(description != null){
            ap.setDescription(description);
        }

        boolean newPetOwner = adoptionProfileUpdateDTO.isNewPetOwner();
        ap.setNewPetOwner(ap.isNewPetOwner() == newPetOwner ? ap.isNewPetOwner() : newPetOwner);

        var preferredPetTypes = adoptionProfileUpdateDTO.getPreferredPetTypes();
        if(preferredPetTypes != null) {
            adoptionProfileUpdateDTO.setPreferredPetTypes(preferredPetTypes);
        }

        repository.save(ap);
    }

    // preenche os campos de localização a partir do CEP do adoptionprofile
    public AdoptionProfile completeLocationData(AdoptionProfile profile, String CEP){
        profile.setCep(CEP);

        var cepData = locationUtils.getCEPData(CEP);
        var coordinates = locationUtils.getCoordinates(String.join(
                " ",
                cepData.getBairro(),
                cepData.getLocalidade(),
                cepData.getUf()
        ));

        profile.setCity(cepData.getLocalidade());
        profile.setDistrict(cepData.getBairro());
        profile.setState(cepData.getUf());
        profile.setLatitude(new BigDecimal(coordinates.getLat()));
        profile.setLongitude(new BigDecimal(coordinates.getLng()));

        return profile;
    }

    public AdoptionProfile findAdoptionProfileByUsername(String username){
        var u = usersService.findByUsername(username);
        var profile = u.getAdoptionProfile();

        if(profile == null){
            throw new ResourceDoesNotExistException(messageSettings.getResourceDoesNotExist(),
                    u.getUsername() + " does not have an adoption profile.",
                    HttpStatus.NOT_FOUND);
        }

        return profile;
    }

    /**
     * @param level caso seja maior que q quantidade de níveis existentes, a busca será GLOBAL
     * todos as áreas de localização acima do nível atual precisam bater para casos como por exemplo:
     * bairro de nome "centro" em duas cidades diferentes
     * @return a lista retornada já exclui o profile reference
     */
    public List<AdoptionProfile> findAllByLevel(int level, AdoptionProfile reference){
        if(level < 0) throw new IllegalArgumentException("Invalid Level: " + level);
        if(level >= AdoptionProfile.getLevels())
            return repository.findAll().stream().filter(ap -> !ap.equals(reference)).toList(); // buscar todos

        var probe = new AdoptionProfile(
                level == 0 ? reference.getDistrict() : null,
                level <= 1 ? reference.getCity() : null,
                level <= 2 ? reference.getState() : null
        );

        // esse query by example deve ignorar newPetOwner (tipo é boolean, logo não pode ser null, e portando não é ignorado)
        return repository.
                findAll(Example.of(probe, ExampleMatcher.matching().withIgnorePaths("newPetOwner")))
                .stream().filter(ap -> !ap.equals(reference)).toList();
    }

    /**
     * @param reference perfil para qual as distâncias serão calculadas
     * @param limit limite de perfis para retornar
     * @return lista com os adoption profiles ordenados por distância
     * Busca realizada com base no sistema de levels de localização (detalhes na classe da entidade AdoptionProfile).
     */
    public List<AdoptionProfileWithDistanceDTO> findAdoptionProfilesSortByDistance(AdoptionProfile reference, int limit){
        List<AdoptionProfileWithDistanceDTO> result = new ArrayList<>();

        for (int i = 0; i < AdoptionProfile.getLevels()+1; i++){
            for (var p : findAllByLevel(i, reference)){
                // não adicionar ja adicionados
                if(result.stream().noneMatch(prof -> prof.getProfile().equals(p)))
                    result.add(new AdoptionProfileWithDistanceDTO(p,
                            locationUtils.getDirectDistanceBetweenProfiles(p, reference)));

                if(result.size() == limit) break;
            }

            if(result.size() == limit) break;
        }

        return result.stream().sorted(Comparator.comparingInt(AdoptionProfileWithDistanceDTO::getDistance)).toList();
    }
}
