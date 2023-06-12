package ar.edu.itba.paw.interfaces.services.exceptions;

public class SetIndicationsForbiddenException extends Exception {
  public static final String MESSAGE = "Set Indications forbidden";

  public SetIndicationsForbiddenException() {
    super(MESSAGE);
  }

  public SetIndicationsForbiddenException(String message) {
    super(message);
  }
}
