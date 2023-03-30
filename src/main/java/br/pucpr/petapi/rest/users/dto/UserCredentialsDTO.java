package br.pucpr.petapi.rest.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCredentialsDTO {
    private String username;
    private String password;
}
