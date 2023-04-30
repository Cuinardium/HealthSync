package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.annotations.PasswordsMatch;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@PasswordsMatch
public class UserRegisterForm {

  @Size(min = 1)
  @Pattern(regexp = "[a-zA-Z ]+")
  private String name;

  @Size(min = 1)
  @Pattern(regexp = "[a-zA-Z ]+")
  private String lastname;

  @Size(min = 1)
  @Pattern(regexp = "[a-zA-Z0-9.+-]+@[a-zA-Z0-9.-]+(.com|.com.ar|.edu.ar)")
  private String email;

  @Size(min = 4)
  @Pattern(regexp = "[a-zA-Z0-9]+")
  private String password;

  @Size(min = 4)
  @Pattern(regexp = "[a-zA-Z0-9]+")
  private String confirmPassword;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
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

  public String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
  }
}
