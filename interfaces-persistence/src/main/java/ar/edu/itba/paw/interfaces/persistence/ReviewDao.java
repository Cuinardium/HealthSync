package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Review;
import java.util.Optional;

public interface ReviewDao {

  // =============== Inserts ===============

  public Review createReview(Review review);

  // =============== Queries ===============

  public Optional<Review> getReview(long reviewId);

  public Page<Review> getReviewsForDoctor(long doctorId, Integer page, Integer pageSize);

  public boolean hasReviewedDoctor(long doctorId, long patientId);
}
