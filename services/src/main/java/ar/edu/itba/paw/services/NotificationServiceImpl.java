package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.NotificationDao;
import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.NotificationService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.exceptions.AppointmentNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Notification;
import ar.edu.itba.paw.models.User;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationServiceImpl implements NotificationService {

  private final UserService userService;
  private final AppointmentService appointmentService;
  private final NotificationDao notificationDao;

  @Autowired
  public NotificationServiceImpl(
      UserService userService,
      AppointmentService appointmentService,
      NotificationDao notificationDao) {
    this.userService = userService;
    this.appointmentService = appointmentService;
    this.notificationDao = notificationDao;
  }

  @Transactional
  @Override
  public Notification createNotification(long userId, long appointmentId)
      throws UserNotFoundException, AppointmentNotFoundException {
    Optional<Appointment> appointmentOptional =
        appointmentService.getAppointmentById(appointmentId);
    if (!appointmentOptional.isPresent()) {
      throw new AppointmentNotFoundException();
    }

    Optional<User> userOptional = userService.getUserById(userId);
    if (!userOptional.isPresent()) {
      throw new UserNotFoundException();
    }

    Notification notification =
        new Notification.Builder(userOptional.get(), appointmentOptional.get()).build();

    return notificationDao.createNotification(notification);
  }

  @Transactional
  @Override
  public void deleteNotification(Notification notification) {
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

  public Optional<Notification> getNotification(long notificationId) {
    return notificationDao.getNotification(notificationId);
  }
}
