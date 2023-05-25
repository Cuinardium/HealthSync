package ar.edu.itba.paw.interfaces.services.exceptions;

public class AppointmentNotFoundException extends Exception {
  private static final String MESSAGE = "Appointment not found";

  public AppointmentNotFoundException() {
    super(MESSAGE);
  }

  public AppointmentNotFoundException(String message) {
    super(message);
  }
}
