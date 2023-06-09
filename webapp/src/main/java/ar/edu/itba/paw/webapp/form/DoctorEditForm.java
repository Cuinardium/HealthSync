package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.AttendingHours;
import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.webapp.annotations.ExistsInEnum;
import java.time.DayOfWeek;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

public class DoctorEditForm extends UserEditForm {
  @NotEmpty private List<Integer> healthInsuranceCodes;

  @ExistsInEnum(enumClass = City.class)
  private Integer cityCode = -1;

  @Size(min = 1)
  @Pattern(regexp = "[a-zA-Z0-9. ]+")
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

  // TODO: needs refactor, duplicate method in doctorRegisterForm
  public Map<DayOfWeek, List<Integer>> getAttendingHours() {
    Map<DayOfWeek, List<Integer>> attendingHours = new HashMap<>();

    attendingHours.put(DayOfWeek.MONDAY, getMondayAttendingHours());
    attendingHours.put(DayOfWeek.TUESDAY, getTuesdayAttendingHours());
    attendingHours.put(DayOfWeek.WEDNESDAY, getWednesdayAttendingHours());
    attendingHours.put(DayOfWeek.THURSDAY, getThursdayAttendingHours());
    attendingHours.put(DayOfWeek.FRIDAY, getFridayAttendingHours());
    attendingHours.put(DayOfWeek.SATURDAY, getSaturdayAttendingHours());
    attendingHours.put(DayOfWeek.SUNDAY, getSundayAttendingHours());

    return attendingHours;
  }

  // TODO: needs refactor, duplicate method in doctorRegisterForm
  public void setAttendingHours(Set<AttendingHours> attendingHours) {

    setMondayAttendingHours(
        attendingHours
            .stream()
            .filter((a) -> a.getDay().equals(DayOfWeek.MONDAY))
            .map((a) -> a.getHourBlock().ordinal())
            .collect(Collectors.toList()));
    setTuesdayAttendingHours(
        attendingHours
            .stream()
            .filter((a) -> a.getDay().equals(DayOfWeek.TUESDAY))
            .map((a) -> a.getHourBlock().ordinal())
            .collect(Collectors.toList()));
    setWednesdayAttendingHours(
        attendingHours
            .stream()
            .filter((a) -> a.getDay().equals(DayOfWeek.WEDNESDAY))
            .map((a) -> a.getHourBlock().ordinal())
            .collect(Collectors.toList()));
    setThursdayAttendingHours(
        attendingHours
            .stream()
            .filter((a) -> a.getDay().equals(DayOfWeek.THURSDAY))
            .map((a) -> a.getHourBlock().ordinal())
            .collect(Collectors.toList()));
    setFridayAttendingHours(
        attendingHours
            .stream()
            .filter((a) -> a.getDay().equals(DayOfWeek.FRIDAY))
            .map((a) -> a.getHourBlock().ordinal())
            .collect(Collectors.toList()));
    setSaturdayAttendingHours(
        attendingHours
            .stream()
            .filter((a) -> a.getDay().equals(DayOfWeek.SATURDAY))
            .map((a) -> a.getHourBlock().ordinal())
            .collect(Collectors.toList()));
    setSundayAttendingHours(
        attendingHours
            .stream()
            .filter((a) -> a.getDay().equals(DayOfWeek.SUNDAY))
            .map((a) -> a.getHourBlock().ordinal())
            .collect(Collectors.toList()));
  }
}
