package ar.edu.itba.paw.interfaces.services.exceptions;

public class ReviewForbiddenException extends Exception {
    private static final String MESSAGE = "Patient not allowed to review this doctor";

    public ReviewForbiddenException() {
        super(MESSAGE);
    }

    public ReviewForbiddenException(String message) {
        super(message);
    }
}
