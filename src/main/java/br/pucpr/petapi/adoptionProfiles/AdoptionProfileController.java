package br.pucpr.petapi.adoptionProfiles;

import br.pucpr.petapi.adoptionProfiles.dto.AdoptionProfileRegister;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public AdoptionProfile create(@RequestBody @Valid AdoptionProfileRegister adoptionProfileRegister){
        return service.createAdoptionProfile(adoptionProfileRegister);
    }

    @DeleteMapping("/")
    @RolesAllowed("USER")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "auth")
    public void delete(){
        service.deleteAdoptionProfile();
    }
}
