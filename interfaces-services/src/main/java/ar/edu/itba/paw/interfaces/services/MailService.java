package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Appointment;

public interface MailService {

  // Mail to request an appointment
  public void sendAppointmentRequestMail(Appointment appointment);

  public void sendAppointmentReminderMail(Appointment appointment);

  public void sendAppointmentCancelledByDoctorMail(Appointment appointment);

  public void sendAppointmentCancelledByPatientMail(Appointment appointment);

  public void sendAppointmentCompletedMail(Appointment appointment);

  public void sendAppointmentIndicationMail(Appointment appointment);
}
