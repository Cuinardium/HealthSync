package ar.edu.itba.paw.webapp.validators;


import ar.edu.itba.paw.webapp.annotations.DateAnnotation;
import ar.edu.itba.paw.webapp.form.AppointmentForm;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateValidator implements ConstraintValidator<DateAnnotation, AppointmentForm> {
    @Override
    public boolean isValid(AppointmentForm appointmentForm, ConstraintValidatorContext constraintValidatorContext) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String date= appointmentForm.getDate();
        date = date.replaceFirst("T", " ");
        if(!date.isEmpty()){
            LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
            return dateTime.isAfter(LocalDateTime.now());
        }
        return false;


    }
}
