package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Appointment;
import java.util.Locale;

public interface MailService {

  // Mail to request an appointment
  public void sendAppointmentRequestMail(Appointment appointment, Locale locale);

  public void sendAppointmentReminderMail(Appointment appointment, Locale locale);

  public void sendAppointmentCancelledByDoctorMail(Appointment appointment, Locale locale);

  public void sendAppointmentCancelledByPatientMail(Appointment appointment, Locale locale);

  public void sendAppointmentCompletedMail(Appointment appointment, Locale locale);
}
