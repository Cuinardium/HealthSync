package ar.edu.itba.paw.webapp.exceptions;

public class NotificationNotFoundException extends RuntimeException  {

    public NotificationNotFoundException() {
        super("Notification not found");
    }
}
