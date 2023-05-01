package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.annotations.PasswordsMatch;
import ar.edu.itba.paw.webapp.form.UserRegisterForm;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator
    implements ConstraintValidator<PasswordsMatch, UserRegisterForm> {

  @Override
  public boolean isValid(
      UserRegisterForm userRegisterForm, ConstraintValidatorContext constraintValidatorContext) {
    return userRegisterForm.getPassword().equals(userRegisterForm.getConfirmPassword());
  }
}
