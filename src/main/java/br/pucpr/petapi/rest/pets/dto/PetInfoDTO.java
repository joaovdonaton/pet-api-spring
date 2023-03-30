package br.pucpr.petapi.rest.pets.dto;

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
}
