package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.webapp.annotations.ExistsInEnumString;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.validation.constraints.NotNull;

public class PatientEditForm extends UserEditForm {

  @NotNull(message = "NotNull.patientForm.healthInsurance")
  @ExistsInEnumString(
      enumClass = HealthInsurance.class,
      message = "ExistsInEnumString.patientForm.healthInsurance")
  @FormDataParam("healthInsurance")
  private String healthInsurance;

  public String getHealthInsurance() {
    return healthInsurance;
  }

  public HealthInsurance getHealthInsuranceEnum() {
    return HealthInsurance.valueOf(healthInsurance);
  }

  public void setHealthInsurance(String healthInsurance) {
    this.healthInsurance = healthInsurance;
  }
}
