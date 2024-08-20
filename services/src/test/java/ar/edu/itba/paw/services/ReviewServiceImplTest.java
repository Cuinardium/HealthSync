package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ReviewDao;
import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.PatientNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.ReviewForbiddenException;
import ar.edu.itba.paw.models.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

// Le permite a Mockito tomar control de JUnit y permite anotaciones que sino no estarian
// disponibles
@RunWith(MockitoJUnitRunner.class)
public class ReviewServiceImplTest {

  // ================== Doctor Constants ==================
  private static final long DOCTOR_ID = 0;
  private static final String DOCTOR_EMAIL = "doctor_email";
  private static final String DOCTOR_PASSWORD = "doctor_password";
  private static final String DOCTOR_FIRST_NAME = "doctor_first_name";
  private static final String DOCTOR_LAST_NAME = "doctor_last_name";
  private static final Image IMAGE = new Image.Builder(null, "images/png").build();
  private static final Locale LOCALE = new Locale("en");
  private static final Set<HealthInsurance> DOCTOR_HEALTH_INSURANCES =
      new HashSet<>(Arrays.asList(HealthInsurance.OSDE, HealthInsurance.OMINT));
  private static final Specialty SPECIALTY = Specialty.CARDIOLOGY;
  private static final String CITY = "Ayacucho";
  private static final String ADDRESS = "1234";
  private static final Collection<ThirtyMinuteBlock> ATTENDING_HOURS_FOR_DAY =
      ThirtyMinuteBlock.fromRange(ThirtyMinuteBlock.BLOCK_08_00, ThirtyMinuteBlock.BLOCK_16_00);
  private static final Set<AttendingHours> ATTENDING_HOURS =
      new HashSet<>(
          Stream.of(
                  AttendingHours.createFromList(
                      DOCTOR_ID, DayOfWeek.MONDAY, ATTENDING_HOURS_FOR_DAY),
                  AttendingHours.createFromList(
                      DOCTOR_ID, DayOfWeek.TUESDAY, ATTENDING_HOURS_FOR_DAY),
                  AttendingHours.createFromList(
                      DOCTOR_ID, DayOfWeek.WEDNESDAY, ATTENDING_HOURS_FOR_DAY),
                  AttendingHours.createFromList(
                      DOCTOR_ID, DayOfWeek.THURSDAY, ATTENDING_HOURS_FOR_DAY),
                  AttendingHours.createFromList(
                      DOCTOR_ID, DayOfWeek.FRIDAY, ATTENDING_HOURS_FOR_DAY),
                  AttendingHours.createFromList(
                      DOCTOR_ID, DayOfWeek.SATURDAY, ATTENDING_HOURS_FOR_DAY),
                  AttendingHours.createFromList(
                      DOCTOR_ID, DayOfWeek.SUNDAY, ATTENDING_HOURS_FOR_DAY))
              .flatMap(Collection::stream)
              .collect(Collectors.toList()));
  private static final Float RATING = 3F;
  private static final Integer RATING_COUNT = 1;
  private static final Doctor DOCTOR =
      new Doctor.Builder(
              DOCTOR_EMAIL,
              DOCTOR_PASSWORD,
              DOCTOR_FIRST_NAME,
              DOCTOR_LAST_NAME,
              DOCTOR_HEALTH_INSURANCES,
              SPECIALTY,
              CITY,
              ADDRESS,
              ATTENDING_HOURS,
              LOCALE)
          .id(DOCTOR_ID)
          .rating(RATING)
          .ratingCount(RATING_COUNT)
          .isVerified(true)
          .image(IMAGE)
          .build();
  // ================== Patient Constants ==================
  private static final long PATIENT_ID = 1;
  private static final String PATIENT_EMAIL = "patient_email";
  private static final String PATIENT_PASSWORD = "patient_password";
  private static final String FIRST_NAME = "patient_first_name";
  private static final String PATIENT_LAST_NAME = "patient_last_name";

  private static final HealthInsurance PATIENT_HEALTH_INSURANCE = HealthInsurance.NONE;
  private static final Patient PATIENT =
      new Patient.Builder(
              PATIENT_EMAIL,
              PATIENT_PASSWORD,
              FIRST_NAME,
              PATIENT_LAST_NAME,
              PATIENT_HEALTH_INSURANCE,
              LOCALE)
          .id(PATIENT_ID)
          .isVerified(true)
          .image(IMAGE)
          .build();

