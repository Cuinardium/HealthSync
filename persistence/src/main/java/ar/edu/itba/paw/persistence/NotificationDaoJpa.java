package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.NotificationDao;
import ar.edu.itba.paw.models.Notification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class NotificationDaoJpa implements NotificationDao {

    @PersistenceContext
    EntityManager em;

    @Override
    public Notification createNotification(Notification notification) {
        em.persist(notification);
        return notification;
    }

    @Override
    public void deleteNotification(Notification notification) {
        em.detach(notification);
    }

    @Override
    public List<Notification> getUserNotifications(long userId) {
        Query nativeQuery=
                em.createNativeQuery("SELECT id FROM notification WHERE user_id = " + userId);

        @SuppressWarnings("unchecked")
        final List<Long> idList =
                (List<Long>)
                        nativeQuery
                                .getResultList()
                                .stream()
                                .map(o -> ((Number) o).longValue())
                                .collect(Collectors.toList());

        if (idList.isEmpty()) return new ArrayList<>();

        final TypedQuery<Notification> query =
                em.createQuery("from Notification where id in :idList", Notification.class);
        query.setParameter("idList", idList);

        List<Notification> content = query.getResultList();

        return new ArrayList<Notification>(content) {
        };
    }

}
