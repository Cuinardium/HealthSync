package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.NotificationDao;
import ar.edu.itba.paw.models.Notification;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class NotificationDaoJpa implements NotificationDao {

  @PersistenceContext private EntityManager em;

  @Override
  public Notification createNotification(Notification notification) {
    em.persist(notification);
    return notification;
  }

  @Override
  public void deleteNotification(Notification notification) {
    em.remove(notification);
  }

  @Override
  public List<Notification> getUserNotifications(long userId) {
    TypedQuery<Notification> query =
        em.createQuery("from Notification where user_id = :userId", Notification.class);
    query.setParameter("userId", userId);
    return query.getResultList();
  }

  @Override
  public Optional<Notification> getUserAppointmentNotification(long userId, long appointmentId) {
    TypedQuery<Notification> query =
        em.createQuery(
            "from Notification where user_id = :userId and appointment_id = :appointmentId",
            Notification.class);
    query.setParameter("userId", userId);
    query.setParameter("appointmentId", appointmentId);
    return query.getResultList().stream().findFirst();
  }

  @Override
  public Optional<Notification> getNotification(long notificationId) {
    return Optional.ofNullable(em.find(Notification.class, notificationId));
  }
}
