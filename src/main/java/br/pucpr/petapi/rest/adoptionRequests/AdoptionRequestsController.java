package br.pucpr.petapi.rest.adoptionRequests;

import br.pucpr.petapi.rest.adoptionRequests.dto.AdoptionRequestRegisterDTO;
import br.pucpr.petapi.rest.adoptionRequests.dto.AdoptionRequestStatusPatchDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
            summary = "Send an adoption request to a given pet"
    )
    @Tag(name = "Adoption")
    public AdoptionRequest createRequest(@RequestBody @Valid AdoptionRequestRegisterDTO adoptionRequestRegisterDTO){
        return service.createAdoptionRequest(adoptionRequestRegisterDTO);
    }

    @PatchMapping("/")
    @SecurityRequirement(name = "auth")
    @RolesAllowed("USER")
    @Operation(
            summary = "Update request status (for receiving end)"
    )
    @Tag(name = "Adoption")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void patchRequest(@RequestBody @Valid AdoptionRequestStatusPatchDTO adoptionRequestStatusPatchDTO){
        service.updateStatus(adoptionRequestStatusPatchDTO);
    }
}
