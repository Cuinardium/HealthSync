package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.persistence.config.TestConfig;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

  private static final long DOCTOR_ID = 7L;
  private static final long PATIENT_ID = 5L;
  private static final long NO_REVIEW_PATIENT_ID = 8L;
  private static final long REVIEW_ID = 7L;
  private static final Integer PAGE = 0;
  private static final int PAGE_SIZE = 10;
  @PersistenceContext private EntityManager em;
  @Autowired private ReviewDaoJpa reviewDao;

  @Test
  public void testGetReview() {
    // 1. Precondition (Review must exist in the DB for this test)
    Optional<Review> maybeReview = reviewDao.getReview(REVIEW_ID);

    // 2. Assertion
    Assert.assertTrue(maybeReview.isPresent());
    Review review = maybeReview.get();
    Assert.assertEquals(REVIEW_ID, review.getId().longValue());
  }

  @Test
  public void testGetReviewNotFound() {
    // 1. Exercise
    Optional<Review> maybeReview = reviewDao.getReview(999L); // Non-existent ID

    // 2. Assertion
    Assert.assertFalse(maybeReview.isPresent());
  }

  @Test
  public void testGetReviewsForDoctor() {
    // 1. Exercise
    Page<Review> page = reviewDao.getReviewsForDoctor(DOCTOR_ID, PAGE, PAGE_SIZE);

    // 2. Assertion
    Assert.assertNotNull(page);
    Assert.assertEquals(PAGE, page.getCurrentPage());
    Assert.assertTrue(page.getContent().size() <= PAGE_SIZE);
  }

  @Test
  public void testGetReviewsForDoctorNoReviews() {
    // 1. Exercise
    Page<Review> page =
        reviewDao.getReviewsForDoctor(999L, PAGE, PAGE_SIZE); // Non-existent doctor ID

    // 2. Assertion
    Assert.assertNotNull(page);
    Assert.assertTrue(page.getContent().isEmpty());
  }

  @Test
  public void testHasReviewedDoctorTrue() {
    // 1. Exercise
    boolean hasReviewed = reviewDao.hasReviewedDoctor(DOCTOR_ID, PATIENT_ID);

    // 2. Assertion
    Assert.assertTrue(hasReviewed);
  }

  @Test
  public void testHasReviewedDoctorFalse() {
    // 1. Exercise
    boolean hasReviewed = reviewDao.hasReviewedDoctor(999L, PATIENT_ID); // Non-existent doctor ID

    // 2. Assertion
    Assert.assertFalse(hasReviewed);
  }

  @Test
  public void testHasReviewedDoctorFalseNoReview() {
    // 1. Exercise
    boolean hasReviewed =
        reviewDao.hasReviewedDoctor(DOCTOR_ID, NO_REVIEW_PATIENT_ID); // Non-existent review

    // 2. Assertion
    Assert.assertFalse(hasReviewed);
  }
}
