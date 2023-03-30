package br.pucpr.petapi.rest.pets.dto;

import br.pucpr.petapi.rest.pets.validation.ValidPetType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PetRegisterDTO {
    @NotEmpty(message = "name field must not be empty")
    @Length(min = 3, max = 30, message = "name field length must be in the interval [3,30]")
    private String name;
    @NotEmpty(message = "nickname field must not be empty")
    @Length(min = 3, max = 30, message = "nickname field length must be in the interval [3,30]")
    private String nickname;
    @NotEmpty(message = "description field must not be empty")
    @Length(min = 50, max = 750, message = "description field length must be in the interval [50,750]")
    private String description;
    @PositiveOrZero(message = "invalid age")
    @Max(value = 50, message = "invalid age")
    private Integer age;

    @ValidPetType(message = "Invalid pet type. View available types in pet type endpoints.")
    private String type;
}
