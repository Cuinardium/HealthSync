package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CancelAppointmentForm {

  @NotNull(message = "NotNull.cancelAppointmentForm.status")
  @Pattern(regexp = "CANCELLED", message = "Pattern.cancelAppointmentForm.status")
  private String status;

  @Size(max = 1000, message = "Size.cancelAppointmentForm.description")
  private String description;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
