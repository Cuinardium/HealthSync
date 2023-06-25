package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Notification;

import java.util.List;

public interface NotificationDao {

    public Notification createNotification(Notification notification);

    public void deleteNotification(Notification notification);

    public List<Notification> getUserNotifications(long userId);
}
