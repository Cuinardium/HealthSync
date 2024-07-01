package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserRegisterForm extends PasswordForm {
  @NotNull
  @Size(min = 1)
  @Pattern(regexp = "[a-zA-Z ñÑáÁéÉíÍóÓúÚ]+")
  private String name;

  @NotNull
  @Size(min = 1)
  @Pattern(regexp = "[a-zA-Z ñÑáÁéÉíÍóÓúÚ]+")
  private String lastname;

  @NotNull
  @Size(min = 1)
  @Pattern(regexp = "[a-zA-Z0-9.+-ñÑ]+@[a-zA-Z0-9.-]+(.com|.com.ar|.edu.ar)")
  private String email;

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
}
