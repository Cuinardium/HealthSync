package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ReviewDao;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Review;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getReviewsForDoctor'");
  }
}
