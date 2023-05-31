package ar.edu.itba.paw.models;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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

  //  @OneToOne(fetch = FetchType.LAZY)
  //  @JoinColumn(name = "doctor_id" /* TODO: this , nullable = false */)
  @Column(name = "doctor_id")
  private Long /* TODO: Doctor */ doctor;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "patient_id", nullable = false)
  private Patient patient;

  @Column(name = "review_date", nullable = false)
  private LocalDate date;

  @Column(name = "review_description", length = 1000, nullable = false)
  private String description;

  @Column(name = "rating", nullable = false)
  private Short rating;

  /* package */ Review() {
    // Solo para hibernate
  }

  public Review(
      Long id, Long doctor, Patient patient, LocalDate date, String description, Short rating) {
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

  public Long getDoctor() {
    return doctor;
  }

  public void setDoctor(Long doctor) {
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
}
