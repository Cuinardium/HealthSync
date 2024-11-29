package ar.edu.itba.paw.webapp.query;

import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.webapp.annotations.ExistsInEnumString;
import ar.edu.itba.paw.webapp.annotations.FutureOrPresent;
import ar.edu.itba.paw.webapp.annotations.ValidRange;
import ar.edu.itba.paw.webapp.annotations.ValidThirtyMinuteBlock;
import ar.edu.itba.paw.webapp.utils.DateUtil;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.ws.rs.QueryParam;

@ValidRange(message = "ValidRange.doctorQuery")
public class DoctorQuery extends PageQuery {

  @QueryParam("name")
  private String name;

  @FutureOrPresent(message = "FutureOrPresent.doctorQuery.date")
  @Pattern(regexp = DateUtil.DATE_REGEX, message = "Pattern.query.date")
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

  @Min(value = 1, message = "Min.doctorQuery.minRating")
  @Max(value = 5, message = "Min.doctorQuery.minRating")
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

  public LocalDate getLocalDate() {
    return DateUtil.parseDate(date);
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
    return "{" +
            (name != null ? "name='" + name + "'," : "") +
            (date != null ? "date='" + date + "'," : "") +
            (fromTime != null ? "fromTime='" + fromTime + "'," : "") +
            (toTime != null ? "toTime='" + toTime + "'," : "") +
            (specialties != null && !specialties.isEmpty() ? "specialties=" + specialties + "," : "") +
            (cities != null && !cities.isEmpty() ? "cities=" + cities + "," : "") +
            (healthInsurances != null && !healthInsurances.isEmpty() ? "healthInsurances=" + healthInsurances + "," : "") +
            (minRating != null ? "minRating=" + minRating + "," : "") +
            '}';
  }
}
