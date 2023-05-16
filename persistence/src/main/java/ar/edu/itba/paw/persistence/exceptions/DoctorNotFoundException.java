package ar.edu.itba.paw.persistence.exceptions;

public class DoctorNotFoundException extends RuntimeException {
  private static final String MESSAGE = "Doctor not found";

  public DoctorNotFoundException() {
    super(MESSAGE);
  }

  public DoctorNotFoundException(String message) {
    super(message);
  }
}
