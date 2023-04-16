package ar.edu.itba.paw.models;

public class User {
  private final long id;
  private final long profilePictureId;

  private final String email;
  private String password;
  private String firstName;
  private String lastName;
  private Boolean isDoctor;

  public User(
      long id,
      String email,
      String password,
      String firstName,
      String lastName,
      Boolean isDoctor,
      long profilePictureId) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.isDoctor = isDoctor;
    this.profilePictureId = profilePictureId;
  }

  // Getters and setters
  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public long getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public Boolean isDoctor() {
    return isDoctor;
  }

  public long getProfilePictureId() {
    return profilePictureId;
  }
}
