package br.pucpr.petapi.rest.adoptionRequests;

import br.pucpr.petapi.rest.adoptionRequests.dto.AdoptionRequestRegister;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adoption/requests")
public class AdoptionRequestsController {
    private final AdoptionRequestsService service;

    public AdoptionRequestsController(AdoptionRequestsService service) {
        this.service = service;
    }

    @PostMapping("/")
    @SecurityRequirement(name = "auth")
    @RolesAllowed("USER")
    @Operation(
            summary = ""
    )
    @Tag(name = "Adoption")
    public AdoptionRequest createRequest(@RequestBody @Valid AdoptionRequestRegister adoptionRequestRegister){
        return service.createAdoptionRequest(adoptionRequestRegister);
    }
}
