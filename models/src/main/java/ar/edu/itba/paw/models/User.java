package ar.edu.itba.paw.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "users") // Si la clase no tiene el mismo nombre que la DB table
public class User {
  // TODO: agregar restricciones (solo las usa si levanto la DB con Hibernate)
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_userid_seq")
  @SequenceGenerator(
    sequenceName = "users_userid_seq",
    name = "users_userid_seq",
    allocationSize = 1
  )
  @Column(name = "user_id")
  private Long id; // uso Long para q si es nulo inserte

  @Column(length = 100, unique = true, nullable = false)
  private String email;

  @Column(length = 100, nullable = false)
  private String password;

  @Column(name = "first_name", length = 100, nullable = false)
  private String firstName;

  @Column(name = "last_name", length = 100, nullable = false)
  private String lastName;

  @Column(name = "profile_picture_id")
  private Long profilePictureId;

  /* package */ User() {
    // Solo para hibernate
  }

  public User(
      Long id,
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

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Long getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Long getProfilePictureId() {
    return profilePictureId;
  }

  public void setProfilePictureId(Long profilePictureId) {
    this.profilePictureId = profilePictureId;
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

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof User)) return false;
    User other = (User) obj;
    // el || es para rescatarme del nullptrexcep
    return (id == other.id || id.equals(other.id))
        && (profilePictureId == other.profilePictureId
            || profilePictureId.equals(other.profilePictureId))
        && email.equals(other.email)
        && firstName.equals(other.firstName)
        && lastName.equals(other.lastName);
  }
}
