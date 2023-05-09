package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Specialty;

public class DoctorFilterForm {

  private String name = null;

  private int cityCode = -1;

  private int specialtyCode = -1;

  private int healthInsuranceCode = -1;

  public int getCityCode() {
    if (cityCode < 0 || cityCode > City.values().length) {
      return -1;
    }

    return cityCode;
  }

  public void setCityCode(int cityCode) {
    this.cityCode = cityCode;
  }

  public int getSpecialtyCode() {
    if (specialtyCode < 0 || specialtyCode > Specialty.values().length) {
      return -1;
    }

    return specialtyCode;
  }

  public void setSpecialtyCode(int specialtyCode) {
    this.specialtyCode = specialtyCode;
  }

  public int getHealthInsuranceCode() {
    if (healthInsuranceCode < 0 || healthInsuranceCode > HealthInsurance.values().length) {
      return -1;
    }

    return healthInsuranceCode;
  }

  public void setHealthInsuranceCode(int healthInsuranceCode) {
    this.healthInsuranceCode = healthInsuranceCode;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
