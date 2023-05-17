package ar.edu.itba.paw.interfaces.services.exceptions;

public class UserNotFoundException extends Exception {
  private static final String MESSAGE = "User not found";

  public UserNotFoundException() {
    super(MESSAGE);
  }

  public UserNotFoundException(String message) {
    super(message);
  }
}
