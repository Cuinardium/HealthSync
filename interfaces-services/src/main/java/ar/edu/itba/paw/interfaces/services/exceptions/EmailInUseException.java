package ar.edu.itba.paw.interfaces.services.exceptions;

public class EmailInUseException extends Exception {
  private static final String MESSAGE = "Email in use";

  public EmailInUseException() {
    super(MESSAGE);
  }

  public EmailInUseException(String message) {
    super(message);
  }
}
