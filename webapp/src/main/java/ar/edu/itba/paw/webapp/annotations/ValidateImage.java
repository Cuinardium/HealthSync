package ar.edu.itba.paw.webapp.annotations;

import ar.edu.itba.paw.webapp.validators.ImageValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageValidator.class)
public @interface ValidateImage {
  String message() default "Image must be a jpeg of size < 8MB";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
