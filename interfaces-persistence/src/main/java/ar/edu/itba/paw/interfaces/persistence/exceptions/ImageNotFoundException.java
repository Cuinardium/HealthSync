package ar.edu.itba.paw.interfaces.persistence.exceptions;

public class ImageNotFoundException extends Exception {
  private static final String MESSAGE = "Image not found";

  public ImageNotFoundException() {
    super(MESSAGE);
  }

  public ImageNotFoundException(String message) {
    super(message);
  }
}
