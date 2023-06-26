package ar.edu.itba.paw.models;

import java.util.Locale;
import java.util.Objects;

import javax.persistence.*;

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
  @JoinColumn(name = "profile_picture_id", referencedColumnName = "profile_picture_id")
  private Image image;

  @Column(name = "locale", length = 10, nullable = false)
  private Locale locale;

  @Column(name = "is_verified", nullable = false)
  private Boolean isVerified;

  protected User() {
    // Solo para hibernate
  }

  public User(
      Long id,
      String email,
      String password,
      String firstName,
      String lastName,
      Image image,
      Locale locale,
      Boolean isVerified) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.image = image;
    this.locale = locale;
    this.isVerified = isVerified;
  }

  // Getters and setters

  public User(Builder builder) {
    this.id = builder.id;
    this.email = builder.email;
    this.password = builder.password;
    this.firstName = builder.firstName;
    this.lastName = builder.lastName;
    this.image = builder.image;
    this.locale = builder.locale;
    this.isVerified = builder.isVerified;
  }

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

  public Boolean getIsVerified() {
    return isVerified;
  }

  public void setIsVerified(Boolean isVerified) {
    this.isVerified = isVerified;
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
        + ", locale="
        + locale
        + ", isVerified="
        + isVerified
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

  public static class Builder {
    // required
    private String email, password, firstName, lastName;
    private Locale locale;
    // defaults
    private Long id = null;
    private Image image = null;
    private Boolean isVerified = false;

    public Builder(
        String email, String password, String firstName, String lastName, Locale locale) {
      this.email = email;
      this.password = password;
      this.firstName = firstName;
      this.lastName = lastName;
      this.locale = locale;
    }

    public Builder id(long id) {
      this.id = id;
      return this;
    }

    public Builder image(Image image) {
      this.image = image;
      return this;
    }

    public Builder isVerified(boolean isVerified) {
      this.isVerified = isVerified;
      return this;
    }

    public User build() {
      return new User(this);
    }
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

}
