package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.webapp.annotations.ExistsInEnum;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class DoctorRegisterForm extends UserRegisterForm {

  @NotEmpty
  private List<Integer> healthInsuranceCodes = new ArrayList<>();

  @ExistsInEnum(enumClass = City.class)
  private Integer cityCode = -1;

  @Size(min = 1)
  @Pattern(regexp = "[a-zA-Z0-9. ]+")
  private String address;

  @ExistsInEnum(enumClass = Specialty.class)
  private Integer specialtyCode = -1;

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
}
