package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.annotations.FutureOrPresent;
import ar.edu.itba.paw.webapp.utils.DateUtil;
import java.time.LocalDate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FutureOrPresentValidator implements ConstraintValidator<FutureOrPresent, String> {
  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

    LocalDate date = DateUtil.parseDate(s);

    if (date == null) {
      return true; // Other validator will handle this
    }

    return !date.isBefore(LocalDate.now());
  }
}
