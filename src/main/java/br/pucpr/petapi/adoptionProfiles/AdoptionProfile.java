package br.pucpr.petapi.adoptionProfiles;

import br.pucpr.petapi.lib.location.dto.response.geocoding.CoordinatesDTO;
import br.pucpr.petapi.users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdoptionProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    @JoinTable(
            name = "users_adoption_profiles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "adoption_profile_id", referencedColumnName = "id")

    )
    private User user;
    private String cep;
    @Column(length = 550)
    private String description;
    private boolean newPetOwner;
    private String state;
    private String city;
    private String district;
    @Column(precision = 15, scale = 12)
    private BigDecimal latitude;
    @Column(precision = 15, scale = 12)
    private BigDecimal longitude;
    @ElementCollection
    private Set<UUID> viewedPetIds;
    @ElementCollection
    private Set<String> preferredPetTypes;

    public AdoptionProfile(User user, String cep, String description, boolean newPetOwner, String state, String city, String district, BigDecimal latitude, BigDecimal longitude, Set<UUID> viewedPetIds, Set<String> preferredPetTypes) {
        this.user = user;
        this.cep = cep;
        this.description = description;
        this.newPetOwner = newPetOwner;
        this.state = state;
        this.city = city;
        this.district = district;
        this.latitude = latitude;
        this.longitude = longitude;
        this.viewedPetIds = viewedPetIds;
        this.preferredPetTypes = preferredPetTypes;
    }

    public AdoptionProfile(User user, String cep, String description, boolean newPetOwner, Set<UUID> viewedPetIds, Set<String> preferredPetTypes) {
        this.user = user;
        this.cep = cep;
        this.description = description;
        this.newPetOwner = newPetOwner;
        this.viewedPetIds = viewedPetIds;
        this.preferredPetTypes = preferredPetTypes;
    }
}
