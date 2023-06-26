package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

public class DoctorFilterForm {
  private String name;

  private Set<String> cities;

  private Set<Integer> specialtyCodes;

  private Set<Integer> healthInsuranceCodes;

  private int minRating = -1;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate date;

  private int from = 0, to = ThirtyMinuteBlock.values().length - 1;

  public Set<String> getCities() {
    return cities;
  }

  public void setCities(Set<String> cities) {
    this.cities = cities;
  }

  public Set<Integer> getSpecialtyCodes() {
    return specialtyCodes;
  }

  public void setSpecialtyCodes(Set<Integer> specialtieCodes) {
    this.specialtyCodes = specialtieCodes;
  }

  public Set<Integer> getHealthInsuranceCodes() {
    return healthInsuranceCodes;
  }

  public void setHealthInsuranceCodes(Set<Integer> healthInsuranceCodes) {
    this.healthInsuranceCodes = healthInsuranceCodes;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public int getFrom() {
    return from;
  }

  public void setFrom(int from) {
    this.from = from;
  }

  public int getTo() {
    return to;
  }

  public void setTo(int to) {
    this.to = to;
  }

  public int getMinRating() {
    return minRating;
  }

  public void setMinRating(int minRating) {
    this.minRating = minRating;
  }
}
