package ar.edu.itba.paw.interfaces.services.exceptions;

public class TokenNotFoundException extends Exception {
  private static final String MESSAGE = "Token not found";

  public TokenNotFoundException() {
    super(MESSAGE);
  }

  public TokenNotFoundException(String message) {
    super(message);
  }
}
