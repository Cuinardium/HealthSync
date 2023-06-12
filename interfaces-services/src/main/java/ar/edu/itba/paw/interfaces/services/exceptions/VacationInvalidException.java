package ar.edu.itba.paw.interfaces.services.exceptions;

public class VacationInvalidException extends Exception{

    private static final String MESSAGE = "Invalid Vacation";

    public VacationInvalidException() {
        super(MESSAGE);
    }

    public VacationInvalidException(String message) {
        super(message);
    }
}
