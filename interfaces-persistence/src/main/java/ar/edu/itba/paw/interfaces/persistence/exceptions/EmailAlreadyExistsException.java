package ar.edu.itba.paw.interfaces.persistence.exceptions;

public class EmailAlreadyExistsException extends Exception {
  private static final String MESSAGE = "Email already exists";

  public EmailAlreadyExistsException() {
    super(MESSAGE);
  }

  public EmailAlreadyExistsException(String message) {
    super(message);
  }
}
