package br.pucpr.petapi.rest.adoptionProfiles.validation;

import br.pucpr.petapi.rest.petTypes.PetTypeService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class PreferredTypeValidator implements ConstraintValidator<ValidPreferredTypes, Set<String>> {

    private final PetTypeService petTypeService;
    private boolean nullable;

    public PreferredTypeValidator(PetTypeService petTypeService) {
        this.petTypeService = petTypeService;
    }

    @Override
    public void initialize(ValidPreferredTypes constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.nullable = constraintAnnotation.nullable();
    }

    @Override
    public boolean isValid(Set<String> preferredTypes, ConstraintValidatorContext constraintValidatorContext) {
        if(nullable && preferredTypes == null) return true;
        if(!nullable && preferredTypes == null) return false;

        var validTypes = petTypeService.getAllPetTypeNames();
        for(String t: preferredTypes){
            if(!validTypes.contains(t)) return false;
        }

        return true;
    }
}
