package br.pucpr.petapi.petTypes;

import br.pucpr.petapi.pets.Pet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PetType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "petType")
    private Set<Pet> pets;

    public PetType(String name) {
        this.name = name;
    }
}
