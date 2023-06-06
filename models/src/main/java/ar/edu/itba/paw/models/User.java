package ar.edu.itba.paw.models;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "users") // Si la clase no tiene el mismo nombre que la DB table
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_user_id_seq")
  @SequenceGenerator(
    sequenceName = "users_user_id_seq",
    name = "users_user_id_seq",
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

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "profile_picture_id")
  private Image image;

  protected User() {
    // Solo para hibernate
  }

  public User(
      Long id, String email, String password, String firstName, String lastName, Image image) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.image = image;
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

  public Image getImage() {
    return image;
  }

  public void setImage(Image image) {
    this.image = image;
  }

  @Override
  public String toString() {
    return "User [id="
        + id
        + " "
        + image
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
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof User)) return false;
    User other = (User) obj;
    return Objects.equals(this.id, other.id);
  }
}
