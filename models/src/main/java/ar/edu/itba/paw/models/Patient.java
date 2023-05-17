package ar.edu.itba.paw.models;

public class Patient extends User {

  private final HealthInsurance healthInsurance;

  public Patient(
      long id,
      String email,
      String password,
      String firstName,
      String lastName,
      Long profilePictureId,
      HealthInsurance healthInsurance) {
    super(id, email, password, firstName, lastName, profilePictureId);
    this.healthInsurance = healthInsurance;
  }

  public HealthInsurance getHealthInsurance() {
    return healthInsurance;
  }

  @Override
  public String toString() {
    return "Patient [healthInsurance=" + healthInsurance + " " + super.toString() + "]";
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Patient)) return false;
    Patient other = (Patient) obj;
    return super.equals(obj) && healthInsurance == other.healthInsurance;
  }
}
