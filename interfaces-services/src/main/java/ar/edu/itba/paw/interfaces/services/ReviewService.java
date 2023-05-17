package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Review;

public interface ReviewService {

  // =============== Inserts ===============

  public Review createReview(long doctorId, long patientId, int rating, String description);
  
  // =============== Queries ===============
  
  Page<Review> getReviewsForDoctor(long doctorId, Integer page, Integer pageSize);

  public boolean canReview(long doctorId, long patientId);
}
