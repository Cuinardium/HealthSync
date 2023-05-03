package ar.edu.itba.paw.webapp.exceptions;

public class ImageNotFoundException extends RuntimeException {
  private static final String MESSAGE_ID = "error.imageNotFound";

  public ImageNotFoundException() {
    super(MESSAGE_ID);
  }

  public ImageNotFoundException(String message) {
    super(message);
  }
}