  private static final short REVIEW_RATING = 3;
  private static final String REVIEW_DESCRIPTION = "review_description";
  private static final LocalDate REVIEW_DATE = LocalDate.now();
  private static final Review REVIEW =
      new Review.Builder(DOCTOR, REVIEW_RATING, REVIEW_DESCRIPTION, REVIEW_DATE, PATIENT).build();

  private static final Page<Review> REVIEWS =
      new Page<>(Collections.singletonList(REVIEW), null, null, null);
  // ================== Review Constants ==================
  private static final long REVIEW_ID = 1;
  @Mock private DoctorService doctorService;

  @Mock private PatientService patientService;

  @Mock private AppointmentService appointmentService;

  @Mock private ReviewDao reviewDao;

  @InjectMocks private ReviewServiceImpl rs;

  // =================== createReview ===================

  @Test
  public void testCreateReview()
      throws DoctorNotFoundException, PatientNotFoundException, ReviewForbiddenException {

    // Mock doctorService
    Mockito.when(doctorService.getDoctorById(DOCTOR_ID)).thenReturn(Optional.of(DOCTOR));

    // Mock patientService
    Mockito.when(patientService.getPatientById(PATIENT_ID)).thenReturn(Optional.of(PATIENT));

    // Mock appointmentService
    Mockito.when(appointmentService.hasPatientMetDoctor(PATIENT_ID, DOCTOR_ID)).thenReturn(true);

    // Mock reviewDao
    Mockito.when(reviewDao.createReview(REVIEW)).thenReturn(REVIEW);

    // Call method
    Review review = rs.createReview(DOCTOR_ID, PATIENT_ID, REVIEW_RATING, REVIEW_DESCRIPTION);

    // Assert
    Assert.assertEquals(REVIEW, review);
  }

  @Test(expected = DoctorNotFoundException.class)
  public void testCreateReviewForUnexistingDoctor()
      throws DoctorNotFoundException, PatientNotFoundException, ReviewForbiddenException {
    // Mock doctorService
    Mockito.when(doctorService.getDoctorById(DOCTOR_ID)).thenReturn(Optional.empty());

    // Call method
    rs.createReview(DOCTOR_ID, PATIENT_ID, REVIEW_RATING, REVIEW_DESCRIPTION);
  }

  @Test(expected = PatientNotFoundException.class)
  public void testCreateReviewByUnexistingPatient()
      throws DoctorNotFoundException, PatientNotFoundException, ReviewForbiddenException {
    // Mock doctorService
    Mockito.when(doctorService.getDoctorById(DOCTOR_ID)).thenReturn(Optional.of(DOCTOR));

    // Mock patientService
    Mockito.when(patientService.getPatientById(PATIENT_ID)).thenReturn(Optional.empty());

    // Call method
    rs.createReview(DOCTOR_ID, PATIENT_ID, REVIEW_RATING, REVIEW_DESCRIPTION);
  }

  @Test(expected = ReviewForbiddenException.class)
  public void testCreateReviewByPatientWhoHasNotMetDoctor()
      throws DoctorNotFoundException, PatientNotFoundException, ReviewForbiddenException {
    // Mock doctorService
    Mockito.when(doctorService.getDoctorById(DOCTOR_ID)).thenReturn(Optional.of(DOCTOR));

    // Mock patientService
    Mockito.when(patientService.getPatientById(PATIENT_ID)).thenReturn(Optional.of(PATIENT));

    // Mock appointmentService
    Mockito.when(appointmentService.hasPatientMetDoctor(PATIENT_ID, DOCTOR_ID)).thenReturn(false);

    // Call method
    rs.createReview(DOCTOR_ID, PATIENT_ID, REVIEW_RATING, REVIEW_DESCRIPTION);
  }

  // =================== getReview ===================

  @Test
  public void testGetReview() {
    // Mock reviewDao
    Mockito.when(reviewDao.getReview(REVIEW_ID)).thenReturn(Optional.of(REVIEW));

    // Call method
    Optional<Review> review = rs.getReview(REVIEW_ID);

    // Assert
    Assert.assertEquals(REVIEW, review.get());
  }

