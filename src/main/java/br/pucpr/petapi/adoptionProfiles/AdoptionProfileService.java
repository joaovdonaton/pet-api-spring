package br.pucpr.petapi.adoptionProfiles;

import br.pucpr.petapi.adoptionProfiles.dto.AdoptionProfileLocationDTO;
import br.pucpr.petapi.adoptionProfiles.dto.AdoptionProfileRegisterDTO;
import br.pucpr.petapi.adoptionProfiles.dto.AdoptionProfileUpdateDTO;
import br.pucpr.petapi.lib.error.ResourceAlreadyExistsException;
import br.pucpr.petapi.lib.error.ResourceDoesNotExistException;
import br.pucpr.petapi.lib.location.LocationUtils;
import br.pucpr.petapi.users.User;
import br.pucpr.petapi.users.UsersService;
import br.pucpr.petapi.users.dto.UserInfoDTO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class AdoptionProfileService {
    private final UsersService usersService;
    private final LocationUtils locationUtils;
    private final AdoptionProfileRepository repository;

    public AdoptionProfileService(UsersService usersService, LocationUtils locationUtils, AdoptionProfileRepository repository) {
        this.usersService = usersService;
        this.locationUtils = locationUtils;
        this.repository = repository;
    }

    public AdoptionProfile createAdoptionProfile(AdoptionProfileRegisterDTO adoptionProfileRegisterDTO){
        UserInfoDTO currentUserInfo = (UserInfoDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = usersService.findById(currentUserInfo.getId());

        if(currentUser.getAdoptionProfile() != null)
            throw new ResourceAlreadyExistsException(currentUser.getUsername() + " already has an adoption profile.",
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

        if(ap == null) throw new ResourceDoesNotExistException("User does not have an AdoptionProfile", HttpStatus.NOT_FOUND);

        repository.delete(ap);
    }

    public void updateAdoptionProfile(AdoptionProfileUpdateDTO adoptionProfileUpdateDTO) {
        UserInfoDTO currentUserInfo = (UserInfoDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = usersService.findById(currentUserInfo.getId());

        AdoptionProfile ap = currentUser.getAdoptionProfile();

        if(ap == null)
            throw new ResourceDoesNotExistException(currentUser.getUsername() + " does not have an adoption profile.",
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
            throw new ResourceDoesNotExistException(
                    "User ["+u.getUsername()+"] does not have a profile.",
                    HttpStatus.NOT_FOUND);
        }

        return profile;
    }

    /**
     * todos as áreas de localização acima do nível atual precisam bater para casos como por exemplo:
     * bairro de nome "centro" em duas cidades diferentes
     */
    public List<AdoptionProfile> findAllByLevel(int level, AdoptionProfileLocationDTO location){
        if(level < 0 || level >= AdoptionProfile.getLevels()) throw new IllegalArgumentException("Invalid Level: " + level);

        var probe = new AdoptionProfile(
                level == 0 ? location.getDistrict() : null,
                level <= 1 ? location.getCity() : null,
                level <= 2 ? location.getState() : null
        );

        // esse query by example deve ignorar newPetOwner (tipo é boolean, logo não pode ser null, e portando não é ignorado)
        return repository.findAll(Example.of(probe, ExampleMatcher.matching().withIgnorePaths("newPetOwner")));
    }

    /**
     * @param reference perfil para qual as distâncias serão calculadas
     * @param limit limite de perfis para retornar
     * @return lista com os adoption profiles ordenados por distância
     * Busca realizada com base no sistema de levels de localização (detalhes na classe da entidade AdoptionProfile).
     */
    public List<AdoptionProfile> findAdoptionProfilesSortByDistance(AdoptionProfile reference, int limit){
        List<AdoptionProfile> profiles = new ArrayList<>();

        for (int i = 0; i < reference.getLevels(); i++){

        }



        return profiles;
    }
}
