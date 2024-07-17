package ar.edu.itba.paw.webapp.annotations;

import ar.edu.itba.paw.webapp.validators.ValidRangeValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidRangeValidator.class)
public @interface ValidRange {

  String message() default "Invalid date and time range";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
