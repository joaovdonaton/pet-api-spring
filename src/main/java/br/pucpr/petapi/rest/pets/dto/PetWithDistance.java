package br.pucpr.petapi.rest.pets.dto;

import br.pucpr.petapi.rest.pets.Pet;
import br.pucpr.petapi.rest.users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PetWithDistance {
    private UUID id;
    private String name;
    private String nickname;
    private Integer age;
    private String description;
    private String type;
    @JsonIgnore
    private User user;
    private int distance;

    public static PetWithDistance fromPet(Pet p, int distance){
        return new PetWithDistance(p.getId(), p.getName(), p.getNickname(), p.getAge(), p.getDescription(), p.getPetType().getName(),
                p.getUser(), distance);
    }
}
