package ar.edu.itba.paw.webapp.annotations;

import ar.edu.itba.paw.webapp.validators.ValidAppointmentQueryRangeValidator;
import ar.edu.itba.paw.webapp.validators.ValidDoctorQueryRangeValidator;
import ar.edu.itba.paw.webapp.validators.ValidOccupiedHoursQueryRangeValidator;
import ar.edu.itba.paw.webapp.validators.ValidVacationFormRangeValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
    validatedBy = {
      ValidVacationFormRangeValidator.class,
      ValidDoctorQueryRangeValidator.class,
      ValidOccupiedHoursQueryRangeValidator.class,
      ValidAppointmentQueryRangeValidator.class,
    })
public @interface ValidRange {

  String message() default "Invalid date and time range";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
