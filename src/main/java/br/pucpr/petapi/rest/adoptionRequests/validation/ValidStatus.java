package br.pucpr.petapi.rest.adoptionRequests.validation;

import br.pucpr.petapi.rest.adoptionProfiles.validation.CEPValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StatusValidator.class)
public @interface ValidStatus {
    String message() default "Invalid Status.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
