package ar.edu.itba.paw.persistence;

import static org.junit.Assert.assertThrows;

import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.EmailAlreadyExistsException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.config.TestConfig;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class DoctorDaoImplTest {

  private static final Long INSERTED_DOCTOR_ID = 7L;
  private static final String INSERTED_DOCTOR_EMAIL = "doctor@email.com";
  private static final String INSERTED_DOCTOR_PASSWORD = "doctor_password";
  private static final String INSERTED_DOCTOR_FIRST_NAME = "doctor_first_name";
  private static final String INSERTED_DOCTOR_LAST_NAME = "doctor_last_name";
  private static final Set<HealthInsurance> INSERTED_DOCTOR_INSURANCES =
      new HashSet<>(Arrays.asList(HealthInsurance.OMINT, HealthInsurance.OSDE));
  private static final Specialty INSERTED_DOCTOR_SPECIALTY =
      Specialty.PEDIATRIC_ALLERGY_AND_IMMUNOLOGY;
  private static final City INSERTED_DOCTOR_CITY = City.ADOLFO_GONZALES_CHAVES;
  private static final String INSERTED_DOCTOR_ADDRESS = "doctor_address";
  private static final Set<AttendingHours> INSERTED_DOCTOR_ATTENDING_HOURS = new HashSet<>(
        Arrays.asList(new AttendingHours(INSERTED_DOCTOR_ID, DayOfWeek.MONDAY, ThirtyMinuteBlock.BLOCK_00_00),
                new AttendingHours(INSERTED_DOCTOR_ID, DayOfWeek.TUESDAY, ThirtyMinuteBlock.BLOCK_00_00),
                new AttendingHours(INSERTED_DOCTOR_ID, DayOfWeek.WEDNESDAY, ThirtyMinuteBlock.BLOCK_00_00),
                new AttendingHours(INSERTED_DOCTOR_ID, DayOfWeek.THURSDAY, ThirtyMinuteBlock.BLOCK_00_00),
                new AttendingHours(INSERTED_DOCTOR_ID, DayOfWeek.FRIDAY, ThirtyMinuteBlock.BLOCK_00_00))
  );

  private static final Image INSERTED_DOCTOR_IMAGE = null;

  private static final Float INSERTED_DOCTOR_RATING = 3f;
  private static final Integer INSERTED_DOCTOR_RATING_COUNT = 5;

  private static final Long AUX_DOCTOR_ID = 8L;
  private static final String AUX_DOCTOR_EMAIL = "notdoctor_1@email.com";
  private static final String AUX_DOCTOR_PASSWORD = "notdoctor_password";
  private static final String AUX_DOCTOR_FIRST_NAME = "notdoctor_first_name";
  private static final String AUX_DOCTOR_LAST_NAME = "notdoctor_last_name";
  private static final Set<HealthInsurance> AUX_DOCTOR_INSURANCES =
      new HashSet<>(Arrays.asList(HealthInsurance.NONE, HealthInsurance.SWISS_MEDICAL));
  private static final Specialty AUX_DOCTOR_SPECIALTY = Specialty.NEPHROLOGY;
  private static final City AUX_DOCTOR_CITY = City.CASEROS;
  private static final String AUX_DOCTOR_ADDRESS = "notdoctor_address";
  private static final Set<AttendingHours> AUX_DOCTOR_ATTENDING_HOURS = new HashSet<>(
          Arrays.asList(new AttendingHours(INSERTED_DOCTOR_ID, DayOfWeek.MONDAY, ThirtyMinuteBlock.BLOCK_00_00),
                  new AttendingHours(INSERTED_DOCTOR_ID, DayOfWeek.TUESDAY, ThirtyMinuteBlock.BLOCK_02_00),
                  new AttendingHours(INSERTED_DOCTOR_ID, DayOfWeek.WEDNESDAY, ThirtyMinuteBlock.BLOCK_02_00),
                  new AttendingHours(INSERTED_DOCTOR_ID, DayOfWeek.THURSDAY, ThirtyMinuteBlock.BLOCK_02_00),
                  new AttendingHours(INSERTED_DOCTOR_ID, DayOfWeek.FRIDAY, ThirtyMinuteBlock.BLOCK_02_00),
                  new AttendingHours(INSERTED_DOCTOR_ID, DayOfWeek.SATURDAY, ThirtyMinuteBlock.BLOCK_02_00),
                  new AttendingHours(INSERTED_DOCTOR_ID, DayOfWeek.SUNDAY, ThirtyMinuteBlock.BLOCK_02_00))
  );

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
          INSERTED_DOCTOR_CITY,
          INSERTED_DOCTOR_ADDRESS,
          INSERTED_DOCTOR_ATTENDING_HOURS,
          new ArrayList<>(),
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

  private static final Short RATING = 5;
  private static final String DESCRIPTION = "This is a review description";
  private static final LocalDate DATE = LocalDate.now();

  private static final List<Review> REVIEWS_FOR_DOCTOR = new ArrayList<>(
          Arrays.asList(
                  new Review(null, DOCTOR_7, PATIENT_5, LocalDate.of(2023, 5, 17), "Muy buen doctor", (short) 5),
                  new Review(null, DOCTOR_7, PATIENT_5, LocalDate.of(2023, 5, 16), "Buen doctor", (short) 4),
                  new Review(null, DOCTOR_7, PATIENT_5, LocalDate.of(2023, 5, 15), "Regular doctor", (short) 3),
                  new Review(null, DOCTOR_7, PATIENT_5, LocalDate.of(2023, 5, 14), "Malo doctor", (short) 2),
                  new Review(null, DOCTOR_7, PATIENT_5, LocalDate.of(2023, 5, 13), "Muy malo doctor", (short) 1)
          )
  );

  private static final long NON_EXISTING_DOCTOR_ID = 100;

  private static final int INSERTED_REVIEWS = 5;

  @Autowired private DataSource ds;

  private JdbcTemplate jdbcTemplate;

  @PersistenceContext
  private EntityManager em;

  @Autowired private DoctorDaoJpa doctorDao;

  @Before
  public void setUp() {
    jdbcTemplate = new JdbcTemplate(ds);
  }

  @Test
  public void testCreateDoctor() throws DoctorAlreadyExistsException, EmailAlreadyExistsException {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Doctor doctor =
        doctorDao.createDoctor(new Doctor(
                        null,
                        AUX_DOCTOR_EMAIL,
                        AUX_DOCTOR_PASSWORD,
                        AUX_DOCTOR_FIRST_NAME,
                        AUX_DOCTOR_LAST_NAME,
                        INSERTED_DOCTOR_IMAGE,
                        AUX_DOCTOR_INSURANCES,
                        AUX_DOCTOR_SPECIALTY,
                        AUX_DOCTOR_CITY,
                        AUX_DOCTOR_ADDRESS,
                        AUX_DOCTOR_ATTENDING_HOURS,
                        new ArrayList<>(),
                        INSERTED_DOCTOR_RATING,
                        INSERTED_DOCTOR_RATING_COUNT));

    em.flush();

    // 3. Meaningful assertions
    //Assert.assertEquals(AUX_DOCTOR_ID, doctor.getId());
    Assert.assertEquals(AUX_DOCTOR_EMAIL, doctor.getEmail());
    Assert.assertEquals(AUX_DOCTOR_PASSWORD, doctor.getPassword());
    Assert.assertEquals(AUX_DOCTOR_FIRST_NAME, doctor.getFirstName());
    Assert.assertEquals(AUX_DOCTOR_LAST_NAME, doctor.getLastName());
    Assert.assertEquals(INSERTED_DOCTOR_IMAGE, doctor.getImage());
    Assert.assertEquals(AUX_DOCTOR_INSURANCES, doctor.getHealthInsurances());
    Assert.assertEquals(AUX_DOCTOR_ADDRESS, doctor.getAddress());
    Assert.assertEquals(AUX_DOCTOR_CITY, doctor.getCity());
    Assert.assertEquals(AUX_DOCTOR_SPECIALTY, doctor.getSpecialty());
    Assert.assertEquals(AUX_DOCTOR_ATTENDING_HOURS, doctor.getAttendingHours());

    Assert.assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "doctor"));
  }

  @Test
  public void testCreateDoctorAlreadyExists() {
    // 1.Precondiciones
    // 2. Ejercitar la class under test
    assertThrows(
        DoctorAlreadyExistsException.class,
        () ->
                doctorDao.createDoctor(new Doctor(
                        INSERTED_DOCTOR_ID,
                        AUX_DOCTOR_EMAIL,
                        AUX_DOCTOR_PASSWORD,
                        AUX_DOCTOR_FIRST_NAME,
                        AUX_DOCTOR_LAST_NAME,
                        INSERTED_DOCTOR_IMAGE,
                        AUX_DOCTOR_INSURANCES,
                        AUX_DOCTOR_SPECIALTY,
                        AUX_DOCTOR_CITY,
                        AUX_DOCTOR_ADDRESS,
                        AUX_DOCTOR_ATTENDING_HOURS,
                        new ArrayList<>(),
                        INSERTED_DOCTOR_RATING,
                        INSERTED_DOCTOR_RATING_COUNT)));
    // 3. Meaningful assertions

  }

  @Test
  public void testUpdateDoctorInfo() throws DoctorNotFoundException {
    // 1.Precondiciones
    // 2. Ejercitar la class under test
    Doctor doctor =
        doctorDao.updateDoctorInfo(
            INSERTED_DOCTOR_ID,
            AUX_DOCTOR_SPECIALTY,
            AUX_DOCTOR_CITY,
            AUX_DOCTOR_ADDRESS,
            AUX_DOCTOR_INSURANCES,
            AUX_DOCTOR_ATTENDING_HOURS);
    // 3. Meaningful assertions
    Assert.assertEquals(INSERTED_DOCTOR_ID, doctor.getId());
    Assert.assertEquals(INSERTED_DOCTOR_EMAIL, doctor.getEmail());
    Assert.assertEquals(INSERTED_DOCTOR_PASSWORD, doctor.getPassword());
    Assert.assertEquals(INSERTED_DOCTOR_FIRST_NAME, doctor.getFirstName());
    Assert.assertEquals(INSERTED_DOCTOR_LAST_NAME, doctor.getLastName());
    Assert.assertEquals(INSERTED_DOCTOR_IMAGE, doctor.getImage());
    Assert.assertEquals(AUX_DOCTOR_SPECIALTY, doctor.getSpecialty());
    Assert.assertEquals(AUX_DOCTOR_CITY, doctor.getCity());
    Assert.assertEquals(AUX_DOCTOR_ADDRESS, doctor.getAddress());
    Assert.assertEquals(AUX_DOCTOR_INSURANCES, doctor.getHealthInsurances());
    Assert.assertEquals(AUX_DOCTOR_ATTENDING_HOURS, doctor.getAttendingHours());
  }

  @Test
  public void testUpdateDoctorReviews() throws DoctorNotFoundException {
    // 1.Precondiciones
    // 2. Ejercitar la class under test
    Doctor doctor =
            doctorDao.updateReviews(
                    INSERTED_DOCTOR_ID,
                    REVIEWS_FOR_DOCTOR);

    // 3. Meaningful assertions
    Assert.assertEquals(REVIEWS_FOR_DOCTOR, doctor.getReviews());
  }

  @Test
  public void testUpdateDoctorInfoDoctorNotFound() {
    // 1.Precondiciones
    // 2. Ejercitar la class under test

    assertThrows(
        DoctorNotFoundException.class,
        () ->
            doctorDao.updateDoctorInfo(
                AUX_DOCTOR_ID,
                AUX_DOCTOR_SPECIALTY,
                AUX_DOCTOR_CITY,
                AUX_DOCTOR_ADDRESS,
                AUX_DOCTOR_INSURANCES,
                AUX_DOCTOR_ATTENDING_HOURS));
  }

  @Test
  public void testGetDoctorById() {
    // 1.Precondiciones
    Doctor expectedDoctor = DOCTOR_7;
    List<Review> reviewsForDoctor = new ArrayList<>(
            Arrays.asList(
                    new Review(7L, expectedDoctor, PATIENT_5, LocalDate.of(2023, 5, 17), "Muy buen doctor", (short) 5),
                    new Review(8L, expectedDoctor, PATIENT_5, LocalDate.of(2023, 5, 16), "Buen doctor", (short) 4),
                    new Review(9L, expectedDoctor, PATIENT_5, LocalDate.of(2023, 5, 15), "Regular doctor", (short) 3),
                    new Review(10L, expectedDoctor, PATIENT_5, LocalDate.of(2023, 5, 14), "Malo doctor", (short) 2),
                    new Review(11L, expectedDoctor, PATIENT_5, LocalDate.of(2023, 5, 13), "Muy malo doctor", (short) 1)
            )
    );
    expectedDoctor.setReviews(reviewsForDoctor);
    // 2. Ejercitar la class under test
    Optional<Doctor> maybeDoctor = doctorDao.getDoctorById(INSERTED_DOCTOR_ID);
    // 3. Meaningful assertions
    Assert.assertTrue(maybeDoctor.isPresent());
    Assert.assertEquals(expectedDoctor, maybeDoctor.get());
  }

  @Test
  public void testGetDoctorByIdDoctorDoesNotExist() {
    // 1.Precondiciones
    // 2. Ejercitar la class under test
    Optional<Doctor> maybeDoctor = doctorDao.getDoctorById(AUX_DOCTOR_ID);
    // 3. Meaningful assertions
    Assert.assertFalse(maybeDoctor.isPresent());
  }

  @Test
  public void testGetFilteredDoctors() {
    // 1.Precondiciones
    Doctor expectedDoctor = DOCTOR_7;
    List<Review> reviewsForDoctor = new ArrayList<>(
            Arrays.asList(
                    new Review(7L, expectedDoctor, PATIENT_5, LocalDate.of(2023, 5, 17), "Muy buen doctor", (short) 5),
                    new Review(8L, expectedDoctor, PATIENT_5, LocalDate.of(2023, 5, 16), "Buen doctor", (short) 4),
                    new Review(9L, expectedDoctor, PATIENT_5, LocalDate.of(2023, 5, 15), "Regular doctor", (short) 3),
                    new Review(10L, expectedDoctor, PATIENT_5, LocalDate.of(2023, 5, 14), "Malo doctor", (short) 2),
                    new Review(11L, expectedDoctor, PATIENT_5, LocalDate.of(2023, 5, 13), "Muy malo doctor", (short) 1)
            )
    );
    expectedDoctor.setReviews(reviewsForDoctor);
    // 2. Ejercitar la class under test
    Page<Doctor> doctors =
        doctorDao.getFilteredDoctors(null, null, null, null, null, null, null, null, null);
    // 3. Meaningful assertions
    Assert.assertNull(doctors.getTotalPages());
    Assert.assertNull(doctors.getCurrentPage());
    Assert.assertEquals(1, doctors.getContent().size());
    Assert.assertEquals(expectedDoctor, doctors.getContent().get(0));
  }

  @Test
  public void testGetDoctors() {
    // 1.Precondiciones
    // 2. Ejercitar la class under test
    List<Doctor> doctors = doctorDao.getDoctors();
    // 3. Meaningful assertions
    Assert.assertEquals(1, doctors.size());
    Assert.assertEquals(INSERTED_DOCTOR_ID, doctors.get(0).getId());
    Assert.assertEquals(INSERTED_DOCTOR_EMAIL, doctors.get(0).getEmail());
    Assert.assertEquals(INSERTED_DOCTOR_PASSWORD, doctors.get(0).getPassword());
    Assert.assertEquals(INSERTED_DOCTOR_FIRST_NAME, doctors.get(0).getFirstName());
    Assert.assertEquals(INSERTED_DOCTOR_LAST_NAME, doctors.get(0).getLastName());
    Assert.assertEquals(INSERTED_DOCTOR_IMAGE, doctors.get(0).getImage());
    Assert.assertEquals(INSERTED_DOCTOR_INSURANCES, doctors.get(0).getHealthInsurances());
    Assert.assertEquals(INSERTED_DOCTOR_ADDRESS, doctors.get(0).getAddress());
    Assert.assertEquals(INSERTED_DOCTOR_CITY, doctors.get(0).getCity());
    Assert.assertEquals(INSERTED_DOCTOR_SPECIALTY, doctors.get(0).getSpecialty());
    Assert.assertEquals(INSERTED_DOCTOR_ATTENDING_HOURS, doctors.get(0).getAttendingHours());
  }

  @Test
  public void testGetUsedHealthInsurances() {
    // 1.Precondiciones
    // 2. Ejercitar la class under test
    Map<HealthInsurance, Integer> healthInsurances = doctorDao.getUsedHealthInsurances();
    // 3. Meaningful assertions
    Assert.assertEquals(2, healthInsurances.size());
    Assert.assertEquals((Integer) 1, healthInsurances.get(HealthInsurance.OSDE));
    Assert.assertEquals((Integer) 1, healthInsurances.get(HealthInsurance.OMINT));
  }

  @Test
  public void testGetUsedSpecialties() {
    // 1.Precondiciones
    // 2. Ejercitar la class under test
    Map<Specialty, Integer> specialties = doctorDao.getUsedSpecialties();
    // 3. Meaningful assertions
    Assert.assertEquals(1, specialties.size());
    Assert.assertEquals((Integer) 1, specialties.get(Specialty.PEDIATRIC_ALLERGY_AND_IMMUNOLOGY));
  }

  @Test
  public void testGetUsedCities() {
    // 1.Precondiciones
    // 2. Ejercitar la class under test
    Map<City, Integer> cities = doctorDao.getUsedCities();
    // 3. Meaningful assertions
    Assert.assertEquals(1, cities.size());
    Assert.assertEquals((Integer) 1, cities.get(City.ADOLFO_GONZALES_CHAVES));
  }

  @Test
  public void testCreateReview() throws DoctorNotFoundException{
    // 1. Precondiciones
    Review review_aux = new Review(null, DOCTOR_7, PATIENT_5, DATE, DESCRIPTION, RATING);
    // 2. Ejercitar la class under test
    Doctor doctor = doctorDao.updateReviews(
        INSERTED_DOCTOR_ID,
        Collections.singletonList(review_aux));

    em.flush();

    Optional<Review> review = doctor.getReviews().stream().findFirst();

    // 3. Meaningful assertions
    Assert.assertTrue(review.isPresent());
    Assert.assertEquals(INSERTED_PATIENT_ID, review.get().getPatient().getId());
    Assert.assertEquals(RATING, review.get().getRating());
    Assert.assertEquals(DATE, review.get().getDate());
    Assert.assertEquals(DESCRIPTION, review.get().getDescription());

    // TODO: assert the expected columns
  }

  @Test
  public void testGetReviewsForDoctor() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Page<Review> reviews = doctorDao.getReviewsForDoctor(INSERTED_DOCTOR_ID, null, null);

    // 3. Meaningful assertions
    Assert.assertEquals(INSERTED_REVIEWS, reviews.getContent().size());
    Assert.assertNull(reviews.getTotalPages());
    Assert.assertNull(reviews.getCurrentPage());
  }

  @Test
  public void testGetReviewsForNonExistingDoctor() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Page<Review> reviews = doctorDao.getReviewsForDoctor(NON_EXISTING_DOCTOR_ID, null, null);

    // 3. Meaningful assertions
    Assert.assertEquals(0, reviews.getContent().size());
    Assert.assertNull(reviews.getTotalPages());
    Assert.assertNull(reviews.getCurrentPage());
  }
}
