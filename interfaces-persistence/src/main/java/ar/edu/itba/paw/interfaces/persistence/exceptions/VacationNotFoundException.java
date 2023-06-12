package ar.edu.itba.paw.interfaces.persistence.exceptions;

public class VacationNotFoundException extends Exception {
  private static final String MESSAGE = "Vacation not found";

  public VacationNotFoundException() {
    super(MESSAGE);
  }

  public VacationNotFoundException(String message) {
    super(message);
  }
}

