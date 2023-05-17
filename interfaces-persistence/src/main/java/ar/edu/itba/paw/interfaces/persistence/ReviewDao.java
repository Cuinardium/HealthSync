package ar.edu.itba.paw.interfaces.persistence;

import java.time.LocalDate;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Review;

public interface ReviewDao {

  // =============== Inserts ===============

  public Review createReview(long doctorId, long patientId, int rating, LocalDate date, String description);
  
  // =============== Queries ===============
  
  Page<Review> getReviewsForDoctor(long doctorId, Integer page, Integer pageSize);
}
