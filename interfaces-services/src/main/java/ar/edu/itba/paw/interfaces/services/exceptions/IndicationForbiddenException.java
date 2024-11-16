package ar.edu.itba.paw.interfaces.services.exceptions;

public class IndicationForbiddenException extends Exception {
    private static final String MESSAGE = "Patient not allowed to indicate this doctor";

    public IndicationForbiddenException() {
        super(MESSAGE);
    }

    public IndicationForbiddenException(String message) {
        super(message);
    }
}
