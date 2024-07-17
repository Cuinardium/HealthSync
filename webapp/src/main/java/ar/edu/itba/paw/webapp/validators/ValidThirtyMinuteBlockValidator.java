package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.webapp.annotations.ValidThirtyMinuteBlock;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidThirtyMinuteBlockValidator
    implements ConstraintValidator<ValidThirtyMinuteBlock, String> {

  private ThirtyMinuteBlock[] enumConstants;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    for (ThirtyMinuteBlock thirtyMinuteBlock : enumConstants) {
      if (thirtyMinuteBlock.getBlockBeginning().equals(value)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void initialize(ValidThirtyMinuteBlock validThirtyMinuteBlock) {
    this.enumConstants = ThirtyMinuteBlock.values();
  }
}
