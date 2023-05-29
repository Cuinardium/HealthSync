package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "doctor")
@PrimaryKeyJoinColumn(name = "doctor_id", referencedColumnName = "user_id")
public class Doctor extends User {
  @Enumerated(EnumType.ORDINAL)
  @ElementCollection(targetClass = HealthInsurance.class)
  @JoinTable(
          name = "health_insurance_accepted_by_doctor",
          joinColumns = @JoinColumn(name = "doctor_id"))
  @Column(name = "health_insurance_code", nullable = false)
  private List<HealthInsurance> healthInsurances;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "specialty_code", nullable = false)
  private Specialty specialty;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinTable(name = "location_for_doctor",
          joinColumns =
                  { @JoinColumn(name = "doctor_id") },
          inverseJoinColumns =
                  { @JoinColumn(name = "doctor_location_id") })
  private Location location;

  @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
  private Set<AttendingHours> attendingHours;
//  @Formula("(SELECT AVG(rating) FROM review WHERE doctor_id = doctor_id)")
//  private Float rating;

//  @Formula("(SELECT count(*) FROM review WHERE doctor_id = doctor_id)")
//  private Integer ratingCount;

  /* package */ Doctor() {
    // Solo para hibernate
  }

  public Doctor(
      long id,
      String email,
      String password,
      String firstName,
      String lastName,
      Image image,
      List<HealthInsurance> healthInsurances,
      Specialty specialty,
      Location location,
      Set<AttendingHours> attendingHours,
      Float rating,
      Integer ratingCount) {
    super(id, email, password, firstName, lastName, image);
    this.healthInsurances = healthInsurances;
    this.specialty = specialty;
    this.location = location;
    this.attendingHours = attendingHours;
//    this.rating = rating;
//    this.ratingCount = ratingCount;
  }

  public List<ThirtyMinuteBlock> getAttendingBlocksForDay(DayOfWeek day) {
    return attendingHours.stream().filter(attendingDays -> attendingDays.getId().getDay().equals(day)).map(AttendingHours::getHourBlock).collect(Collectors.toCollection(ArrayList::new));
  }

  public List<ThirtyMinuteBlock> getAttendingBlocksForDate(LocalDate date) {
    return getAttendingBlocksForDay(date.getDayOfWeek());
  }

  // Getters
  public List<HealthInsurance> getHealthInsurances() {
    return healthInsurances;
  }

  public Specialty getSpecialty() {
    return specialty;
  }

  public Location getLocation() {
    return location;
  }

  public Set<AttendingHours> getAttendingHours() {
    return attendingHours;
  }

//  public Float getRating() {
//    return rating;
//  }
//
//  public Integer getRatingCount() {
//    return ratingCount;
//  }

  public void setHealthInsurances(List<HealthInsurance> healthInsurances) {
    this.healthInsurances = healthInsurances;
  }

  public void setSpecialty(Specialty specialty) {
    this.specialty = specialty;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public void setAttendingHours(Set<AttendingHours> attendingHours) {
    this.attendingHours = attendingHours;
  }

  public void setRating(Float rating) {
//    this.rating = rating;
  }

  public void setRatingCount(Integer ratingCount) {
//    this.ratingCount = ratingCount;
  }

  @Override
  public String toString() {
    return "Doctor [healthInsurances="
        + healthInsurances
        + ", specialty="
        + specialty
        + ", location="
        + location
        + ", attendingHours="
        + attendingHours
        + " "
        + super.toString()
        + "]";
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Doctor)) return false;
    Doctor other = (Doctor) obj;
    return super.equals(other)
            && healthInsurances.equals(other.healthInsurances)
            && specialty.equals(other.specialty)
            && location.equals(other.location)
            && attendingHours.equals(other.attendingHours);
  }
}
