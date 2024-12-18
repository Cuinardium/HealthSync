package ar.edu.itba.paw.webapp.annotations;

import ar.edu.itba.paw.webapp.validators.PasswordsMatchValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordsMatchValidator.class)
public @interface PasswordsMatch {

  String message() default "Passwords dont match";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
