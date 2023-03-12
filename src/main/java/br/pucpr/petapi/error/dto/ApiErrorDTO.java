package br.pucpr.petapi.error.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ApiErrorDTO {
    private List<String> errors;

    public ApiErrorDTO(String ... errors) {
        this.errors = List.of(errors);
    }}