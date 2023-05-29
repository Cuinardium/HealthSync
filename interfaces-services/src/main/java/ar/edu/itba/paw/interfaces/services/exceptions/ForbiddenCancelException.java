package ar.edu.itba.paw.interfaces.services.exceptions;

public class ForbiddenCancelException extends Exception {
  private static final String MESSAGE = "Requester not allowed to cancel appointment";

  public ForbiddenCancelException() {
    super(MESSAGE);
  }

  public ForbiddenCancelException(String message) {
    super(message);
  }
}
