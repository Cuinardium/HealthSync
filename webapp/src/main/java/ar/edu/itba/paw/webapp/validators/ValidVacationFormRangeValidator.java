package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.webapp.annotations.ValidRange;
import ar.edu.itba.paw.webapp.form.DoctorVacationForm;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidVacationFormRangeValidator implements ConstraintValidator<ValidRange, DoctorVacationForm> {

  @Override
  public boolean isValid(
      DoctorVacationForm vacation, ConstraintValidatorContext constraintValidatorContext) {

    LocalDate fromDate = vacation.getFromDate();
    LocalDate toDate = vacation.getToDate();
    ThirtyMinuteBlock fromTime = vacation.getFromTimeEnum();
    ThirtyMinuteBlock toTime = vacation.getToTimeEnum();

    if (fromDate == null || toDate == null || fromTime == null || toTime == null) {
      // Other validators will take care of this
      return true;
    }

    boolean fromIsBeforeTo =
        fromDate.isBefore(toDate)
            || (fromDate.isEqual(toDate) && (fromTime.isBefore(toTime) || fromTime.equals(toTime)));

    LocalDate today = LocalDate.now();
    ThirtyMinuteBlock now = ThirtyMinuteBlock.fromTime(LocalTime.now());

    boolean fromIsAfterNow =
        fromDate.isAfter(today) || (fromDate.isEqual(today) && fromTime.isAfter(now));

    return fromIsBeforeTo && fromIsAfterNow;
  }
}
