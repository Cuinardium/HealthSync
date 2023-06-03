package ar.edu.itba.paw.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "patient")
@PrimaryKeyJoinColumn(name = "patient_id", referencedColumnName = "user_id")
public class Patient extends User {
  @Enumerated(EnumType.ORDINAL)
  @Column(name = "health_insurance_code", nullable = false)
  private HealthInsurance healthInsurance;

  protected Patient() {
    // Solo para hibernate
  }

  public Patient(
      Long id,
      String email,
      String password,
      String firstName,
      String lastName,
      Image image,
      HealthInsurance healthInsurance) {
    super(id, email, password, firstName, lastName, image);
    this.healthInsurance = healthInsurance;
  }

  public HealthInsurance getHealthInsurance() {
    return healthInsurance;
  }

  public void setHealthInsurance(HealthInsurance healthInsurance) {
    this.healthInsurance = healthInsurance;
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
    return super.equals(obj) && (healthInsurance.equals(other.healthInsurance));
  }
}
