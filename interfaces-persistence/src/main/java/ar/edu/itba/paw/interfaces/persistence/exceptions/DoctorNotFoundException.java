package ar.edu.itba.paw.interfaces.persistence.exceptions;

public class DoctorNotFoundException extends Exception {
  private static final String MESSAGE = "Doctor not found";

  public DoctorNotFoundException() {
    super(MESSAGE);
  }

  public DoctorNotFoundException(String message) {
    super(message);
  }
}
