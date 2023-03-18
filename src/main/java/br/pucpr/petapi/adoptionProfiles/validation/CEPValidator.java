package br.pucpr.petapi.adoptionProfiles.validation;

import br.pucpr.petapi.lib.location.LocationUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class CEPValidator implements ConstraintValidator<ValidCEP, String> {
    private final LocationUtils utils;
    private boolean nullable;

    public CEPValidator(LocationUtils utils) {
        this.utils = utils;
    }

    @Override
    public void initialize(ValidCEP constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.nullable = constraintAnnotation.nullable();
    }

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext constraintValidatorContext) {
        if(nullable && cpf == null) return true;
        if(!nullable && cpf == null) return false;

        if(cpf.length() != 9 && cpf.length() != 8){
            return false;
        }

        var validChars = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-");
        for(String c: cpf.split("")){
            if(!validChars.contains(c)) return false;
        }

        if(Arrays.stream(cpf.split("")).filter(c -> c.equals("-")).count() >= 2) return false;

        return true;
    }
}
