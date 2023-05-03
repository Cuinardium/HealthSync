package ar.edu.itba.paw.webapp.exceptions;

public class UserNotFoundException extends RuntimeException {

  private static final String MESSAGE_ID = "error.userNotFound";

  public UserNotFoundException() {
    super(MESSAGE_ID);
  }

  public UserNotFoundException(String message) {
    super(message);
  }
}
