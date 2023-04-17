package ar.edu.itba.paw.models;

public class Location {
  private long id;
  private String city;
  private String address;

  public Location(long id, String city, String address) {
    this.id = id;
    this.city = city;
    this.address = address;
  }

  public long getId() {
    return id;
  }

  public String getCity() {
    return city;
  }

  public String getAddress() {
    return address;
  }
}
