package br.pucpr.petapi.adoptionProfiles.dto;

import br.pucpr.petapi.adoptionProfiles.validation.ValidCEP;
import br.pucpr.petapi.users.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Data
public class AdoptionProfileRegister {
//    @Length(min = 8, max = 9, message = "Invalid Cep Length (min 8, max 9)")
    @ValidCEP(message = "Invalid CEP format! Valid formats: 000000-00 or 00000000")
    private String cep;
    private String description;
    private boolean newPetOwner;
    @ElementCollection
    private Set<String> preferredPetTypes;
}
