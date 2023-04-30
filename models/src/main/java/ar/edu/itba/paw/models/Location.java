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
}
