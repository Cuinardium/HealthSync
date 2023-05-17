package ar.edu.itba.paw.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.interfaces.persistence.ReviewDao;
import ar.edu.itba.paw.interfaces.services.ReviewService;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Review;

@Service
public class ReviewServiceImpl implements ReviewService {

  private final ReviewDao reviewDao;

  @Autowired
  public ReviewServiceImpl(ReviewDao reviewDao) {
    this.reviewDao = reviewDao;
  }

  // =============== Inserts ===============

  @Override
  public Review createReview(long doctorId, long patientId, int rating, String description) {
    return reviewDao.createReview(doctorId, patientId, rating, LocalDate.now(), description);
  }

  // =============== Queries ===============

  @Override
  public Page<Review> getReviewsForDoctor(long doctorId, Integer page, Integer pageSize) {
    return reviewDao.getReviewsForDoctor(doctorId, page, pageSize);
  }
}
