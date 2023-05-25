package ar.edu.itba.paw.interfaces.services.exceptions;

public class PatientNotFoundException extends Exception {
  private static final String MESSAGE = "Patient not found";

  public PatientNotFoundException() {
    super(MESSAGE);
  }

  public PatientNotFoundException(String message) {
    super(message);
  }
}
