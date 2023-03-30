package br.pucpr.petapi.rest.users.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserRegisterDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String name;
    @NotBlank
    @Length(min = 8, message = "Password length must be at least 8 characters")
    private String password;
}
