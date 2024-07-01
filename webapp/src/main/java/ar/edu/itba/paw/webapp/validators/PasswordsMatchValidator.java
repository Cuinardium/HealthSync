package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.annotations.PasswordsMatch;
import ar.edu.itba.paw.webapp.form.PasswordForm;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, PasswordForm> {

  @Override
  public boolean isValid(
      PasswordForm passwordForm, ConstraintValidatorContext constraintValidatorContext) {
    if (passwordForm.getPassword() == null) {
      return false;
    }
    return passwordForm.getPassword().equals(passwordForm.getConfirmPassword());
  }
}
