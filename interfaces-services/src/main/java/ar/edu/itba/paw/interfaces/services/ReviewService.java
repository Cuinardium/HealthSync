package ar.edu.itba.paw.interfaces.services;

import java.util.List;

import ar.edu.itba.paw.models.Review;

public interface ReviewService {

  // =============== Inserts ===============

  public Review createReview(long doctorId, long patientId, int rating, String description);
  
  // =============== Queries ===============
  
  List<Review> getReviewsForDoctor(long doctorId);
}
