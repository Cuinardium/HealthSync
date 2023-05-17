package ar.edu.itba.paw.interfaces.persistence;

import java.time.LocalDate;
import java.util.List;

import ar.edu.itba.paw.models.Review;

public interface ReviewDao {

  // =============== Inserts ===============

  public Review createReview(long doctorId, long patientId, int rating, LocalDate date, String description);
  
  // =============== Queries ===============
  
  List<Review> getReviewsForDoctor(long doctorId);
}
