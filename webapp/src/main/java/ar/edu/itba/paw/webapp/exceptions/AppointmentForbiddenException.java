package ar.edu.itba.paw.webapp.exceptions;

public class AppointmentForbiddenException extends RuntimeException {

  private static final String MESSAGE_ID = "error.appointmentForbidden";

  public AppointmentForbiddenException() {
    super(MESSAGE_ID);
  }

  public AppointmentForbiddenException(String message) {
    super(message);
  }
}
