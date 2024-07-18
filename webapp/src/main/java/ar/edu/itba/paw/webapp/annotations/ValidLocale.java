package ar.edu.itba.paw.webapp.annotations;

import ar.edu.itba.paw.webapp.validators.LocaleValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = LocaleValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLocale {
    String message() default "Invalid locale";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}