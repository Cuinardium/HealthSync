package ar.edu.itba.paw.webapp.exceptions;

public class ReviewForbiddenException extends RuntimeException {

  private static final String MESSAGE_ID = "error.reviewForbidden";

  public ReviewForbiddenException() {
    super(MESSAGE_ID);
  }

  public ReviewForbiddenException(String message) {
    super(message);
  }
}
