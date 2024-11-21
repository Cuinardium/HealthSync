package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ChangePasswordForm extends PasswordForm {
  @NotNull(message = "NotNull.passwordForm.password")
  @Size(min = 4, max = 50, message = "Size.passwordForm.password")
  @Pattern(regexp = "[a-zA-Z0-9]+", message = "Pattern.passwordForm.password")

  private String oldPassword;

  public String getOldPassword() {
    return oldPassword;
  }

  public void setOldPassword(String oldPassword) {
    this.oldPassword = oldPassword;
  }
}
