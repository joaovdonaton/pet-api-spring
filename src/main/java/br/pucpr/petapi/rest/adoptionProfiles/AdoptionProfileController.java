package br.pucpr.petapi.rest.adoptionProfiles;

import br.pucpr.petapi.rest.adoptionProfiles.dto.AdoptionProfileInfo;
import br.pucpr.petapi.rest.adoptionProfiles.dto.AdoptionProfileRegisterDTO;
import br.pucpr.petapi.rest.adoptionProfiles.dto.AdoptionProfileUpdateDTO;
import br.pucpr.petapi.rest.users.dto.UserInfoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("adoption/profile")
public class AdoptionProfileController {
    private final AdoptionProfileService service;

    public AdoptionProfileController(AdoptionProfileService service) {
        this.service = service;
    }

    @PostMapping("/")
    @RolesAllowed("USER")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "auth")
    @Operation(
            summary = "create profile"
    )
    @Tag(name = "Profile")
    public AdoptionProfile create(@RequestBody @Valid AdoptionProfileRegisterDTO adoptionProfileRegisterDTO){
        return service.createAdoptionProfile(adoptionProfileRegisterDTO);
    }

    @DeleteMapping("/")
    @RolesAllowed("USER")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "auth")
    @Operation(
            summary = "Delete user profile",
            description = "Deletes current user's adoption profile, if it exists."
    )
    @Tag(name = "Profile")
    public void delete(){
        service.deleteAdoptionProfile();
    }

    @PatchMapping("/")
    @RolesAllowed("USER")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "auth")
    @Operation(
            summary = "Update profile fields",
            description = "Updates currently authenticated user's adoption profile. All fields are optional."
    )
    @Tag(name = "Profile")
    public void update(@RequestBody @Valid AdoptionProfileUpdateDTO adoptionProfileUpdateDTO){
        service.updateAdoptionProfile(adoptionProfileUpdateDTO);
    }

    @GetMapping("/{username}")
    @RolesAllowed("USER")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "auth")
    @Operation(
            summary = "get user profile information",
            description = "excludes detailed location information for security reasons. /me endpoint provides" +
                    "full details for self."
    )
    @Tag(name = "Profile")
    public AdoptionProfileInfo profileInfo(@PathVariable String username){
        var profile = service.findAdoptionProfileByUsername(username);

        return new AdoptionProfileInfo(profile.getDescription(),
                profile.isNewPetOwner(),
                profile.getPreferredPetTypes(),
                profile.getCity(),
                profile.getState());
    }

    @GetMapping("/me")
    @RolesAllowed("USER")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "auth")
    @Operation(
            summary = "get current auth's detailed profile information"
    )
    @Tag(name = "Profile")
    public AdoptionProfile detailedProfileInfo(){
        return service.findAdoptionProfileByUsername(
                ((UserInfoDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getUsername()
        );
    }
}
