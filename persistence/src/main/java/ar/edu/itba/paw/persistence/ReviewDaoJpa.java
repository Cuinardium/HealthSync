package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ReviewDao;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Review;
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
public class ReviewDaoJpa implements ReviewDao {

  @PersistenceContext private EntityManager em;

  @Override
  public Review createReview(Review review) {
    em.persist(review);
    return review;
  }

  @Override
  public Optional<Review> getReview(long reviewId) {
    Review review = em.find(Review.class, reviewId);
    return Optional.ofNullable(review);
  }

  @Override
  public Page<Review> getReviewsForDoctor(long doctorId, Integer page, Integer pageSize) {
    Query nativeQuery =
        em.createNativeQuery("SELECT review_id FROM review WHERE doctor_id = " + doctorId);

    Query countQuery =
        em.createNativeQuery("SELECT COUNT(*) FROM review WHERE doctor_id = " + doctorId);

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

    if (idList.isEmpty()) {
      return new Page<>(Collections.emptyList(), page, 0, pageSize);
    }

    final TypedQuery<Review> query =
        em.createQuery("from Review where id in :idList", Review.class);
    query.setParameter("idList", idList);

    List<Review> content = query.getResultList();
    Number count = (Number) countQuery.getSingleResult();

    return new Page<>(content, page, count.intValue(), pageSize);
  }

  @Override
  public boolean hasReviewedDoctor(long doctorId, long patientId) {
    final TypedQuery<Review> query =
        em.createQuery(
            "from Review  as review where review.doctor.id = :doctorId and review.patient.id = :patientId",
            Review.class);
    query.setParameter("doctorId", doctorId);
    query.setParameter("patientId", patientId);

    return query.getResultList().stream().findFirst().isPresent();
  }
}
