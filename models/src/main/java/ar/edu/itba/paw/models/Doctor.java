package ar.edu.itba.paw.models;

import java.util.List;

public class Doctor extends User {

  private final List<HealthInsurance> healthInsurances;
  private final Specialty specialty;
  private final Location location;
  private final AttendingHours attendingHours;
  private final Float rating;
  private final Integer ratingCount;

  public Doctor(
      long id,
      String email,
      String password,
      String firstName,
      String lastName,
      Image image,
      List<HealthInsurance> healthInsurances,
      Specialty specialty,
      Location location,
      AttendingHours attendingHours,
      Float rating,
      Integer ratingCount) {
    super(id, email, password, firstName, lastName, image);
    this.healthInsurances = healthInsurances;
    this.specialty = specialty;
    this.location = location;
    this.attendingHours = attendingHours;
    this.rating = rating;
    this.ratingCount = ratingCount;
  }

  // Getters
  public List<HealthInsurance> getHealthInsurances() {
    return healthInsurances;
  }

  public Specialty getSpecialty() {
    return specialty;
  }

  public Location getLocation() {
    return location;
  }

  public AttendingHours getAttendingHours() {
    return attendingHours;
  }

  public Float getRating() {
    return rating;
  }

  public Integer getRatingCount() {
    return ratingCount;
  }

  @Override
  public String toString() {
    return "Doctor [healthInsurances="
        + healthInsurances
        + ", specialty="
        + specialty
        + ", location="
        + location
        + ", attendingHours="
        + attendingHours
        + " "
        + super.toString()
        + "]";
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Doctor)) return false;
    Doctor other = (Doctor) obj;
    return super.equals(other)
        && healthInsurances.equals(other.healthInsurances)
        && specialty.equals(other.specialty)
        && location.equals(other.location)
        && attendingHours.equals(other.attendingHours);
  }
}
