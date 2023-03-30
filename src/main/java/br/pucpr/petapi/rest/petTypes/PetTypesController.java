package br.pucpr.petapi.rest.petTypes;

import br.pucpr.petapi.rest.petTypes.dto.PetTypeListDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pets/types")
public class PetTypesController {
    private final PetTypeService service;

    public PetTypesController(PetTypeService service) {
        this.service = service;
    }

    @GetMapping()
    @Operation(
            summary = "get a list of valid pet types.",
            description = "used for setting preferred types and pet type."
    )
    @Tag(name = "Pet Types")
    public PetTypeListDTO list(){
        return new PetTypeListDTO(service.getAllPetTypeNames());
    }
}
