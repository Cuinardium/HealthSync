package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ChangePasswordForm extends PasswordForm {
  @Size(min = 4)
  @Pattern(regexp = "[a-zA-Z0-9]+")
  private String oldPassword;

  public String getOldPassword() {
    return oldPassword;
  }

  public void setOldPassword(String oldPassword) {
    this.oldPassword = oldPassword;
  }
}
