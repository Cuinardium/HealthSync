package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "doctor_location")
public class Location {
  @Id
  @Column(name = "doctor_id")
  private Long id;

  @OneToOne
  @MapsId
  @JoinColumn(name = "doctor_id")
  private Doctor doctor;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "city_code", nullable = false)
  private City city;

  @Column(name = "address", nullable = false)
  private String address;

  protected Location() {
    // Solo para hibernate
  }

  public Location(Long id, City city, String address) {
    this.id = id;
    this.city = city;
    this.address = address;
  }

  public Location(City city, String address) {
    this.city = city;
    this.address = address;
  }

  public
  Long getId() {
    return id;
  }

  public City getCity() {
    return city;
  }

  public String getAddress() {
    return address;
  }

  public Doctor getDoctor() {
    return doctor;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setDoctor(Doctor doctor) {
    this.doctor = doctor;
  }

  public void setCity(City city) {
    this.city = city;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return "Location [id=" + id + ", city=" + city + ", address=" + address + "]";
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Location)) return false;
    Location other = (Location) obj;
    // TODO: Check getter methods bc if not other variables are null
    return Objects.equals(id, other.getId())
        && city.equals(other.getCity())
        && address.equals(other.getAddress());
  }
}
