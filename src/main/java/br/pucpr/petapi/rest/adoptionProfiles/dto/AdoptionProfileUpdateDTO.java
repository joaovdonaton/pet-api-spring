package br.pucpr.petapi.rest.adoptionProfiles.dto;

import br.pucpr.petapi.rest.adoptionProfiles.validation.ValidCEP;
import br.pucpr.petapi.rest.adoptionProfiles.validation.ValidPreferredTypes;
import jakarta.persistence.ElementCollection;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Data
public class AdoptionProfileUpdateDTO {
    @ValidCEP(message = "Invalid CEP format! Valid formats: 000000-00 or 00000000", nullable = true)
    private String cep;
    @Length(min = 50, max = 500, message = "Description length must be at least 50 or at max 500")
    private String description;
    private boolean newPetOwner;
    @ElementCollection
    @ValidPreferredTypes(message = "Invalid preferredPetTypes", nullable = true)
    private Set<String> preferredPetTypes;
}
