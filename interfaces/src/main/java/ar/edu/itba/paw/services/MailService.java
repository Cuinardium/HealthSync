package ar.edu.itba.paw.services;

public interface MailService {

  // Mail to request an appointment
  void sendAppointmentRequestMail(
      String clientEmail,
      String doctorEmail,
      String clientName,
      String healthCare,
      String date,
      String description);
}
