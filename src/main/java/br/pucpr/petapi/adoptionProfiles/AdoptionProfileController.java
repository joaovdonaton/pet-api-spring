package br.pucpr.petapi.adoptionProfiles;

import br.pucpr.petapi.adoptionProfiles.dto.AdoptionProfileRegister;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("adoption/profile")
public class AdoptionProfileController {
    private final AdoptionProfileService service;

    public AdoptionProfileController(AdoptionProfileService service) {
        this.service = service;
    }

    @PostMapping("/")
    @RolesAllowed("USER")
    @SecurityRequirement(name = "auth")
    public AdoptionProfileRegister create(@RequestBody @Valid AdoptionProfileRegister adoptionProfileRegister){
        return adoptionProfileRegister;
    }
}
