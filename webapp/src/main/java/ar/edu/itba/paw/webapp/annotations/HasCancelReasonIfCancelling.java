package ar.edu.itba.paw.webapp.annotations;

import ar.edu.itba.paw.webapp.validators.HasCancelReasonIfCancellingValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HasCancelReasonIfCancellingValidator.class)
public @interface HasCancelReasonIfCancelling {

  String message() default "A cancel reason must be provided if cancelling appointments";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
