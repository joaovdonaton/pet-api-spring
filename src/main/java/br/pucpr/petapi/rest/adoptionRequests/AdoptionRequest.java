package br.pucpr.petapi.rest.adoptionRequests;

import br.pucpr.petapi.rest.adoptionRequests.enums.Status;
import br.pucpr.petapi.rest.pets.Pet;
import br.pucpr.petapi.rest.users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdoptionRequest {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    @JsonIgnore
    private Pet pet;
    @ManyToOne
    @JsonIgnore
    private User userSender;
    @ManyToOne
    @JsonIgnore
    private User userReceiver;
    private String title;
    @Column(length = 750)
    private String message;
    @Enumerated(EnumType.STRING)
    private Status status;

    public AdoptionRequest(Pet pet, User userSender, User userReceiver, String title, String message, Status status) {
        this.pet = pet;
        this.userSender = userSender;
        this.userReceiver = userReceiver;
        this.title = title;
        this.message = message;
        this.status = status;
    }

//    @Override
//    public String toString() {
//        return "request from " +userSender.getUsername()+ " to " + userReceiver.getUsername();
//    }
}
