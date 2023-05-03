package ar.edu.itba.paw.webapp.exceptions;

public class AppointmentNotFoundException extends RuntimeException {

  private static final String MESSAGE = "Appointment not found!";

  public AppointmentNotFoundException() {
    super(MESSAGE);
  }

  public AppointmentNotFoundException(String message) {
    super(message);
  }
}
