package br.pucpr.petapi.adoptionProfiles.dto;


import br.pucpr.petapi.adoptionProfiles.AdoptionProfile;
import br.pucpr.petapi.users.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AdoptionProfileWithDistanceDTO{
    private AdoptionProfile profile;
    private int distance;

    @Override
    public String toString() {
        return "Distance to %s = %d meters".formatted(profile.getUser().getUsername(), distance);
    }
}
