package ar.edu.itba.paw.models;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.*;
import org.hibernate.annotations.Formula;

@Entity
@Table(name = "doctor")
@PrimaryKeyJoinColumn(name = "doctor_id", referencedColumnName = "user_id")
public class Doctor extends User {
  @Enumerated(EnumType.ORDINAL)
  @ElementCollection(fetch = FetchType.LAZY, targetClass = HealthInsurance.class)
  @JoinTable(
    name = "health_insurance_accepted_by_doctor",
    joinColumns = @JoinColumn(name = "doctor_id")
  )
  @Column(name = "health_insurance_code", nullable = false)
  private Set<HealthInsurance> healthInsurances;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "specialty_code", nullable = false)
  private Specialty specialty;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "city_code", nullable = false)
  private City city;

  @Column(name = "address", nullable = false)
  private String address;

  @OneToMany(
    mappedBy = "doctor",
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private Set<AttendingHours> attendingHours;

  @OneToMany(
    mappedBy = "doctor",
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<Review> reviews;

  @Formula("(SELECT AVG(r.rating) FROM Review r WHERE r.doctor_id = doctor_id)")
  private Float rating;

  @Formula("(SELECT count(*) FROM Review r WHERE r.doctor_id = doctor_id)")
  private Integer ratingCount;

  protected Doctor() {
    // Solo para hibernate
  }

  public Doctor(
      Long id,
      String email,
      String password,
      String firstName,
      String lastName,
      Image image,
      Set<HealthInsurance> healthInsurances,
      Specialty specialty,
      City city,
      String address,
      Set<AttendingHours> attendingHours,
      List<Review> reviews,
      Float rating,
      Integer ratingCount) {
    super(id, email, password, firstName, lastName, image);
    this.healthInsurances = healthInsurances;
    this.specialty = specialty;
    this.city = city;
    this.address = address;
    this.attendingHours = attendingHours;
    this.reviews = reviews;
    this.rating = rating;
    this.ratingCount = ratingCount;
  }

  public List<ThirtyMinuteBlock> getAttendingBlocksForDay(DayOfWeek day) {
    return attendingHours
        .stream()
        .filter(attendingDays -> attendingDays.getId().getDay().equals(day))
        .map(AttendingHours::getHourBlock)
        .collect(Collectors.toCollection(ArrayList::new));
  }

  public List<ThirtyMinuteBlock> getAttendingBlocksForDate(LocalDate date) {
    return getAttendingBlocksForDay(date.getDayOfWeek());
  }

  // Getters
  public Set<HealthInsurance> getHealthInsurances() {
    return healthInsurances;
  }

  public Specialty getSpecialty() {
    return specialty;
  }

  public Set<AttendingHours> getAttendingHours() {
    return attendingHours;
  }

  public Float getRating() {
    return rating;
  }

  public Integer getRatingCount() {
    return ratingCount;
  }

  public void setHealthInsurances(Set<HealthInsurance> healthInsurances) {
    this.healthInsurances = healthInsurances;
  }

  public void setSpecialty(Specialty specialty) {
    this.specialty = specialty;
  }

  public void setAttendingHours(Set<AttendingHours> attendingHours) {
    this.attendingHours = attendingHours;
  }

  public void setRating(Float rating) {
    this.rating = rating;
  }

  public void setRatingCount(Integer ratingCount) {
    this.ratingCount = ratingCount;
  }

  public List<Review> getReviews() {
    return reviews;
  }

  public void setReviews(List<Review> reviews) {
    this.reviews.clear();
    this.reviews.addAll(reviews);
  }

  public City getCity() {
    return city;
  }

  public void setCity(City city) {
    this.city = city;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return "Doctor["
        + "healthInsurances="
        + healthInsurances
        + ", specialty="
        + specialty
        + ", city="
        + city
        + ", address='"
        + address
        + '\''
        + ", rating="
        + rating
        + ", ratingCount="
        + ratingCount
        + ']';
  }
}
