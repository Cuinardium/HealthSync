package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.webapp.annotations.ExistsInEnum;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class DoctorEditForm extends UserEditForm {
  @ExistsInEnum(enumClass = HealthInsurance.class)
  private Integer healthInsuranceCode = -1;

  @ExistsInEnum(enumClass = City.class)
  private Integer cityCode = -1;

  @Size(min = 1)
  @Pattern(regexp = "[a-zA-Z0-9. ]+")
  private String address;

  @ExistsInEnum(enumClass = Specialty.class)
  private Integer specialtyCode = -1;


  // Attending hours encoded as bits
  private long mondayAttendingHours;
  private long tuesdayAttendingHours;
  private long wednesdayAttendingHours;
  private long thursdayAttendingHours;
  private long fridayAttendingHours;
  private long saturdayAttendingHours;
  private long sundayAttendingHours;

  public int getHealthInsuranceCode() {
    return healthInsuranceCode;
  }

  public void setHealthInsuranceCode(int healthInsuranceCode) {
    this.healthInsuranceCode = healthInsuranceCode;
  }

  public int getCityCode() {
    return cityCode;
  }

  public void setCityCode(int cityCode) {
    this.cityCode = cityCode;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public int getSpecialtyCode() {
    return specialtyCode;
  }

  public void setSpecialtyCode(int specialtyCode) {
    this.specialtyCode = specialtyCode;
  }

  // =================== Attending hours ==============================
  public long getMondayAttendingHours() {
    return mondayAttendingHours;
  }

  public void setMondayAttendingHours(long mondayAttendingHours) {
    this.mondayAttendingHours = mondayAttendingHours;
  }

  public long getTuesdayAttendingHours() {
    return tuesdayAttendingHours;
  }

  public void setTuesdayAttendingHours(long tuesdayAttendingHours) {
    this.tuesdayAttendingHours = tuesdayAttendingHours;
  }

  public long getWednesdayAttendingHours() {
    return wednesdayAttendingHours;
  }

  public void setWednesdayAttendingHours(long wednesdayAttendingHours) {
    this.wednesdayAttendingHours = wednesdayAttendingHours;
  }

  public long getThursdayAttendingHours() {
    return thursdayAttendingHours;
  }

  public void setThursdayAttendingHours(long thursdayAttendingHours) {
    this.thursdayAttendingHours = thursdayAttendingHours;
  }

  public long getFridayAttendingHours() {
    return fridayAttendingHours;
  }

  public void setFridayAttendingHours(long fridayAttendingHours) {
    this.fridayAttendingHours = fridayAttendingHours;
  }

  public long getSaturdayAttendingHours() {
    return saturdayAttendingHours;
  }

  public void setSaturdayAttendingHours(long saturdayAttendingHours) {
    this.saturdayAttendingHours = saturdayAttendingHours;
  }

  public long getSundayAttendingHours() {
    return sundayAttendingHours;
  }

  public void setSundayAttendingHours(long sundayAttendingHours) {
    this.sundayAttendingHours = sundayAttendingHours;
  }
}
