package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Appointment;
import java.util.Locale;

public interface MailService {

  // Mail to request an appointment
  void sendAppointmentRequestMail(Appointment appointment, Locale locale);

  void sendAppointmentReminderMail(Appointment appointment, Locale locale);

  void sendAppointmentCancelledByDoctorMail(Appointment appointment, String cancelDescription,Locale locale);

  void sendAppointmentCancelledByPatientMail(Appointment appointment, String cancelDescription,Locale locale);
}
