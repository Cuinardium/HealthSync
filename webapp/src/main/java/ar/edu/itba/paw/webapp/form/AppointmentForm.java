package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.webapp.annotations.DateAnnotation;
import ar.edu.itba.paw.webapp.annotations.ExistsInEnumString;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

@DateAnnotation
public class AppointmentForm {

  @NotNull(message = "NotNull.appointmentForm.date")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate date;

  @NotNull(message = "NotNull.appointmentForm.block")
  @ExistsInEnumString(
      enumClass = ThirtyMinuteBlock.class,
      message = "ExistsInEnumString.appointmentForm.block")
  private String block;

  @NotNull(message = "NotNull.appointmentForm.description")
  @Size(min = 1, max = 100, message = "Size.appointmentForm.description")
  private String description;

  @NotNull(message = "NotNull.appointmentForm.docId")
  private Long docId;

  @Override
  public String toString() {
    return "AppointmentForm [date="
        + date
        + ", block="
        + block
        + ", description="
        + description
        + ", docId="
        + docId
        + "]";
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public String getBlock() {

    if (block == null) {
      return null;
    }

    return block;
  }

  public void setBlock(String block) {
    this.block = block;
  }

  public ThirtyMinuteBlock getBlockEnum() {
    return ThirtyMinuteBlock.fromBeginning(block);
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getDocId() {
    return docId;
  }

  public void setDocId(Long docId) {
    this.docId = docId;
  }
}
