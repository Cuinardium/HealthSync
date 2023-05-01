package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.annotations.DateAnnotation;
import ar.edu.itba.paw.webapp.form.AppointmentForm;
import java.time.LocalDate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateValidator implements ConstraintValidator<DateAnnotation, AppointmentForm> {
  @Override
  public boolean isValid(
      AppointmentForm appointmentForm, ConstraintValidatorContext constraintValidatorContext) {

    LocalDate date = appointmentForm.getDate();
    return date.isAfter(LocalDate.now());
  }
}
