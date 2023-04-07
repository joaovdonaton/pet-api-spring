package br.pucpr.petapi.rest.matcher.dto;

import br.pucpr.petapi.rest.pets.dto.PetWithDistance;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
public class MatchingResultDTO {
    private UUID petId;
    private String name;
    private String nickname;
    private Integer age;
    private String description;
    private String type;
    private String ownerUsername;
    private String city;
    private String state;
    private Integer distance;

    public static MatchingResultDTO fromPetWithDistance(PetWithDistance petWithDistance){
        return new MatchingResultDTO(
                petWithDistance.getId(),
                petWithDistance.getName(),
                petWithDistance.getNickname(),
                petWithDistance.getAge(),
                petWithDistance.getDescription(),
                petWithDistance.getType(),
                petWithDistance.getUser().getUsername(),
                petWithDistance.getUser().getAdoptionProfile().getCity(),
                petWithDistance.getUser().getAdoptionProfile().getState(),
                petWithDistance.getDistance()
        );
    }
}
