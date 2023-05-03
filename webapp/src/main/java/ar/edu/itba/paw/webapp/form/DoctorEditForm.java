package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.webapp.annotations.ExistsInEnum;
import java.util.List;
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

  public enum DayEnum {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;
  }

  public List<HourRangeForm> attendingHours;

  public List<HourRangeForm> getAttendingHours() {
    return attendingHours;
  }

  public void setAttendingHours(List<HourRangeForm> attendingHours) {
    this.attendingHours = attendingHours;
  }

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
}
