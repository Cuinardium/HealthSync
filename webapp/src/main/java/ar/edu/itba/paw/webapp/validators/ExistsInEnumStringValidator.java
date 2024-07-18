package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.annotations.ExistsInEnumString;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistsInEnumStringValidator
    implements ConstraintValidator<ExistsInEnumString, String> {

  private Enum<?>[] enumConstants;

  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

    if (s == null) {
      // @NotNull handles this case
      return true;
    }

    for (Enum<?> e : enumConstants) {
      if (e.name().equals(s)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void initialize(ExistsInEnumString existsInEnumString) {
    Class<? extends Enum<?>> enumClass = existsInEnumString.enumClass();
    this.enumConstants = enumClass.getEnumConstants();
  }
}
