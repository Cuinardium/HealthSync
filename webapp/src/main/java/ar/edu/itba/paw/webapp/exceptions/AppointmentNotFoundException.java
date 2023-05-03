package ar.edu.itba.paw.webapp.exceptions;

public class AppointmentNotFoundException extends RuntimeException {

  private static final String MESSAGE_ID = "error.appointmentNotFound";

  public AppointmentNotFoundException() {
    super(MESSAGE_ID);
  }

  public AppointmentNotFoundException(String message) {
    super(message);
  }
}
