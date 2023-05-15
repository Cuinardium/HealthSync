package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.webapp.annotations.DateAnnotation;
import java.time.LocalDate;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

@DateAnnotation
public class AppointmentForm {

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

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate date;

  private String block;

  @Size(min = 1, max = 100)
  private String description;

  private int docId;

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

  public ThirtyMinuteBlock getBlockEnum() {
    return ThirtyMinuteBlock.fromString(block);
  }

  public void setBlock(String block) {
    this.block = block;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getDocId() {
    return docId;
  }

  public void setDocId(int docId) {
    this.docId = docId;
  }
}
