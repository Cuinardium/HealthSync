package ar.edu.itba.paw.webapp.annotations;

import ar.edu.itba.paw.webapp.validators.ExistsInEnumStringValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE_USE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistsInEnumStringValidator.class)
public @interface ExistsInEnumString {
  String message() default "String is not a valid enum name";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  Class<? extends Enum<?>> enumClass();
}
