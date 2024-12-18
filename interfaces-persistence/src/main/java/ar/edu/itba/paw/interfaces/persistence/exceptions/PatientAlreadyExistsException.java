package ar.edu.itba.paw.interfaces.persistence.exceptions;

public class PatientAlreadyExistsException extends Exception {
  private static final String MESSAGE = "Patient already exists";

  public PatientAlreadyExistsException() {
    super(MESSAGE);
  }

  public PatientAlreadyExistsException(String message) {
    super(message);
  }
}
