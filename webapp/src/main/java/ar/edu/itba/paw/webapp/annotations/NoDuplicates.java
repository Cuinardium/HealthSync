package ar.edu.itba.paw.webapp.annotations;

import ar.edu.itba.paw.webapp.validators.NoDuplicatesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NoDuplicatesValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NoDuplicates {
    String message() default "List contains duplicate elements";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


