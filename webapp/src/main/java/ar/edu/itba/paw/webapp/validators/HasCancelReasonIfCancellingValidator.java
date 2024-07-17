package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.annotations.HasCancelReasonIfCancelling;
import ar.edu.itba.paw.webapp.form.DoctorVacationForm;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HasCancelReasonIfCancellingValidator
    implements ConstraintValidator<HasCancelReasonIfCancelling, DoctorVacationForm> {

  private static final Integer MAX_REASON_LENGTH = 1000;
  private static final Integer MIN_REASON_LENGTH = 1;

  @Override
  public boolean isValid(
      DoctorVacationForm doctorVacationForm,
      ConstraintValidatorContext constraintValidatorContext) {
    if (!doctorVacationForm.cancelAppointments()) {
      return true;
    }

    if (doctorVacationForm.getCancelReason() == null) {
      return false;
    }

    return doctorVacationForm.getCancelReason().length() >= MIN_REASON_LENGTH
        && doctorVacationForm.getCancelReason().length() <= MAX_REASON_LENGTH;
  }
}
