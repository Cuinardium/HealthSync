package ar.edu.itba.paw.interfaces.persistence.exceptions;

public class UserNotFoundException extends Exception {
  private static final String MESSAGE = "User not Found";

  public UserNotFoundException() {
    super(MESSAGE);
  }

  public UserNotFoundException(String message) {
    super(message);
  }
}
