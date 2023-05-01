package ar.edu.itba.paw.models;

public class Patient extends User {

  private final HealthInsurance healthInsurance;

  public Patient(
      long id,
      String email,
      String password,
      String firstName,
      String lastName,
      long profilePictureId,
      HealthInsurance healthInsurance) {
    super(id, email, password, firstName, lastName, profilePictureId);
    this.healthInsurance = healthInsurance;
  }

  public HealthInsurance getHealthInsurance() {
    return healthInsurance;
  }
}
