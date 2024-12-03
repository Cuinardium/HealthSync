package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.annotations.ValidRange;
import ar.edu.itba.paw.webapp.query.AppointmentQuery;
import java.time.LocalDate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidAppointmentQueryRangeValidator
    implements ConstraintValidator<ValidRange, AppointmentQuery> {

  @Override
  public boolean isValid(
      AppointmentQuery appointmentQuery, ConstraintValidatorContext constraintValidatorContext) {

    LocalDate fromDate = appointmentQuery.getFromDate();
    LocalDate toDate = appointmentQuery.getToDate();

    if (fromDate == null && toDate == null) {
      return true; // Ok
    }

    if (fromDate == null || toDate == null) {
      return false; // Las dos fechas deben ser nulas o no nulas
    }

    return !fromDate.isAfter(toDate);
  }
}
