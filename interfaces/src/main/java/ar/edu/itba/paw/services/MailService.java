package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;
import java.util.List;

public interface MailService {

  // Mail to request an appointment
  void sendAppointmentRequestMail(
      User client, User doctor, List<String> possibleDates, String reason);
}
