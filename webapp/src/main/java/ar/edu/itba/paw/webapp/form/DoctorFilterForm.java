package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public class DoctorFilterForm {
  private String name;

  private String city;

  private int specialtyCode = -1;

  private int healthInsuranceCode = -1;

  private int minRating = -1;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate date;

  private int from = 0, to = ThirtyMinuteBlock.values().length - 1;

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
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
