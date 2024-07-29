package ar.edu.itba.paw.interfaces.services.exceptions;

public class InvalidRangeException extends Exception {

    private static final String MESSAGE = "Invalid date and time range";

    public InvalidRangeException() {
        super(MESSAGE);
    }

    public InvalidRangeException(String message) {
        super(message);
    }
}
