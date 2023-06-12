package ar.edu.itba.paw.interfaces.persistence.exceptions;

public class VacationCollisionException extends Exception {
    private static final String MESSAGE = "Vacation collision";

    public VacationCollisionException() {
        super(MESSAGE);
    }

    public VacationCollisionException(String message) {
        super(message);
    }
}
