package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserRegisterForm extends PasswordForm {
  @NotNull(message = "NotNull.userForm.name")
  @Size(min = 1, max = 50, message = "Size.userForm.name")
  @Pattern(regexp = "[a-zA-Z ñÑáÁéÉíÍóÓúÚ]+", message = "Pattern.userForm.name")
  private String name;

  @NotNull(message = "NotNull.userForm.lastname")
  @Size(min = 1, max = 50, message = "Size.userForm.lastname")
  @Pattern(regexp = "[a-zA-Z ñÑáÁéÉíÍóÓúÚ]+", message = "Pattern.userForm.lastname")
  private String lastname;

  @NotNull(message = "NotNull.userForm.email")
  @Size(min = 1, max = 50, message = "Size.userForm.email")
  @Pattern(
      regexp = "[a-zA-Z0-9.+-ñÑ]+@[a-zA-Z0-9.-]+(.com|.com.ar|.edu.ar)",
      message = "Pattern.userForm.email")
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
