package ar.edu.itba.paw.models;

public class Location {

  private final long id;
  private final City city;
  private final String address;

  public Location(long id, City city, String address) {
    this.id = id;
    this.city = city;
    this.address = address;
  }

  public long getId() {
    return id;
  }

  public City getCity() {
    return city;
  }

  public String getAddress() {
    return address;
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
    return id == other.id && city.equals(other.city) && address.equals(other.address);
  }
}
