package ar.edu.itba.paw.interfaces.services.exceptions;

public class AlreadyReviewedException extends Exception {
  private static final String MESSAGE = "Patient already reviewed this doctor";

  public AlreadyReviewedException() {
    super(MESSAGE);
  }

  public AlreadyReviewedException(String message) {
    super(message);
  }
}
