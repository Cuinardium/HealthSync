package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.webapp.annotations.HasCancelReasonIfCancelling;
import ar.edu.itba.paw.webapp.annotations.ValidRange;
import ar.edu.itba.paw.webapp.annotations.ValidThirtyMinuteBlock;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

@ValidRange(message = "ValidRange.doctorVacationForm")
@HasCancelReasonIfCancelling(message = "HasCancelReasonIfCancelling.doctorVacationForm")
public class DoctorVacationForm {

  @NotNull(message = "NotNull.doctorVacationForm.fromDate")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate fromDate;

  @NotNull(message = "NotNull.doctorVacationForm.fromTime")
  @ValidThirtyMinuteBlock(
      enumClass = ThirtyMinuteBlock.class,
      message = "ExistsInEnum.doctorVacationForm.fromTime")
  private String fromTime;

  @NotNull(message = "NotNull.doctorVacationForm.toDate")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate toDate;

  @NotNull(message = "NotNull.doctorVacationForm.toTime")
  @ValidThirtyMinuteBlock(
      enumClass = ThirtyMinuteBlock.class,
      message = "ExistsInEnum.doctorVacationForm.toTime")
  private String toTime;

  private boolean cancelAppointments;

  private String cancelReason = null;

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

  public String getCancelReason() {
    return cancelReason;
  }

  public void setCancelReason(String cancelReason) {
    this.cancelReason = cancelReason;
  }

  public boolean cancelAppointments() {
    return cancelAppointments;
  }

  public boolean getCancelAppointments() {
    return cancelAppointments;
  }

  public void setCancelAppointments(boolean cancelAppointments) {
    this.cancelAppointments = cancelAppointments;
  }

  public String getToTime() {
    return toTime;
  }

  public void setToTime(String toTime) {
    this.toTime = toTime;
  }

  public ThirtyMinuteBlock getFromTimeEnum() {
    return ThirtyMinuteBlock.fromBeginning(fromTime);
  }

  public ThirtyMinuteBlock getToTimeEnum() {
    return ThirtyMinuteBlock.fromEnd(toTime);
  }

  @Override
  public String toString() {
    return "DoctorVacationForm";
  }
}
