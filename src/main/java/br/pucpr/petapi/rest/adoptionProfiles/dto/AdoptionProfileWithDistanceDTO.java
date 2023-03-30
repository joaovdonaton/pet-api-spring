package br.pucpr.petapi.rest.adoptionProfiles.dto;


import br.pucpr.petapi.rest.adoptionProfiles.AdoptionProfile;
import lombok.AllArgsConstructor;
import lombok.Data;

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
