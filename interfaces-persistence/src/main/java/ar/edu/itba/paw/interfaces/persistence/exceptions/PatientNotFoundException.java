package ar.edu.itba.paw.interfaces.persistence.exceptions;

public class PatientNotFoundException extends RuntimeException {
  private static final String MESSAGE = "Patient not found";

  public PatientNotFoundException() {
    super(MESSAGE);
  }

  public PatientNotFoundException(String message) {
    super(message);
  }
}
