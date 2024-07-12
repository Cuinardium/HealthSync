package ar.edu.itba.paw.webapp.exceptions;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException() {
        super("File not found");
    }
}
