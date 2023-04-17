package ar.edu.itba.paw.models;

public class HealthInsurance {
  private long id;
  private String name;

  public HealthInsurance(long id, String name) {
    this.id = id;
    this.name = name;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
