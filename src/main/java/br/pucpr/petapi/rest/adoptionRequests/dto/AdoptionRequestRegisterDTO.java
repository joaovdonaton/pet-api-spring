package br.pucpr.petapi.rest.adoptionRequests.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AdoptionRequestRegisterDTO {
    @NotNull
    private UUID petId;
    @NotEmpty
    @Length(min = 20, max = 70, message = "title length must be between 20 and 70 characters")
    private String title;
    @NotEmpty
    @Length(min = 50, max = 750, message = "message length must be between 50 and 750 characters")
    private String message;
}
