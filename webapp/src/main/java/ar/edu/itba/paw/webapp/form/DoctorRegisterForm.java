package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.webapp.annotations.ExistsInEnum;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

public class DoctorRegisterForm extends UserRegisterForm {

  @NotEmpty private List<Integer> healthInsuranceCodes = new ArrayList<>();

  @ExistsInEnum(enumClass = City.class)
  private Integer cityCode = -1;

  @Size(min = 1)
  @Pattern(regexp = "[a-zA-Z0-9. ñÑ]+")
  private String address;

  @ExistsInEnum(enumClass = Specialty.class)
  private Integer specialtyCode = -1;

  // Attending hours encoded as bits
  private List<Integer> mondayAttendingHours;
  private List<Integer> tuesdayAttendingHours;
  private List<Integer> wednesdayAttendingHours;
  private List<Integer> thursdayAttendingHours;
  private List<Integer> fridayAttendingHours;
  private List<Integer> saturdayAttendingHours;
  private List<Integer> sundayAttendingHours;

  public List<Integer> getHealthInsuranceCodes() {
    return healthInsuranceCodes;
  }

  public void setHealthInsuranceCodes(List<Integer> healthInsuranceCodes) {
    this.healthInsuranceCodes = healthInsuranceCodes;
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
  public List<Integer> getMondayAttendingHours() {
    return mondayAttendingHours;
  }

  public void setMondayAttendingHours(List<Integer> mondayAttendingHours) {
    if (mondayAttendingHours == null) {
      this.mondayAttendingHours = Collections.emptyList();
      return;
    }

    this.mondayAttendingHours = mondayAttendingHours;
  }

  public List<Integer> getTuesdayAttendingHours() {
    return tuesdayAttendingHours;
  }

  public void setTuesdayAttendingHours(List<Integer> tuesdayAttendingHours) {
    if (tuesdayAttendingHours == null) {
      this.tuesdayAttendingHours = Collections.emptyList();
      return;
    }

    this.tuesdayAttendingHours = tuesdayAttendingHours;
  }

  public List<Integer> getWednesdayAttendingHours() {
    return wednesdayAttendingHours;
  }

  public void setWednesdayAttendingHours(List<Integer> wednesdayAttendingHours) {
    if (wednesdayAttendingHours == null) {
      this.wednesdayAttendingHours = Collections.emptyList();
      return;
    }

    this.wednesdayAttendingHours = wednesdayAttendingHours;
  }

  public List<Integer> getThursdayAttendingHours() {
    return thursdayAttendingHours;
  }

  public void setThursdayAttendingHours(List<Integer> thursdayAttendingHours) {
    if (thursdayAttendingHours == null) {
      this.thursdayAttendingHours = Collections.emptyList();
      return;
    }

    this.thursdayAttendingHours = thursdayAttendingHours;
  }

  public List<Integer> getFridayAttendingHours() {
    return fridayAttendingHours;
  }

  public void setFridayAttendingHours(List<Integer> fridayAttendingHours) {
    if (fridayAttendingHours == null) {
      this.fridayAttendingHours = Collections.emptyList();
      return;
    }

    this.fridayAttendingHours = fridayAttendingHours;
  }

  public List<Integer> getSaturdayAttendingHours() {
    return saturdayAttendingHours;
  }

  public void setSaturdayAttendingHours(List<Integer> saturdayAttendingHours) {
    if (saturdayAttendingHours == null) {
      this.saturdayAttendingHours = Collections.emptyList();
      return;
    }

    this.saturdayAttendingHours = saturdayAttendingHours;
  }

  public List<Integer> getSundayAttendingHours() {
    return sundayAttendingHours;
  }

  public void setSundayAttendingHours(List<Integer> sundayAttendingHours) {
    if (sundayAttendingHours == null) {
      this.sundayAttendingHours = Collections.emptyList();
      return;
    }

    this.sundayAttendingHours = sundayAttendingHours;
  }
}
