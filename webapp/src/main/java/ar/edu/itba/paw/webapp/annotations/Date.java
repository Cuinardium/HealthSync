package ar.edu.itba.paw.webapp.annotations;

import ar.edu.itba.paw.webapp.validators.DateValidator;
import ar.edu.itba.paw.webapp.validators.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidator.class)
public @interface Date {
    String message() default "Date cant be in the past";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