  @Test
  public void testGetUnexistingReview() {
    // Mock reviewDao
    Mockito.when(reviewDao.getReview(REVIEW_ID)).thenReturn(Optional.empty());

    // Call method
    Optional<Review> review = rs.getReview(REVIEW_ID);

    // Assert
    Assert.assertFalse(review.isPresent());
  }

  // =================== getReviewsForDoctor ===================

  @Test
  public void testGetReviewsForDoctor() throws DoctorNotFoundException {
    // Mock doctorService
    Mockito.when(reviewDao.getReviewsForDoctor(DOCTOR_ID, null, null)).thenReturn(REVIEWS);
    Mockito.when(doctorService.getDoctorById(DOCTOR_ID)).thenReturn(Optional.of(DOCTOR));

    // Call method
    List<Review> reviews = rs.getReviewsForDoctor(DOCTOR_ID, null, null).getContent();

    // Assert
    Assert.assertEquals(REVIEWS.getContent(), reviews);
  }

  @Test
  public void testGetReviewsForDoctorWithoutReviews() throws DoctorNotFoundException {
    // Mock doctorService
    Mockito.when(reviewDao.getReviewsForDoctor(DOCTOR_ID, null, null))
        .thenReturn(new Page<>(Collections.emptyList(), null, 0, null));
    Mockito.when(doctorService.getDoctorById(DOCTOR_ID)).thenReturn(Optional.of(DOCTOR));

    // Call method
    List<Review> reviews = rs.getReviewsForDoctor(DOCTOR_ID, null, null).getContent();

    // Assert
    Assert.assertTrue(reviews.isEmpty());
  }

  @Test(expected = DoctorNotFoundException.class)
  public void testGetReviewsForUnexistingDoctor() throws DoctorNotFoundException {
    // Mock doctorService
    Mockito.when(doctorService.getDoctorById(DOCTOR_ID)).thenReturn(Optional.empty());

    // Call method
    rs.getReviewsForDoctor(DOCTOR_ID, null, null);
  }

  // =================== canReview ===================

  @Test
  public void testCanReview() {
    // Mock doctorService
    Mockito.when(doctorService.getDoctorById(DOCTOR_ID)).thenReturn(Optional.of(DOCTOR));

    // Mock patientService
    Mockito.when(patientService.getPatientById(PATIENT_ID)).thenReturn(Optional.of(PATIENT));

    // Mock appointmentService
    Mockito.when(appointmentService.hasPatientMetDoctor(PATIENT_ID, DOCTOR_ID)).thenReturn(true);

    // Call method
    boolean canReview = rs.canReview(DOCTOR_ID, PATIENT_ID);

    // Assert
    Assert.assertTrue(canReview);
  }

  @Test
  public void testCanReviewForUnexistingDoctor() {
    // Mock doctorService
    Mockito.when(doctorService.getDoctorById(DOCTOR_ID)).thenReturn(Optional.empty());

    // Call method
    boolean canReview = rs.canReview(DOCTOR_ID, PATIENT_ID);

    // Assert
    Assert.assertFalse(canReview);
  }

  @Test
  public void testCanReviewForUnexistingPatient() {
    // Mock doctorService
    Mockito.when(doctorService.getDoctorById(DOCTOR_ID)).thenReturn(Optional.of(DOCTOR));

    // Mock patientService
    Mockito.when(patientService.getPatientById(PATIENT_ID)).thenReturn(Optional.empty());

    // Call method
    boolean canReview = rs.canReview(DOCTOR_ID, PATIENT_ID);

    // Assert
    Assert.assertFalse(canReview);
  }

  @Test
  public void testCanReviewForPatientWhoHasNotMetDoctor() {
    // Mock doctorService
    Mockito.when(doctorService.getDoctorById(DOCTOR_ID)).thenReturn(Optional.of(DOCTOR));

    // Mock patientService
    Mockito.when(patientService.getPatientById(PATIENT_ID)).thenReturn(Optional.of(PATIENT));

    // Mock appointmentService
    Mockito.when(appointmentService.hasPatientMetDoctor(PATIENT_ID, DOCTOR_ID)).thenReturn(false);

    // Call method
    boolean canReview = rs.canReview(DOCTOR_ID, PATIENT_ID);

    // Assert
    Assert.assertFalse(canReview);
  }
}
