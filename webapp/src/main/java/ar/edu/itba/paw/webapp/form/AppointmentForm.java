package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.webapp.annotations.DateAnnotation;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ar.edu.itba.paw.webapp.annotations.FutureOrPresent;
import ar.edu.itba.paw.webapp.annotations.ValidThirtyMinuteBlock;
import org.springframework.format.annotation.DateTimeFormat;

public class AppointmentForm {

  @NotNull(message = "NotNull.appointmentForm.date")
  @FutureOrPresent(message = "FutureOrPresent.appointmentForm.date")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate date;

  @NotNull(message = "NotNull.appointmentForm.block")
  @ValidThirtyMinuteBlock(message = "ExistsInEnum.appointmentForm.block")
  private String timeBlock;

  @NotNull(message = "NotNull.appointmentForm.description")
  @Size(min = 1, max = 100, message = "Size.appointmentForm.description")
  private String description;

  @NotNull(message = "NotNull.appointmentForm.docId")
  private Long doctorId;

  @Override
  public String toString() {
    return "AppointmentForm [date="
        + date
        + ", block="
        + timeBlock
        + ", description="
        + description
        + ", docId="
        + doctorId
        + "]";
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public String getTimeBlock() {

    if (timeBlock == null) {
      return null;
    }

    return timeBlock;
  }

  public void setTimeBlock(String timeBlock) {
    this.timeBlock = timeBlock;
  }

  public ThirtyMinuteBlock getBlockEnum() {
    return ThirtyMinuteBlock.fromBeginning(timeBlock);
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getDoctorId() {
    return doctorId;
  }

  public void setDoctorId(Long doctorId) {
    this.doctorId = doctorId;
  }
}
