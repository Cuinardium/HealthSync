package ar.edu.itba.paw.models;

public class Doctor extends User {

  private final HealthInsurance healthInsurance;
  private final Specialty specialty;
  private final Location location;

  public Doctor(
      long id,
      String email,
      String password,
      String firstName,
      String lastName,
      long pfpId,
      HealthInsurance healthInsurance,
      Specialty specialty,
      Location location) {
    super(id, email, password, firstName, lastName, pfpId);
    this.healthInsurance = healthInsurance;
    this.specialty = specialty;
    this.location = location;
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
}
