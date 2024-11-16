package ar.edu.itba.paw.interfaces.services.exceptions;

public class NotCompletedException extends Exception {
  private static final String MESSAGE = "The appointment is not completed";

  public NotCompletedException() {
    super(MESSAGE);
  }

  public NotCompletedException(String message) {
    super(message);
  }
}
