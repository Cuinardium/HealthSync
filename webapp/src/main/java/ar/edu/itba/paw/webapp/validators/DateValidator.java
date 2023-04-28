package ar.edu.itba.paw.webapp.validators;


import ar.edu.itba.paw.webapp.annotations.Date;
import ar.edu.itba.paw.webapp.form.AppointmentForm;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateValidator implements ConstraintValidator<Date, AppointmentForm> {
    @Override
    public boolean isValid(AppointmentForm appointmentForm, ConstraintValidatorContext constraintValidatorContext) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(appointmentForm.getDate(), formatter);
        return dateTime.isAfter(LocalDateTime.now());

    }
}
