package br.pucpr.petapi.rest.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {
    private UUID id;
    private String name;
    private String username;
    private Set<String> roles;
}
