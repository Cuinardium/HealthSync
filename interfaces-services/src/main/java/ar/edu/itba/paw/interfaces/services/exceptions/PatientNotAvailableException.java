package ar.edu.itba.paw.interfaces.services.exceptions;

public class PatientNotAvailableException extends Exception {

    private static final String MESSAGE = "Patient is not available at the given time";

    public PatientNotAvailableException() {
        super(MESSAGE);
    }

    public PatientNotAvailableException(String message) {
        super(message);
    }
}
