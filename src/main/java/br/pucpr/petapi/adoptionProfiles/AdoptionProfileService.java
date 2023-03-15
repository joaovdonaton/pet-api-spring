package br.pucpr.petapi.adoptionProfiles;

import br.pucpr.petapi.adoptionProfiles.dto.AdoptionProfileRegister;
import br.pucpr.petapi.lib.location.LocationUtils;
import br.pucpr.petapi.users.User;
import br.pucpr.petapi.users.UsersService;
import br.pucpr.petapi.users.dto.UserInfoDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AdoptionProfileService {
    private final UsersService usersService;
    private final LocationUtils locationUtils;

    public AdoptionProfileService(UsersService usersService, LocationUtils locationUtils) {
        this.usersService = usersService;
        this.locationUtils = locationUtils;
    }

    public AdoptionProfileRegister createAdoptionProfile(AdoptionProfileRegister adoptionProfileRegister){
        UserInfoDTO currentUserInfo = (UserInfoDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = usersService.findById(currentUserInfo.getId());
        var cepData = locationUtils.getCEPData(adoptionProfileRegister.getCep());

        AdoptionProfile ap = new AdoptionProfile(
            currentUser,
                adoptionProfileRegister.getCep(),
                adoptionProfileRegister.getDescription(),
                adoptionProfileRegister.isNewPetOwner(),
                cepData.getUf(),
                cepData.getLocalidade(),
                cepData.getBairro(),

        );

        return null;
    }
}
