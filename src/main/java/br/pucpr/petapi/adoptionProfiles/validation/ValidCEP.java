package br.pucpr.petapi.adoptionProfiles.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CEPValidator.class)
public @interface ValidCEP {
    String message() default "Invalid CEP.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
