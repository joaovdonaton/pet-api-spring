package br.pucpr.petapi.pets;

import br.pucpr.petapi.pets.dto.PetInfoDTO;
import br.pucpr.petapi.pets.dto.PetRegisterDTO;
import br.pucpr.petapi.pets.dto.PetWithDistance;
import br.pucpr.petapi.pets.enums.AscDescEnum;
import br.pucpr.petapi.pets.enums.PetSortByEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public PetInfoDTO create(@RequestBody @Valid PetRegisterDTO petRegisterDTO){
        var pet = service.createPet(petRegisterDTO);

        return new PetInfoDTO(pet.getId(),
                pet.getName(),
                pet.getNickname(),
                pet.getDescription(),
                pet.getAge(),
                pet.getPetType().getName());
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed("USER")
    @SecurityRequirement(name = "auth")
    @Operation(
            summary = "Search for pets based on parameters",
            description = "Returns a JSON array of Pet objects, these Pet objects have an additional property for distance" +
                    "(distance will be returned as -1 if it is not relevant to the search)"
    )
    @Tag(name="Pet")
    public List<PetWithDistance> search(@RequestParam(defaultValue = "10") Integer limit,
                                        @RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(defaultValue = "name", required = false) PetSortByEnum sortBy,
                                        @RequestParam(defaultValue = "asc", required = false) AscDescEnum ascDesc){
        return service.searchPet(limit, page, sortBy.toString(), ascDesc.toString());
    }

    @GetMapping("/me")
    @RolesAllowed("USER")
    @SecurityRequirement(name = "auth")
    @Operation(
            summary = "Return currently authenticated user's pets"
    )
    @Tag(name="Pet")
    public List<Pet> getMyPets(){
        return service.findAllPetsBelongingToUser();
    }
}
