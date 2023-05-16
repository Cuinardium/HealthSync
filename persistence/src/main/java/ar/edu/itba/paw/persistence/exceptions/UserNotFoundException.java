package ar.edu.itba.paw.persistence.exceptions;

public class UserNotFoundException extends RuntimeException {
  private static final String MESSAGE = "User not Found";

  public UserNotFoundException() {
    super(MESSAGE);
  }

  public UserNotFoundException(String message) {
    super(message);
  }
}
