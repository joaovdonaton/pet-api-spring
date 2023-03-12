package br.pucpr.petapi.users;

import br.pucpr.petapi.roles.Role;
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

    public Set<String> getRoleNames(){
        return getRoles().stream().map(Role::getName).collect(Collectors.toSet());
    }
}