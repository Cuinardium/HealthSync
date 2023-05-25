package ar.edu.itba.paw.interfaces.services.exceptions;

public class CancelForbiddenException extends Exception {
    private static final String MESSAGE = "Requester not allowed to cancel appointment";

    public CancelForbiddenException() {
        super(MESSAGE);
    }

    public CancelForbiddenException(String message) {
        super(message);
    }
}
