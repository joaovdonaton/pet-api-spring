package br.pucpr.petapi.rest.adoptionProfiles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class AdoptionProfileInfo {
    private String description;
    private boolean newPetOwner;
    private Set<String> preferredPetTypes;
    private String city;
    private String state;
}
