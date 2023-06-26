package ar.edu.itba.paw.webapp.annotations;

import ar.edu.itba.paw.webapp.validators.FileValidator;
import ar.edu.itba.paw.webapp.validators.ImageValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileValidator.class)
public @interface ValidateFile {
    String message() default "File must be a png/jpg/jpeg/pdf";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
