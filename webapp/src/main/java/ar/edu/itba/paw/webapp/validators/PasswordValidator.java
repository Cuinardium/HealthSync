package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.annotations.ConfirmPassword;
import ar.edu.itba.paw.webapp.form.MedicRegisterForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator  implements ConstraintValidator<ConfirmPassword, MedicRegisterForm> {

    @Override
    public boolean isValid(MedicRegisterForm medicRegisterForm, ConstraintValidatorContext constraintValidatorContext) {
        return medicRegisterForm.getPassword().equals(medicRegisterForm.getConfirmPassword());
    }
}
