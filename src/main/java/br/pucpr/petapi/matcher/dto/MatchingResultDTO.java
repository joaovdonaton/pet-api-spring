package br.pucpr.petapi.matcher.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchingResultDTO {
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
