package br.pucpr.petapi.pets.dto;

import br.pucpr.petapi.pets.validation.ValidPetType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PetInfoDTO {
    private UUID id;
    private String name;
    private String nickname;
    private String description;
    private Integer age;
    private String type;
}
