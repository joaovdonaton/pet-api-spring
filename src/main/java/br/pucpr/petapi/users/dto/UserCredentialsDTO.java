package br.pucpr.petapi.users.dto;

import lombok.Data;

@Data
public class UserCredentialsDTO {
    private String username;
    private String password;
}
