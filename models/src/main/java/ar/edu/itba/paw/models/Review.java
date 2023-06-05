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

  @ManyToOne(fetch = FetchType.LAZY)
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Review review = (Review) o;
    return Objects.equals(id, review.id) && Objects.equals(doctor, review.doctor) && Objects.equals(patient, review.patient) && Objects.equals(date, review.date) && Objects.equals(description, review.description) && Objects.equals(rating, review.rating);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, doctor, patient, date, description, rating);
  }
}
