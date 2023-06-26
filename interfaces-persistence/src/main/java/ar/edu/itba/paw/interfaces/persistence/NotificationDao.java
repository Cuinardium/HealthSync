package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationDao {

    public Notification createNotification(Notification notification);

    public void deleteNotification(Notification notification);

    public List<Notification> getUserNotifications(long userId);

    public Optional<Notification> getUserAppointmentNotification(long userId, long appointmentId);
}
