package ar.edu.itba.paw.models;

public class User {
  private final long id;
  private final Long profilePictureId;

  private final String email;
  private String password;
  private final String firstName;
  private final String lastName;

  public User(
      long id,
      String email,
      String password,
      String firstName,
      String lastName,
      Long profilePictureId) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
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

  public Long getProfilePictureId() {
    return profilePictureId;
  }

  @Override
  public String toString() {
    return "User [id="
        + id
        + ", profilePictureId="
        + profilePictureId
        + ", email="
        + email
        + ", password=<redacted>"
        + ", firstName="
        + firstName
        + ", lastName="
        + lastName
        + "]";
  }
}
