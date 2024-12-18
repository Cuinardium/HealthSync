package ar.edu.itba.paw.interfaces.persistence.exceptions;

public class DoctorAlreadyExistsException extends Exception {
  private static final String MESSAGE = "Doctor already exists";

  public DoctorAlreadyExistsException() {
    super(MESSAGE);
  }

  public DoctorAlreadyExistsException(String message) {
    super(message);
  }
}
