package ar.edu.itba.paw.webapp.exceptions;

public class ReviewNotFoundException extends Exception {
    public ReviewNotFoundException() {
        super("Review not found");
    }
}
