package br.pucpr.petapi.adoptionProfiles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdoptionProfileLocationDTO {
    private String district;
    private String city;
    private String state;
}
