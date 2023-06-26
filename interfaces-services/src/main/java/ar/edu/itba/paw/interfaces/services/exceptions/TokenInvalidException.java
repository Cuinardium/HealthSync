package ar.edu.itba.paw.interfaces.services.exceptions;

public class TokenInvalidException extends Exception {
  private static final String MESSAGE = "Token invalid";

  public TokenInvalidException() {
    super(MESSAGE);
  }

  public TokenInvalidException(String message) {
    super(message);
  }
}
