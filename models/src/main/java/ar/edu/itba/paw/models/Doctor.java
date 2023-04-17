package ar.edu.itba.paw.models;

public class Doctor extends User {

  private final String healthInsurance;
  private final String specialty;
  private final String city;
  private final String address;

  public Doctor(
      long id,
      String email,
      String password,
      String firstName,
      String lastName,
      long pfpId,
      String healthInsurance,
      String specialty,
      String city,
      String address) {
    super(id, email, password, firstName, lastName, true, pfpId);
    this.healthInsurance = healthInsurance;
    this.specialty = specialty;
    this.city = city;
    this.address = address;
  }

  // Getters
  public String getHealthInsurance() {
    return healthInsurance;
  }

  public String getSpecialty() {
    return specialty;
  }

  public String getCity() {
    return city;
  }

  public String getAddress() {
    return address;
  }
}
