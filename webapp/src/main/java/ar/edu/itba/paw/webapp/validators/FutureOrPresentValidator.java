package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.webapp.annotations.FutureOrPresent;
import ar.edu.itba.paw.webapp.form.AppointmentForm;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FutureOrPresentValidator
    implements ConstraintValidator<FutureOrPresent, AppointmentForm> {

  @Override
  public boolean isValid(
      AppointmentForm appointment, ConstraintValidatorContext constraintValidatorContext) {
    LocalDate date = appointment.getDate();
    ThirtyMinuteBlock time = appointment.getBlockEnum();
    if (date == null || time == null) {
      return true; // Other validator will handle this
    }

    if (date.isBefore(LocalDate.now())) {
      return false;
    }

    if (date.isAfter(LocalDate.now())) {
      return true;
    }

    // Today, so i have to check times
    ThirtyMinuteBlock now = ThirtyMinuteBlock.fromTime(LocalTime.now());
    return time.isAfter(now);
  }
}
