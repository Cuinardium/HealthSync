package ar.edu.itba.paw.models;

import java.util.Locale;
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
      HealthInsurance healthInsurance,
      Locale locale) {
    super(id, email, password, firstName, lastName, image, locale);
    this.healthInsurance = healthInsurance;
  }

  public Patient(Builder builder) {
    super(
        builder.id,
        builder.email,
        builder.password,
        builder.firstName,
        builder.lastName,
        builder.image,
        builder.locale);
    this.healthInsurance = builder.healthInsurance;
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

  public static class Builder {
    // required
    private String email, password, firstName, lastName;
    private HealthInsurance healthInsurance;
    public Locale locale;

    // default
    private Long id = null;
    private Image image = null;

    public Builder(
        String email,
        String password,
        String firstName,
        String lastName,
        HealthInsurance healthInsurance,
        Locale locale) {
      this.email = email;
      this.password = password;
      this.firstName = firstName;
      this.lastName = lastName;
      this.healthInsurance = healthInsurance;
      this.locale = locale;
    }

    public Builder id(long id) {
      this.id = id;
      return this;
    }

    public Builder image(Image image) {
      this.image = image;
      return this;
    }

    public Patient build() {
      return new Patient(this);
    }
  }
}
