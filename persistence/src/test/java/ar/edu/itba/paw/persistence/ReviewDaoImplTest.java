package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.persistence.config.TestConfig;
import java.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ReviewDaoImplTest {

  private static final int INSERTED_REVIEWS = 5;

  private static final Long INSERTED_DOCTOR_ID = 7L;
  private static final Long INSERTED_PATIENT_ID = 5L;
  private static final long NON_EXISTING_DOCTOR_ID = 100;

  private static final int RATING = 5;
  private static final String DESCRIPTION = "This is a review description";
  private static final LocalDate DATE = LocalDate.now();

  @Autowired private ReviewDaoImpl reviewDao;

  @Test
  public void testCreateReview() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Review review =
        reviewDao.createReview(INSERTED_DOCTOR_ID, INSERTED_PATIENT_ID, RATING, DATE, DESCRIPTION);

    // 3. Meaningful assertions
    Assert.assertEquals(INSERTED_PATIENT_ID, review.getPatient().getId());
    Assert.assertEquals(RATING, review.getRating());
    Assert.assertEquals(DATE, review.getDate());
    Assert.assertEquals(DESCRIPTION, review.getDescription());
  }

  @Test
  public void testGetReviewsForDoctor() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Page<Review> reviews = reviewDao.getReviewsForDoctor(INSERTED_DOCTOR_ID, null, null);

    // 3. Meaningful assertions
    Assert.assertEquals(INSERTED_REVIEWS, reviews.getContent().size());
    Assert.assertNull(reviews.getTotalPages());
    Assert.assertNull(reviews.getCurrentPage());
  }

  @Test
  public void testGetReviewsForNonExistingDoctor() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Page<Review> reviews = reviewDao.getReviewsForDoctor(NON_EXISTING_DOCTOR_ID, null, null);

    // 3. Meaningful assertions
    Assert.assertEquals(0, reviews.getContent().size());
    Assert.assertNull(reviews.getTotalPages());
    Assert.assertNull(reviews.getCurrentPage());
  }
}
