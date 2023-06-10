package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ReviewDao;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

@Repository
public class ReviewDaoJPA implements ReviewDao {

  @PersistenceContext EntityManager em;

  @Override
  public Review createReview(Review review) {
    em.persist(review);
    return review;
  }

    @Override
  public Page<Review> getReviewsForDoctor(long doctorId, Integer page, Integer pageSize) {
    Query nativeQuery =
        em.createNativeQuery("SELECT review_id FROM review WHERE doctor_id = " + doctorId);

    if (page != null && page >= 0 && pageSize != null && pageSize > 0) {
      nativeQuery.setMaxResults(pageSize);
      nativeQuery.setFirstResult(page * pageSize);
    }

    final List<Long> idList =
        (List<Long>)
            nativeQuery.getResultList().stream()
                .map(o -> ((Number) o).longValue())
                .collect(Collectors.toList());

    if (idList.isEmpty()) return new Page<>(new ArrayList<>(), page, 0, pageSize);

    final TypedQuery<Review> query =
        em.createQuery("from Review where id in :idList", Review.class);
    query.setParameter("idList", idList);

    List<Review> content = query.getResultList();

    return new Page<>(content, page, content.size(), pageSize);
  }

}
