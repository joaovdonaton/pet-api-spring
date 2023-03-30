package br.pucpr.petapi.pets.dto;

import br.pucpr.petapi.petTypes.PetType;
import br.pucpr.petapi.pets.Pet;
import br.pucpr.petapi.users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
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

    @Override
    public String toString() {
        return "distance to " + getName() +" =   "+ distance + "meters";
    }

    public static PetWithDistance fromPet(Pet p, int distance){
        return new PetWithDistance(p.getId(), p.getName(), p.getNickname(), p.getAge(), p.getDescription(), p.getPetType().getName(),
                p.getUser(), distance);
    }
}
