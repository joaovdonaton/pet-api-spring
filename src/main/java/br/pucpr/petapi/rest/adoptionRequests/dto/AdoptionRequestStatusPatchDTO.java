package br.pucpr.petapi.rest.adoptionRequests.dto;

import br.pucpr.petapi.rest.adoptionRequests.enums.Status;
import br.pucpr.petapi.rest.adoptionRequests.validation.ValidStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdoptionRequestStatusPatchDTO {
    @NotNull
    private UUID requestId;
    @NonNull
    @ValidStatus(message = "Invalid Status: status must be one of: [ACCEPTED, REJECTED]")
    private String status;
}
