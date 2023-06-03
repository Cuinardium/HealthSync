package ar.edu.itba.paw.interfaces.persistence.exceptions;

public class AppointmentAlreadyExistsException extends Exception {
  private static final String MESSAGE = "Appointment already exists";

  public AppointmentAlreadyExistsException() {
    super(MESSAGE);
  }

  public AppointmentAlreadyExistsException(String message) {
    super(message);
  }
}
