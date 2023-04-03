package br.pucpr.petapi.rest.adoptionRequests.dto;

import br.pucpr.petapi.rest.adoptionRequests.enums.Status;
import br.pucpr.petapi.rest.pets.dto.PetInfoDTO;
import br.pucpr.petapi.rest.users.dto.UserInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdoptionRequestInfoDTO {
    private UUID id;
    private UUID petId;
    private UUID senderId;
    private UUID receiverId;
    private String title;
    private String message;
    private Status status;
}
