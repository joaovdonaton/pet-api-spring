package br.pucpr.petapi.rest.pets.validation;

import br.pucpr.petapi.rest.petTypes.PetTypeService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PetTypeValidator implements ConstraintValidator<ValidPetType, String> {
    private final PetTypeService service;

    public PetTypeValidator(PetTypeService service) {
        this.service = service;
    }

    @Override
    public boolean isValid(String o, ConstraintValidatorContext constraintValidatorContext) {
        if(o == null) return false;

        var types = service.getAllPetTypeNames();

        if(types.contains(o.toLowerCase())) return true;

        return false;
    }
}
