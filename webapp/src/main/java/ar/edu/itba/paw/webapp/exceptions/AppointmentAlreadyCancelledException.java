package ar.edu.itba.paw.webapp.exceptions;

public class AppointmentAlreadyCancelledException extends Exception {
    private static final String MESSAGE_ID = "error.appointmentAlreadyCancelled";

    public AppointmentAlreadyCancelledException() {
        super(MESSAGE_ID);
    }

    public AppointmentAlreadyCancelledException(String message) {
        super(message);
    }
}
