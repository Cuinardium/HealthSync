package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.webapp.annotations.ExistsInEnumString;
import javax.validation.constraints.NotNull;

public class PatientRegisterForm extends UserRegisterForm {

  @NotNull(message = "NotNull.patientForm.healthInsurance")
  @ExistsInEnumString(
      enumClass = HealthInsurance.class,
      message = "ExistsInEnumString.patientForm.healthInsurance")
  private String healthInsurance;

  public String getHealthInsurance() {
    return healthInsurance;
  }

  public void setHealthInsurance(String healthInsurance) {
    this.healthInsurance = healthInsurance;
  }

  public HealthInsurance getHealthInsuranceEnum() {
    return HealthInsurance.valueOf(healthInsurance);
  }
}
