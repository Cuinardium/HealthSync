package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.annotations.ExistsInEnum;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistsInEnumValidator implements ConstraintValidator<ExistsInEnum, Integer> {

  private Enum<?>[] enumConstants;

  @Override
  public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
    return value >= 0 && value < enumConstants.length;
  }

  @Override
  public void initialize(ExistsInEnum existsInEnum) {
    Class<? extends Enum<?>> enumClass = existsInEnum.enumClass();
    this.enumConstants = enumClass.getEnumConstants();
  }
}
