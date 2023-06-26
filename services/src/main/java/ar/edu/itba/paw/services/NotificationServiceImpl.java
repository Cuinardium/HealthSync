package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.NotificationDao;
import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.NotificationService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.exceptions.AppointmentNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final UserService userService;

    private final AppointmentService appointmentService;

    private final NotificationDao notificationDao;

    @Autowired
    public NotificationServiceImpl(UserService userService, AppointmentService appointmentService, NotificationDao notificationDao) {
        this.userService = userService;
        this.appointmentService = appointmentService;
        this.notificationDao = notificationDao;
    }

    @Transactional
    @Override
    public Notification createNotification(long userId, long appointmentId) throws UserNotFoundException, AppointmentNotFoundException {
        if (!appointmentService.getAppointmentById(appointmentId).isPresent()) {
            throw new AppointmentNotFoundException();
        }
        if (!userService.getUserById(userId).isPresent()) {
            throw new UserNotFoundException();
        }

        Notification notification= new Notification(userId, appointmentId);

        return notificationDao.createNotification(notification);
    }

    @Transactional
    @Override
    public void deleteNotification(long userId, long appointmentId) throws UserNotFoundException, AppointmentNotFoundException {
        if (!appointmentService.getAppointmentById(appointmentId).isPresent()) {
            throw new AppointmentNotFoundException();
        }
        if (!userService.getUserById(userId).isPresent()) {
            throw new UserNotFoundException();
        }

        Notification notification= new Notification(userId, appointmentId);

        notificationDao.deleteNotification(notification);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Notification> getUserNotifications(long userId) {
        return notificationDao.getUserNotifications(userId);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Notification> getUserAppointmentNotification(long userId, long appointmentId) {
        return notificationDao.getUserAppointmentNotification(userId, appointmentId);
    }

    @Transactional
    @Override
    public void deleteNotificationIfExists(long userId, long appointmentId) throws UserNotFoundException, AppointmentNotFoundException {
        if(getUserAppointmentNotification(userId, appointmentId).isPresent()){
            deleteNotification(userId, appointmentId);
        }
    }

}
