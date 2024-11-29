package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.IndicationDao;
import ar.edu.itba.paw.models.Indication;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.persistence.utils.QueryBuilder;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class IndicationDaoJpa implements IndicationDao {

  @PersistenceContext private EntityManager em;

  @Override
  public Indication createIndication(Indication indication) {
    em.persist(indication);
    return indication;
  }

  @Override
  public Page<Indication> getIndicationsForAppointment(
      long appointmentId, Integer page, Integer pageSize) {
    /*Query nativeQuery =
        em.createNativeQuery(
            "SELECT indication_id, indication_date FROM indication WHERE appointment_id = " + appointmentId + "ORDER");

    Query countQuery =
            em.createNativeQuery(
                    "SELECT COUNT(*) FROM indication WHERE appointment_id = " + appointmentId);*/

    QueryBuilder nativeQueryBuilder =
        new QueryBuilder()
            .select("indication_id", "indication_date")
            .from("indication")
            .where("appointment_id = " + appointmentId)
            .orderByDesc("indication_date")
            .orderByDesc("indication_id");

    String builtQuery = nativeQueryBuilder.build();
    Query nativeQuery = em.createNativeQuery(builtQuery);
    Query countQuery = em.createNativeQuery(builtQuery);

    if (page != null && page >= 0 && pageSize != null && pageSize > 0) {
      nativeQuery.setMaxResults(pageSize);
      nativeQuery.setFirstResult(page * pageSize);
    }

    @SuppressWarnings("unchecked")
    final List<Long> idList =
        (List<Long>)
            nativeQuery.getResultList().stream()
                .map(row -> ((Number) ((Object[]) row)[0]).longValue()) // Extract the ID
                .collect(Collectors.toList());

    if (idList.isEmpty()) {
      return new Page<>(Collections.emptyList(), page, 0, pageSize);
    }


    final TypedQuery<Indication> query =
        em.createQuery("from Indication where id in :idList order by date desc ,id desc", Indication.class);
    query.setParameter("idList", idList);

    List<Indication> content = query.getResultList();
    int qtyIndications = countQuery.getResultList().size();

    return new Page<>(content, page, qtyIndications, pageSize);
  }

  @Override
  public Optional<Indication> getIndication(long id) {
    return Optional.ofNullable(em.find(Indication.class, id));
  }
}
