package br.pucpr.petapi.rest.pets;

import br.pucpr.petapi.rest.adoptionRequests.AdoptionRequest;
import br.pucpr.petapi.rest.petTypes.PetType;
import br.pucpr.petapi.rest.users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pet {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String nickname;
    private Integer age;

    @Column(length = 800)
    private String description;
    @ManyToOne
    @JsonIgnore
    private User user;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private LocalDateTime createdAt;
    @ManyToOne
    @JsonIgnore
    private PetType petType;
    @JsonIgnore
    @OneToMany(mappedBy = "pet")
    private Set<AdoptionRequest> adoptionRequests = new HashSet<>();

    public Pet(String name, String nickname, Integer age, String description, User user, LocalDateTime createdAt, PetType petType) {
        this.name = name;
        this.nickname = nickname;
        this.age = age;
        this.description = description;
        this.user = user;
        this.createdAt = createdAt;
        this.petType = petType;
    }

    @Transactional
    public void addAdoptionRequest(AdoptionRequest r){
        adoptionRequests.add(r);
        r.setPet(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pet pet)) return false;
        return getId().equals(pet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
