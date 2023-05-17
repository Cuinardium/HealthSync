package ar.edu.itba.paw.models;

import java.time.LocalDate;

public class Review {

  private final long id;
  private final Patient patient;
  private final LocalDate date;
  private final String description;
  private final short rating;

  public Review(long id, Patient patient, LocalDate date, String description, short rating) {
    this.id = id;
    this.patient = patient;
    this.date = date;
    this.description = description;
    this.rating = rating;
  }

  // Getters
  public long getId() {
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

  public short getRating() {
    return rating;
  }

  @Override
  public String toString() {
    return "Review [" +
        "id=" + id +
        ", patient=" + patient +
        ", date=" + date +
        ", description='" + description + '\'' +
        ", rating=" + rating +
        ']';
  }
}
