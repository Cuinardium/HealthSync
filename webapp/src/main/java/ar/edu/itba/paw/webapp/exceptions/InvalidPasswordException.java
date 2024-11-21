package ar.edu.itba.paw.webapp.exceptions;

public class InvalidPasswordException extends Exception {
  public static final String MESSAGE = "Invalid password";

  public InvalidPasswordException() {
    super(MESSAGE);
  }

  public InvalidPasswordException(final String message) {
    super(message);
  }
}
