package ar.edu.itba.paw.models;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
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

  @Column(name = "city", nullable = false)
  private String city;

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
  private Set<Vacation> vacations;

  @Formula("(SELECT AVG(r.rating) FROM Review r WHERE r.doctor_id = doctor_id)")
  private Float rating;

  @Formula("(SELECT count(*) FROM Review r WHERE r.doctor_id = doctor_id)")
  private Integer ratingCount;

  protected Doctor() {
    // Solo para hibernate
  }

  private Doctor(Builder builder) {
    super(
        builder.id,
        builder.email,
        builder.password,
        builder.firstName,
        builder.lastName,
        builder.image,
        builder.locale,
        builder.isVerified);
    this.healthInsurances = builder.healthInsurances;
    this.specialty = builder.specialty;
    this.city = builder.city;
    this.address = builder.address;
    this.attendingHours = builder.attendingHours;
    this.vacations = builder.vacations;
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

  public Set<Vacation> getVacations() {
    return vacations;
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

  public void setVacations(Set<Vacation> vacations) {
    this.vacations.retainAll(vacations);
    this.vacations.addAll(vacations);
  }

  public void addVacation(Vacation vacation) {
    vacations.add(vacation);
  }

  public void removeVacation(Vacation vacation) {
    vacations.remove(vacation);
  }

  public void setRating(Float rating) {
    this.rating = rating;
  }

  public void setRatingCount(Integer ratingCount) {
    this.ratingCount = ratingCount;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
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
        + super.toString()
        + ']';
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();

    result = prime * result + ((address == null) ? 0 : address.hashCode());
    result = prime * result + ((city == null) ? 0 : city.hashCode());
    result = prime * result + ((healthInsurances == null) ? 0 : healthInsurances.hashCode());
    result = prime * result + ((rating == null) ? 0 : rating.hashCode());
    result = prime * result + ((ratingCount == null) ? 0 : ratingCount.hashCode());
    result = prime * result + ((specialty == null) ? 0 : specialty.hashCode());
    result = prime * result + ((attendingHours == null) ? 0 : attendingHours.hashCode());
    result = prime * result + ((vacations == null) ? 0 : vacations.hashCode());

    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;

    if (!super.equals(obj)) return false;

    if (!(obj instanceof Doctor)) return false;

    Doctor other = (Doctor) obj;

    return Objects.equals(address, other.address)
        && Objects.equals(city, other.city)
        && Objects.equals(healthInsurances, other.healthInsurances)
        && Objects.equals(rating, other.rating)
        && Objects.equals(ratingCount, other.ratingCount)
        && Objects.equals(specialty, other.specialty)
        && Objects.equals(attendingHours, other.attendingHours)
        && Objects.equals(vacations, other.vacations);
  }

  public static class Builder {
    // required
    private final String email, password, firstName, lastName;
    private final Set<HealthInsurance> healthInsurances;
    private final Specialty specialty;
    private final String city;
    private final String address;
    private final Set<AttendingHours> attendingHours;
    public Locale locale;

    // default
    private Long id = null;
    private Image image = null;
    private Set<Vacation> vacations = Collections.emptySet();
    private Boolean isVerified = false;

    private Float rating;
    private Integer ratingCount;

    public Builder(
        String email,
        String password,
        String firstName,
        String lastName,
        Set<HealthInsurance> healthInsurances,
        Specialty specialty,
        String city,
        String address,
        Set<AttendingHours> attendingHours,
        Locale locale) {
      this.email = email;
      this.password = password;
      this.firstName = firstName;
      this.lastName = lastName;
      this.healthInsurances = healthInsurances;
      this.specialty = specialty;
      this.city = city;
      this.address = address;
      this.attendingHours = attendingHours;
      this.locale = locale;
    }

    public Builder id(long id) {
      this.id = id;
      return this;
    }

    public Builder vacations(Set<Vacation> vacations) {
      this.vacations = vacations;
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

    public Builder isVerified(boolean isVerified) {
      this.isVerified = isVerified;
      return this;
    }

    public Doctor build() {
      return new Doctor(this);
    }
  }
}
