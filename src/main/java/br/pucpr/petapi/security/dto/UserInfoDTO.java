package br.pucpr.petapi.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserInfoDTO {
    private UUID id;
    private String name;
    private String username;
    private Set<String> roles;
}
