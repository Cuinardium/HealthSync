package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.webapp.annotations.ValidThirtyMinuteBlock;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidThirtyMinuteBlockValidator
    implements ConstraintValidator<ValidThirtyMinuteBlock, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    if (value == null) {
      return true; // Let @NotNull handle this
    }

    for (ThirtyMinuteBlock thirtyMinuteBlock : ThirtyMinuteBlock.values()) {
      if (thirtyMinuteBlock.getBlockBeginning().equals(value)) {
        return true;
      }
    }
    return false;
  }
}
