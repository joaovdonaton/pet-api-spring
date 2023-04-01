package br.pucpr.petapi.rest.matcher.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

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
}
