package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.annotations.ExistsInEnumString;
import ar.edu.itba.paw.webapp.annotations.ValidThirtyMinuteBlock;
import java.time.DayOfWeek;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class AttendingHoursForm {

  @NotNull(message = "NotNull.attendingHoursForm.day")
  @ExistsInEnumString(
      enumClass = DayOfWeek.class,
      message = "ExistsInEnumString.attendingHoursForm.day")
  private String day;

  @NotNull(message = "NotNull.attendingHoursForm.attendingHours")
  @Valid
  private List<
          @ValidThirtyMinuteBlock(
              message = "ValidThirtyMinuteBlock.attendingHoursForm.attendingHours")
          String>
      hours;

  public String getDay() {
    return day;
  }

  public void setDay(String day) {
    this.day = day;
  }

  public List<String> getHours() {
    return hours;
  }

  public void setHours(List<String> hours) {
    this.hours = hours;
  }

  @Override
  public String toString() {
    return day;
  }
}
