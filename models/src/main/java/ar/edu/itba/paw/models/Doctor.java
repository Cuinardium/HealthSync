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
      Float rating,
      Integer ratingCount) {
    super(id, email, password, firstName, lastName, image);
    this.healthInsurances = healthInsurances;
    this.specialty = specialty;
    this.city = city;
    this.address = address;
    this.attendingHours = attendingHours;
    this.rating = rating;
    this.ratingCount = ratingCount;
  }

  public Doctor(Builder builder) {
    super(
        builder.id,
        builder.email,
        builder.password,
        builder.firstName,
        builder.lastName,
        builder.image);
    this.healthInsurances = builder.healthInsurances;
    this.specialty = builder.specialty;
    this.city = builder.city;
    this.address = builder.address;
    this.attendingHours = builder.attendingHours;
    this.rating = builder.rating;
    this.ratingCount = builder.ratingCount;
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
    this.attendingHours.retainAll(attendingHours);
    this.attendingHours.addAll(attendingHours);
  }

  public void setRating(Float rating) {
    this.rating = rating;
  }

  public void setRatingCount(Integer ratingCount) {
    this.ratingCount = ratingCount;
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

  public static class Builder {
    // required
    private String email, password, firstName, lastName;
    private Set<HealthInsurance> healthInsurances;
    private Specialty specialty;
    private City city;
    private String address;
    private Set<AttendingHours> attendingHours;

    // default
    private Long id = null;
    private Image image = null;

    // TODO: set defaults
    private Float rating;
    private Integer ratingCount;

    public Builder(
        String email,
        String password,
        String firstName,
        String lastName,
        Set<HealthInsurance> healthInsurances,
        Specialty specialty,
        City city,
        String address,
        Set<AttendingHours> attendingHours) {
      this.email = email;
      this.password = password;
      this.firstName = firstName;
      this.lastName = lastName;
      this.healthInsurances = healthInsurances;
      this.specialty = specialty;
      this.city = city;
      this.address = address;
      this.attendingHours = attendingHours;
    }

    public Builder id(long id) {
      this.id = id;
      return this;
    }

    public Builder image(Image image) {
      this.image = image;
      return this;
    }

    public Builder rating(float rating) {
      this.rating = rating;
      return this;
    }

    public Builder ratingCount(int ratingCount) {
      this.ratingCount = ratingCount;
      return this;
    }

    public Doctor build() {
      return new Doctor(this);
    }
  }
}
