package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.annotations.PasswordsMatch;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@PasswordsMatch
public class PasswordForm {
  @Size(min = 4)
  @Pattern(regexp = "[a-zA-Z0-9]+")
  @NotNull
  private String password;

  @Size(min = 4)
  @Pattern(regexp = "[a-zA-Z0-9]+")
  @NotNull
  private String confirmPassword;

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
