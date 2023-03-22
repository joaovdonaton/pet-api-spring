package br.pucpr.petapi.pets.validation;

import br.pucpr.petapi.adoptionProfiles.validation.CEPValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PetTypeValidator.class)
public @interface ValidPetType {
    String message() default "Invalid Pet Type.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
