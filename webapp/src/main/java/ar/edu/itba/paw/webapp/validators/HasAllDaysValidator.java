package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.annotations.HasAllDays;
import ar.edu.itba.paw.webapp.form.AttendingHoursForm;
import java.time.DayOfWeek;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HasAllDaysValidator
    implements ConstraintValidator<HasAllDays, List<AttendingHoursForm>> {

  @Override
  public boolean isValid(
      List<AttendingHoursForm> attendingHours,
      ConstraintValidatorContext constraintValidatorContext) {

    if (attendingHours == null) {
      return true;
    }

    // All days must be present only once
    Map<DayOfWeek, Boolean> days = new EnumMap<>(DayOfWeek.class);
    for (DayOfWeek day : DayOfWeek.values()) {
      days.put(day, false);
    }

    for (AttendingHoursForm attendingHour : attendingHours) {

      DayOfWeek day;
      try {
        day = DayOfWeek.valueOf(attendingHour.getDay());
      } catch (IllegalArgumentException | NullPointerException e) {
        return true; // Other validators will handle this
      }

      if (days.get(day)) {
        return false;
      }
      days.put(day, true);
    }

    return days.values().stream().allMatch(Boolean::booleanValue);
  }
}
