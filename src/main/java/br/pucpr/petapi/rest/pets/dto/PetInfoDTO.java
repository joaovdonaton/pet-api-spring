package br.pucpr.petapi.rest.pets.dto;

import br.pucpr.petapi.rest.pets.Pet;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PetInfoDTO {
    private UUID id;
    private String name;
    private String nickname;
    private String description;
    private Integer age;
    private String type;

    public static PetInfoDTO fromPet(Pet pet){
        return new PetInfoDTO(pet.getId(), pet.getName(), pet.getNickname(), pet.getDescription(), pet.getAge(),
                pet.getPetType().getName());
    }
}
