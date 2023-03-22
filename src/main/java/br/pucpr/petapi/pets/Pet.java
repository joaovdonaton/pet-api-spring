package br.pucpr.petapi.pets;

import br.pucpr.petapi.adoptionProfiles.AdoptionProfile;
import br.pucpr.petapi.petTypes.PetType;
import br.pucpr.petapi.users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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
//    @JoinTable(
//            name = "pet_types_pets",
//            joinColumns = @JoinColumn(name = "pet_type_id"),
//            inverseJoinColumns = @JoinColumn(name = "pet_id")
//    )
    @JsonIgnore
    private PetType petType;

    public Pet(String name, String nickname, Integer age, String description, User user, LocalDateTime createdAt, PetType petType) {
        this.name = name;
        this.nickname = nickname;
        this.age = age;
        this.description = description;
        this.user = user;
        this.createdAt = createdAt;
        this.petType = petType;
    }
}
