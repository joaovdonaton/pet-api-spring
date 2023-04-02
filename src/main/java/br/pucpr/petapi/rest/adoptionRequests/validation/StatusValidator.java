package br.pucpr.petapi.rest.adoptionRequests.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.stream.Stream;

public class StatusValidator implements ConstraintValidator<ValidStatus, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return List.of("ACCEPTED", "REJECTED").contains(s);
    }
}
