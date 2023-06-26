package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.IndicationDao;
import ar.edu.itba.paw.models.Indication;
import ar.edu.itba.paw.models.Page;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class IndicationDaoJpa implements IndicationDao {

  @PersistenceContext EntityManager em;

  @Override
  public Indication createIndication(Indication indication) {
    em.persist(indication);
    return indication;
  }

  @Override
  public Page<Indication> getIndicationsForAppointment(
      long appointmentId, Integer page, Integer pageSize) {
    Query nativeQuery =
        em.createNativeQuery(
            "SELECT indication_id FROM indication WHERE appointment_id = " + appointmentId);

    Query countQuery =
        em.createNativeQuery(
            "SELECT COUNT(*) FROM indication WHERE appointment_id = " + appointmentId);

    if (page != null && page >= 0 && pageSize != null && pageSize > 0) {
      nativeQuery.setMaxResults(pageSize);
      nativeQuery.setFirstResult(page * pageSize);
    }

    @SuppressWarnings("unchecked")
    final List<Long> idList =
        (List<Long>)
            nativeQuery.getResultList().stream()
                .map(o -> ((Number) o).longValue())
                .collect(Collectors.toList());

    if (idList.isEmpty()) return new Page<>(new ArrayList<>(), page, 0, pageSize);

    Number count = (Number) countQuery.getSingleResult();

    final TypedQuery<Indication> query =
        em.createQuery("from Indication where id in :idList order by id desc", Indication.class);
    query.setParameter("idList", idList);

    List<Indication> content = query.getResultList();

    return new Page<>(content, page, count.intValue(), pageSize);
  }
}
