package br.pucpr.petapi.pets;

import br.pucpr.petapi.pets.dto.PetRegisterDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pets")
public class PetsController {
    private final PetsService service;

    public PetsController(PetsService service) {
        this.service = service;
    }

    @PostMapping("/")
    @RolesAllowed("USER")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "auth")
    @Operation(
            summary = "create new pet"
    )
    @Tag(name = "Pet")
    public Pet create(@RequestBody @Valid PetRegisterDTO petRegisterDTO){
        return service.createPet(petRegisterDTO);
    }

    /* TODO:
    - Make a proper PetInfoDTO for returning pet data.
    - Make an endpoint to list petTypes
    * */
}
