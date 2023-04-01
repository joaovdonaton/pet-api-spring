package br.pucpr.petapi.rest.users;

import br.pucpr.petapi.rest.adoptionProfiles.AdoptionProfile;
import br.pucpr.petapi.rest.adoptionRequests.AdoptionRequest;
import br.pucpr.petapi.rest.pets.Pet;
import br.pucpr.petapi.rest.roles.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(unique = true)
    @Length(min = 5, max = 30)
    private String username;
    private String password;
    @Length(min = 6)
    private String name;
    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();
    @OneToOne(mappedBy = "user")
    private AdoptionProfile adoptionProfile;
    @OneToMany(mappedBy = "user")
    private Set<Pet> pets = new HashSet<>();
    @OneToMany(mappedBy = "userReceiver")
    private Set<AdoptionRequest> incomingRequests = new HashSet<>();
    @OneToMany(mappedBy = "userSender")
    private Set<AdoptionRequest> outgoingRequests = new HashSet<>();

    public User(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    @Transactional
    public void addRole(Role role){
        var users = role.getUsers();
        users.add(this);
        role.setUsers(users);

        roles.add(role);
    }

    @Transactional
    public void addPet(Pet pet){
        pet.setUser(this);
        pets.add(pet);
    }

    public Set<String> getRoleNames(){
        return getRoles().stream().map(Role::getName).collect(Collectors.toSet());
    }
}
