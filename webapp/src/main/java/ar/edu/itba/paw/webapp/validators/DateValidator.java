package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.annotations.DateAnnotation;
import ar.edu.itba.paw.webapp.form.AppointmentForm;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateValidator implements ConstraintValidator<DateAnnotation, AppointmentForm> {
  @Override
  public boolean isValid(
      AppointmentForm appointmentForm, ConstraintValidatorContext constraintValidatorContext) {
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    String date = appointmentForm.getDate();
    if (!date.isEmpty()) {
      LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
      return dateTime.isAfter(LocalDateTime.now());
    }
    return false;
  }
}
