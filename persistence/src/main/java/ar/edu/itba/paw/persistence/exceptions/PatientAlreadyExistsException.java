package ar.edu.itba.paw.persistence.exceptions;

public class PatientAlreadyExistsException extends RuntimeException {
  private static final String MESSAGE = "Patient already exists";

  public PatientAlreadyExistsException() {
    super(MESSAGE);
  }

  public PatientAlreadyExistsException(String message) {
    super(message);
  }
}
