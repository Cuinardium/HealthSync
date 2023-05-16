package ar.edu.itba.paw.models;

import java.util.List;

public class Doctor extends User {

  private final List<HealthInsurance> healthInsurances;
  private final Specialty specialty;
  private final Location location;
  private final AttendingHours attendingHours;

  public Doctor(
      long id,
      String email,
      String password,
      String firstName,
      String lastName,
      Long pfpId,
      List<HealthInsurance> healthInsurances,
      Specialty specialty,
      Location location,
      AttendingHours attendingHours) {
    super(id, email, password, firstName, lastName, pfpId);
    this.healthInsurances = healthInsurances;
    this.specialty = specialty;
    this.location = location;
    this.attendingHours = attendingHours;
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
}
