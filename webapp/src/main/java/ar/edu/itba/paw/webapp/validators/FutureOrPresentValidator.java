package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.annotations.FutureOrPresent;
import java.time.LocalDate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FutureOrPresentValidator implements ConstraintValidator<FutureOrPresent, LocalDate> {

  @Override
  public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
    if (date == null) {
      return true; // Other validator will handle this
    }

    return !date.isBefore(LocalDate.now());
  }
}
