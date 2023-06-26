package ar.edu.itba.paw.interfaces.services;


import ar.edu.itba.paw.interfaces.services.exceptions.AppointmentNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationService {

    public Notification createNotification(long userId, long appointmentId)
            throws UserNotFoundException, AppointmentNotFoundException;

    public void deleteNotification(long userId, long appointmentId)
            throws UserNotFoundException, AppointmentNotFoundException;

    public List<Notification> getUserNotifications(long userId);

    public Optional<Notification> getUserAppointmentNotification(long userId, long appointmentId);
}
