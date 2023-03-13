package br.pucpr.petapi.adoptionProfiles;

import br.pucpr.petapi.users.User;
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
    @JoinColumn(name = "user_id")
    private User user;
    private String cep;
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
}