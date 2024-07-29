package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.webapp.annotations.ValidRange;
import ar.edu.itba.paw.webapp.query.DoctorQuery;
import java.time.LocalDate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidDoctorQueryRangeValidator
    implements ConstraintValidator<ValidRange, DoctorQuery> {

  @Override
  public boolean isValid(
      DoctorQuery doctorQuery, ConstraintValidatorContext constraintValidatorContext) {

    LocalDate date = doctorQuery.getLocalDate();
    ThirtyMinuteBlock fromTime = doctorQuery.getFromTimeEnum();
    ThirtyMinuteBlock toTime = doctorQuery.getToTimeEnum();

    if (date == null && fromTime == null && toTime == null) {
      // Other validators will take care of this
      return true;
    }

    // If any is null, all must be null
    if (date == null || fromTime == null || toTime == null) {
      return false;
    }

    return !fromTime.isAfter(toTime);
  }
}
