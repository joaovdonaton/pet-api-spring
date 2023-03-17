package br.pucpr.petapi.adoptionProfiles.dto;

import br.pucpr.petapi.adoptionProfiles.validation.ValidCEP;
import br.pucpr.petapi.adoptionProfiles.validation.ValidPreferredTypes;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Data
public class AdoptionProfileRegisterDTO {
//    @Length(min = 8, max = 9, message = "Invalid Cep Length (min 8, max 9)")
    @ValidCEP(message = "Invalid CEP format! Valid formats: 000000-00 or 00000000")
    private String cep;
    @Length(min = 50, max = 500, message = "Description length must be at least 50 or at max 500")
    @NotBlank
    private String description;

    @NotNull
    private boolean newPetOwner;
    @ElementCollection
    @ValidPreferredTypes(message = "Invalid preferredPetTypes")
    @NotNull
    private Set<String> preferredPetTypes;
}