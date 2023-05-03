package ar.edu.itba.paw.webapp.exceptions;

public class AppointmentForbiddenException extends RuntimeException {

  private static final String MESSAGE = "You are not authorized to see this appointment!";

  public AppointmentForbiddenException() {
    super(MESSAGE);
  }

  public AppointmentForbiddenException(String message) {
    super(message);
  }
}
