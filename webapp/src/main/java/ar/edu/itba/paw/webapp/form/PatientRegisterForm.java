package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.webapp.annotations.ExistsInEnum;

public class PatientRegisterForm extends UserRegisterForm {

  @ExistsInEnum(enumClass = HealthInsurance.class)
  private Integer healthInsuranceCode = -1;

  public int getHealthInsuranceCode() {
    return healthInsuranceCode;
  }

  public void setHealthInsuranceCode(int healthInsuranceCode) {
    this.healthInsuranceCode = healthInsuranceCode;
  }
}
