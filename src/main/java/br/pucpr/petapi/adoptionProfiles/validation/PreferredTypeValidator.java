package br.pucpr.petapi.adoptionProfiles.validation;

import br.pucpr.petapi.petTypes.PetTypeService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class PreferredTypeValidator implements ConstraintValidator<ValidPreferredTypes, Set<String>> {

    private final PetTypeService petTypeService;

    public PreferredTypeValidator(PetTypeService petTypeService) {
        this.petTypeService = petTypeService;
    }

    @Override
    public boolean isValid(Set<String> preferredTypes, ConstraintValidatorContext constraintValidatorContext) {
        var validTypes = petTypeService.getAllPetTypeNames();
        for(String t: preferredTypes){
            if(!validTypes.contains(t)) return false;
        }

        return true;
    }
}
