package ar.edu.itba.paw.models;

import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "review")
public class Review {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_review_id_seq")
  @SequenceGenerator(
    sequenceName = "review_review_id_seq",
    name = "review_review_id_seq",
    allocationSize = 1
  )
  @Column(name = "review_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "doctor_id", nullable = false)
  private Doctor doctor;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "patient_id", nullable = false)
  private Patient patient;

  @Column(name = "review_date", nullable = false)
  private LocalDate date;

  @Column(name = "review_description", length = 1000, nullable = false)
  private String description;

  @Column(name = "rating", nullable = false)
  private Short rating;

  protected Review() {
    // Solo para hibernate
  }

  public Review(
      Long id, Doctor doctor, Patient patient, LocalDate date, String description, Short rating) {
    this.id = id;
    this.patient = patient;
    this.date = date;
    this.description = description;
    this.rating = rating;
    this.doctor = doctor;
  }

  public Review(Builder builder) {
    this.id = builder.id;
    this.patient = builder.patient;
    this.date = builder.date;
    this.description = builder.description;
    this.rating = builder.rating;
    this.doctor = builder.doctor;
  }

  // Getters
  public Long getId() {
    return id;
  }

  public Patient getPatient() {
    return patient;
  }

  public LocalDate getDate() {
    return date;
  }

  public String getDescription() {
    return description;
  }

  public Short getRating() {
    return rating;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Doctor getDoctor() {
    return doctor;
  }

  public void setDoctor(Doctor doctor) {
    this.doctor = doctor;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setRating(Short rating) {
    this.rating = rating;
  }

  @Override
  public String toString() {
    return "Review ["
        + "id="
        + id
        + ", patient="
        + patient
        + ", date="
        + date
        + ", description='"
        + description
        + '\''
        + ", rating="
        + rating
        + ']';
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Review)) return false;
    Review other = (Review) obj;
    return Objects.equals(id, other.id);
  }

  public static class Builder {
    private Doctor doctor;
    private Short rating;
    private String description;
    private LocalDate date;
    private Patient patient;
    // default
    private Long id = null;

    public Builder(
        Patient patient, LocalDate date, String description, Short rating, Doctor doctor) {
      this.patient = patient;
      this.date = date;
      this.description = description;
      this.rating = rating;
      this.doctor = doctor;
    }

    public Builder id(long id) {
      this.id = id;
      return this;
    }

    public Review build() {
      return new Review(this);
    }
  }
}
