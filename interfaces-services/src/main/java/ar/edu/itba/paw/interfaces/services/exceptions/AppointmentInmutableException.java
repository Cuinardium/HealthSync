package ar.edu.itba.paw.interfaces.services.exceptions;

public class AppointmentInmutableException extends Exception {
  private static final String MESSAGE = "Appointment is inmutable";

  public AppointmentInmutableException() {
    super(MESSAGE);
  }

  public AppointmentInmutableException(String message) {
    super(message);
  }
}
