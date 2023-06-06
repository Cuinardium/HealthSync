package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Review;

public interface ReviewDao {

  // =============== Inserts ===============

  public Review createReview(Review review);

  // =============== Queries ===============

  public Page<Review> getReviewsForDoctor(long doctorId, Integer page, Integer pageSize);
}
