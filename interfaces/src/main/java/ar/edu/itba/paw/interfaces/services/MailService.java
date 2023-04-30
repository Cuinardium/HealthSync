package ar.edu.itba.paw.interfaces.services;

import java.util.Locale;

public interface MailService {

  // Mail to request an appointment
  void sendAppointmentRequestMail(
      String clientEmail,
      String doctorEmail,
      String clientName,
      String healthCare,
      String date,
      String description,
      Locale locale);

  void sendAppointmentReminderMail(
          String clientEmail,
          String doctorEmail,
          String address,
          String city,
          String clientName,
          String healthCare,
          String date,
          String description,
          Locale locale
  );
}

