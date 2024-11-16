package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.exceptions.AlreadyReviewedException;
import ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.PatientNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.ReviewForbiddenException;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Review;
import java.util.Optional;

public interface ReviewService {

  // =============== Inserts ===============

  public Review createReview(long doctorId, long patientId, int rating, String description)
      throws DoctorNotFoundException,
          PatientNotFoundException,
          ReviewForbiddenException,
          AlreadyReviewedException;

  // =============== Queries ===============

  public Optional<Review> getReview(long reviewId);

  public Page<Review> getReviewsForDoctor(long doctorId, Integer page, Integer pageSize)
      throws DoctorNotFoundException;

  public boolean canReview(long doctorId, long patientId);
}
