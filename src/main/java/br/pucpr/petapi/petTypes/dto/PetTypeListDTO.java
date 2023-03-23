package br.pucpr.petapi.petTypes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class PetTypeListDTO {
    private Set<String> types;
}
