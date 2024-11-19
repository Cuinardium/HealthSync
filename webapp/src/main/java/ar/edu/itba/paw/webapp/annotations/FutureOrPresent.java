package ar.edu.itba.paw.webapp.annotations;

import ar.edu.itba.paw.webapp.validators.FutureOrPresentStringValidator;
import ar.edu.itba.paw.webapp.validators.FutureOrPresentValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = {FutureOrPresentValidator.class, FutureOrPresentStringValidator.class})
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FutureOrPresent {
  String message() default "Date must be in the present or future";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
