package ar.edu.itba.paw.webapp.query;

import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.webapp.annotations.ExistsInEnumString;
import ar.edu.itba.paw.webapp.annotations.ValidThirtyMinuteBlock;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.ws.rs.QueryParam;

public class DoctorQuery extends PageQuery {

  @QueryParam("name")
  private String name;

  @QueryParam("date")
  private String date;

  @ValidThirtyMinuteBlock(message = "ExistsInEnum.doctorQuery.fromTime")
  @QueryParam("fromTime")
  private String fromTime;

  @ValidThirtyMinuteBlock(message = "ExistsInEnum.doctorQuery.toTime")
  @QueryParam("toTime")
  private String toTime;

  @Valid
  @QueryParam("specialty")
  private Set<
          @ExistsInEnumString(
              enumClass = Specialty.class,
              message = "ExistsInEnumString.doctorForm.specialty")
          String>
      specialties;

  @QueryParam("city")
  private Set<String> cities;

  @Valid
  @QueryParam("healthInsurance")
  private Set<
          @ExistsInEnumString(
              enumClass = HealthInsurance.class,
              message = "ExistsInEnumString.doctorForm.healthInsurance")
          String>
      healthInsurances;

  @QueryParam("minRating")
  private Integer minRating;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  // TODO: use @DateTimeFormat(pattern = "yyyy-MM-dd") instead of this method
  // But it's not working for some reason
  public LocalDate getLocalDate() {
    if (date == null) {
      return null;
    }

    LocalDate date;

    try {
      date = LocalDate.parse(this.date);
    } catch (DateTimeParseException e) {
      date = null;
    }

    return date;
  }

  public String getFromTime() {
    return fromTime;
  }

  public void setFromTime(String fromTime) {
    this.fromTime = fromTime;
  }

  public ThirtyMinuteBlock getFromTimeEnum() {
    return ThirtyMinuteBlock.fromBeginning(fromTime);
  }

  public String getToTime() {
    return toTime;
  }

  public void setToTime(String toTime) {
    this.toTime = toTime;
  }

  public ThirtyMinuteBlock getToTimeEnum() {
    return ThirtyMinuteBlock.fromBeginning(toTime);
  }

  public Set<String> getCities() {
    return cities;
  }

  public void setCities(Set<String> cities) {
    this.cities = cities;
  }

  public Set<String> getHealthInsurances() {
    return healthInsurances;
  }

  public void setHealthInsurances(Set<String> healthInsurances) {
    this.healthInsurances = healthInsurances;
  }

  public Set<HealthInsurance> getHealthInsurancesEnum() {
    return healthInsurances.stream().map(HealthInsurance::valueOf).collect(Collectors.toSet());
  }

  public Set<String> getSpecialties() {
    return specialties;
  }

  public void setSpecialties(Set<String> specialties) {
    this.specialties = specialties;
  }

  public Set<Specialty> getSpecialtiesEnum() {
    return specialties.stream().map(Specialty::valueOf).collect(Collectors.toSet());
  }

  public Integer getMinRating() {
    return minRating;
  }

  public void setMinRating(Integer minRating) {
    this.minRating = minRating;
  }

  @Override
  public String toString() {
    return "DoctorQuery{"
        + "name='"
        + name
        + '\''
        + ", date="
        + date
        + ", fromTime='"
        + fromTime
        + '\''
        + ", toTime='"
        + toTime
        + '\''
        + ", specialties="
        + specialties
        + ", cities="
        + cities
        + ", healthInsurances="
        + healthInsurances
        + ", minRating="
        + minRating
        + '}';
  }
}
