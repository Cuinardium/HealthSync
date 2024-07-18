package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.annotations.PasswordsMatch;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@PasswordsMatch(message = "PasswordsMatch.passwordForm")
public class PasswordForm {

  @NotNull(message = "NotNull.passwordForm.password")
  @Size(min = 4, max = 50, message = "Size.passwordForm.password")
  @Pattern(regexp = "[a-zA-Z0-9]+", message = "Pattern.passwordForm.password")
  private String password;

  @NotNull(message = "NotNull.passwordForm.password")
  @Size(min = 4, max = 50, message = "Size.passwordForm.password")
  @Pattern(regexp = "[a-zA-Z0-9]+", message = "Pattern.passwordForm.password")
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

  @Override
  public String toString() {
    return "PasswordForm";
  }
}
