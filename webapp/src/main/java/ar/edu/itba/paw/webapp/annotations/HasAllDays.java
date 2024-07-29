package ar.edu.itba.paw.webapp.annotations;

import ar.edu.itba.paw.webapp.validators.HasAllDaysValidator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HasAllDaysValidator.class)
public @interface HasAllDays {

    String message() default "Attending hours must have all days of the week";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
