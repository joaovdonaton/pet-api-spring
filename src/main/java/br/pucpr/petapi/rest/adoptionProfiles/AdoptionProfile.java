package br.pucpr.petapi.rest.adoptionProfiles;

import br.pucpr.petapi.rest.users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;
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
    private String state; // level 2
    private String city; // level 1
    private String district; // level 0
    @Column(precision = 15, scale = 12)
    private BigDecimal latitude;
    @Column(precision = 15, scale = 12)
    private BigDecimal longitude;
    @ElementCollection
    @JsonIgnore
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

    public AdoptionProfile(String district, String city, String state){
        this.district = district;
        this.city = city;
        this.state = state;
    }

    /**
     * @param level nível da localização, quanto menor, mais "específico"
     * @return String com nome da localização no level
     * Os cases deste switch definem os níveis das localizações.
     * Por padrão a ordem é bairro -> cidade -> estado.
     */
    public String getLocationNameByLevel(int level){
        return switch (level){
            case 0 -> getDistrict();
            case 1 -> getCity();
            case 2 -> getState();

            default -> throw new IllegalArgumentException("Invalid location level" + level);
        };
    }

    /**
     * @return retorna a quantidade levels de localização que essa entidade tem (No AdoptionProfile são 3: bairro,
     * cidade e estado)
     */
    public static int getLevels(){
        return 3;
    }

    @Transactional
    public void addViewedId(UUID id){
        viewedPetIds.add(id);
    }

    @Transactional
    public void clearViewedIds(){
        viewedPetIds.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdoptionProfile that)) return false;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
