package ar.edu.itba.paw.models;

public class Doctor extends User {

  private final HealthInsurance healthInsurance;
  private final Specialty specialty;
  private final Location location;
  private final AttendingHours attendingHours;

  public Doctor(
      long id,
      String email,
      String password,
      String firstName,
      String lastName,
      long pfpId,
      HealthInsurance healthInsurance,
      Specialty specialty,
      Location location,
      AttendingHours attendingHours) {
    super(id, email, password, firstName, lastName, pfpId);
    this.healthInsurance = healthInsurance;
    this.specialty = specialty;
    this.location = location;
    this.attendingHours = attendingHours;
  }

  // Getters
  public HealthInsurance getHealthInsurance() {
    return healthInsurance;
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
    return "Doctor [healthInsurance="
        + healthInsurance
        + ", specialty="
        + specialty
        + ", location="
        + location
        + ", attendingHours="
        + attendingHours
        + super.toString()
        + "]";
  }
}
