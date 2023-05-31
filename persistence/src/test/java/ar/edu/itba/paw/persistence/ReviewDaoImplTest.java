package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.AttendingHours;
import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.persistence.config.TestConfig;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

  private static final int INSERTED_REVIEWS = 5;

  private static final Long INSERTED_DOCTOR_ID = 7L;
  private static final String INSERTED_DOCTOR_EMAIL = "doctor@email.com";
  private static final String INSERTED_DOCTOR_PASSWORD = "doctor_password";
  private static final String INSERTED_DOCTOR_FIRST_NAME = "doctor_first_name";
  private static final String INSERTED_DOCTOR_LAST_NAME = "doctor_last_name";
  private static final List<HealthInsurance> INSERTED_DOCTOR_INSURANCES =
      Arrays.asList(HealthInsurance.OMINT, HealthInsurance.OSDE);
  private static final Specialty INSERTED_DOCTOR_SPECIALTY =
      Specialty.PEDIATRIC_ALLERGY_AND_IMMUNOLOGY;
  private static final City INSERTED_DOCTOR_CITY = City.ADOLFO_GONZALES_CHAVES;
  private static final String INSERTED_DOCTOR_ADDRESS = "doctor_address";
  private static final AttendingHours INSERTED_DOCTOR_ATTENDING_HOURS =
      new AttendingHours(
          Arrays.asList(ThirtyMinuteBlock.BLOCK_00_00),
          Arrays.asList(ThirtyMinuteBlock.BLOCK_00_00),
          Arrays.asList(ThirtyMinuteBlock.BLOCK_00_00),
          Arrays.asList(ThirtyMinuteBlock.BLOCK_00_00),
          Arrays.asList(ThirtyMinuteBlock.BLOCK_00_00),
          new ArrayList<>(),
          new ArrayList<>());
  private static final Image INSERTED_DOCTOR_IMAGE = null;

  private static final Float INSERTED_DOCTOR_RATING = null;
  private static final Integer INSERTED_DOCTOR_RATING_COUNT = 0;
  private static final Location LOCATION_FOR_DOCTOR_7 =
      new Location(3, INSERTED_DOCTOR_CITY, INSERTED_DOCTOR_ADDRESS);

  private static final Doctor DOCTOR_7 =
      new Doctor(
          INSERTED_DOCTOR_ID,
          INSERTED_DOCTOR_EMAIL,
          INSERTED_DOCTOR_PASSWORD,
          INSERTED_DOCTOR_FIRST_NAME,
          INSERTED_DOCTOR_LAST_NAME,
          INSERTED_DOCTOR_IMAGE,
          INSERTED_DOCTOR_INSURANCES,
          INSERTED_DOCTOR_SPECIALTY,
          LOCATION_FOR_DOCTOR_7,
          INSERTED_DOCTOR_ATTENDING_HOURS,
          INSERTED_DOCTOR_RATING,
          INSERTED_DOCTOR_RATING_COUNT);

  private static final Long INSERTED_PATIENT_ID = 5L;
  private static final String INSERTED_PATIENT_EMAIL = "patient@email.com";
  private static final String INSERTED_PATIENT_PASSWORD = "patient_password";
  private static final String INSERTED_PATIENT_FIRST_NAME = "patient_first_name";
  private static final String INSERTED_PATIENT_LAST_NAME = "patient_last_name";
  private static final Image INSERTED_PATIENT_IMAGE = null;
  private static final HealthInsurance INSERTED_PATIENT_HEALTH_INSURANCE = HealthInsurance.OMINT;

  private static final Patient PATIENT_5 =
      new Patient(
          INSERTED_PATIENT_ID,
          INSERTED_PATIENT_EMAIL,
          INSERTED_PATIENT_PASSWORD,
          INSERTED_PATIENT_FIRST_NAME,
          INSERTED_PATIENT_LAST_NAME,
          INSERTED_PATIENT_IMAGE,
          INSERTED_PATIENT_HEALTH_INSURANCE);

  private static final long NON_EXISTING_DOCTOR_ID = 100;

  private static final Short RATING = 5;
  private static final String DESCRIPTION = "This is a review description";
  private static final LocalDate DATE = LocalDate.now();

  @PersistenceContext EntityManager em;

  @Autowired private ReviewDaoJPA reviewDao;

  @Test
  public void testCreateReview() {
    // 1. Precondiciones
    Review review_aux = new Review(null, DOCTOR_7.getId(), PATIENT_5, DATE, DESCRIPTION, RATING);
    // 2. Ejercitar la class under test
    Review review = reviewDao.createReview(review_aux);

    em.flush();

    // 3. Meaningful assertions
    Assert.assertEquals(INSERTED_PATIENT_ID, review.getPatient().getId());
    Assert.assertEquals(RATING, review.getRating());
    Assert.assertEquals(DATE, review.getDate());
    Assert.assertEquals(DESCRIPTION, review.getDescription());

    // TODO: assert the expected columns
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
