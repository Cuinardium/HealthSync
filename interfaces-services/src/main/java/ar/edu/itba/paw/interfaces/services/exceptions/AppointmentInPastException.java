package ar.edu.itba.paw.interfaces.services.exceptions;

public class AppointmentInPastException extends Exception {
  private static final String MESSAGE = "Appointment date and time is in the past";

  public AppointmentInPastException() {
    super(MESSAGE);
  }

  public AppointmentInPastException(String message) {
    super(message);
  }
}
