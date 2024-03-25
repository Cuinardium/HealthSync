package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public class DoctorDeleteVacationForm {

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate fromDate;

  private String fromTime;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate toDate;

  private String toTime;

  public LocalDate getFromDate() {
    return fromDate;
  }

  public void setFromDate(LocalDate fromDate) {
    this.fromDate = fromDate;
  }

  public String getFromTime() {
    return fromTime;
  }

  public void setFromTime(String fromTime) {
    this.fromTime = fromTime;
  }

  public LocalDate getToDate() {
    return toDate;
  }

  public void setToDate(LocalDate toDate) {
    this.toDate = toDate;
  }

  public String getToTime() {
    return toTime;
  }

  public void setToTime(String toTime) {
    this.toTime = toTime;
  }

  public ThirtyMinuteBlock getFromTimeEnum() {
    return ThirtyMinuteBlock.fromString(fromTime);
  }

  public ThirtyMinuteBlock getToTimeEnum() {
    return ThirtyMinuteBlock.fromString(toTime);
  }
}