package ar.edu.itba.paw.interfaces.services.exceptions;

public class DoctorNotAvailableException extends Exception {

  private static final String MESSAGE = "Doctor is not available at the given time";

  public DoctorNotAvailableException() {
    super(MESSAGE);
  }

  public DoctorNotAvailableException(String message) {
    super(message);
  }
}
