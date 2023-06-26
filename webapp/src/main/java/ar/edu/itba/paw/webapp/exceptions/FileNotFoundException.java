package ar.edu.itba.paw.webapp.exceptions;

public class FileNotFoundException extends RuntimeException {
    private static final String MESSAGE_ID = "error.fileNotFound";

    public FileNotFoundException() {
        super(MESSAGE_ID);
    }

    public FileNotFoundException(String message) {
        super(message);
    }
}
