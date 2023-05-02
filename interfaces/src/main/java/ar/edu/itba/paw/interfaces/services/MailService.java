package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Patient;
import java.util.Locale;

public interface MailService {

  // Mail to request an appointment
  void sendAppointmentRequestMail(
      Appointment appointment, Doctor doctor, Patient patient, Locale locale);

  void sendAppointmentReminderMail(
      Appointment appointment, Doctor doctor, Patient patient, Locale locale);

  void sendAppointmentConfirmedMail(
      Appointment appointment, Doctor doctor, Patient patient, Locale locale);

  void sendAppointmentRejectedMail(
      Appointment appointment, Doctor doctor, Patient patient, Locale locale);

  void sendAppointmentCancelledByDoctorMail(
      Appointment appointment, Doctor doctor, Patient patient, Locale locale);

  public void sendAppointmentCancelledByPatientMail(
      Appointment appointment, Doctor doctor, Patient patient, Locale locale);
}
