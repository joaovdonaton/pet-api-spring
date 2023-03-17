package br.pucpr.petapi.adoptionProfiles;

import br.pucpr.petapi.adoptionProfiles.dto.AdoptionProfileRegister;
import br.pucpr.petapi.lib.error.ResourceAlreadyExistsException;
import br.pucpr.petapi.lib.error.ResourceDoesNotExistException;
import br.pucpr.petapi.lib.location.LocationUtils;
import br.pucpr.petapi.users.User;
import br.pucpr.petapi.users.UsersService;
import br.pucpr.petapi.users.dto.UserInfoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public AdoptionProfile createAdoptionProfile(AdoptionProfileRegister adoptionProfileRegister){
        UserInfoDTO currentUserInfo = (UserInfoDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = usersService.findById(currentUserInfo.getId());

        if(currentUser.getAdoptionProfile() != null)
            throw new ResourceAlreadyExistsException(currentUser.getUsername() + " already has an adoption profile.",
                    HttpStatus.BAD_REQUEST);

        // preencher dados de localização
        var cepData = locationUtils.getCEPData(adoptionProfileRegister.getCep());
        var coordinates = locationUtils.getCoordinates(
                String.join(
                        " ",
                        cepData.getBairro(),
                        cepData.getLocalidade(),
                        cepData.getUf()
                        )
        );

        AdoptionProfile ap = new AdoptionProfile(
            currentUser,
                adoptionProfileRegister.getCep(),
                adoptionProfileRegister.getDescription(),
                adoptionProfileRegister.isNewPetOwner(),
                cepData.getUf(),
                cepData.getLocalidade(),
                cepData.getBairro(),
                new BigDecimal(coordinates.getLat()),
                new BigDecimal(coordinates.getLng()),
                Set.of(),
                adoptionProfileRegister.getPreferredPetTypes()
        );

        return repository.save(ap);
    }

    // apagar perfil do usuario atualmente autenticado
    public void deleteAdoptionProfile() {
        UserInfoDTO currentUserInfo = (UserInfoDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = usersService.findById(currentUserInfo.getId());

        AdoptionProfile ap = currentUser.getAdoptionProfile();

        if(ap == null) throw new ResourceDoesNotExistException("User does not have an AdoptionProfile", HttpStatus.NOT_FOUND);

        repository.delete(ap);
    }
}
