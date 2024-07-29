package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.annotations.ValidRange;
import ar.edu.itba.paw.webapp.query.OccupiedHoursQuery;
import java.time.LocalDate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidOccupiedHoursQueryRangeValidator
    implements ConstraintValidator<ValidRange, OccupiedHoursQuery> {

  @Override
  public boolean isValid(
      OccupiedHoursQuery occupiedHoursQuery,
      ConstraintValidatorContext constraintValidatorContext) {

    LocalDate fromDate = occupiedHoursQuery.getFromDate();
    LocalDate toDate = occupiedHoursQuery.getToDate();

    if (fromDate == null || toDate == null) {
      return true; // Other validators will handle this
    }

    return !fromDate.isAfter(toDate);
  }
}
