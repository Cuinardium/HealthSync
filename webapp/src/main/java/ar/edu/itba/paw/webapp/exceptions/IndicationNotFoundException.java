package ar.edu.itba.paw.webapp.exceptions;

public class IndicationNotFoundException extends Exception {

    public IndicationNotFoundException() {
        super("Indication not found");
    }
}
