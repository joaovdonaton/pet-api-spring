package br.pucpr.petapi.pets.dto;

import br.pucpr.petapi.pets.Pet;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PetWithDistance {
    private Pet pet;
    private int distance;
}
