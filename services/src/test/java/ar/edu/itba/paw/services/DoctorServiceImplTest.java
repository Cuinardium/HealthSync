package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.DoctorDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.EmailAlreadyExistsException;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

// Le permite a Mockito tomar control de JUnit y permite anotaciones que sino no estarian
// disponibles
@RunWith(MockitoJUnitRunner.class)
public class DoctorServiceImplTest {

  private static final long ID = 0;
  private static final String EMAIL = "email";
  private static final String PASSWORD = "password";
  private static final String PASSWORD_ENCODED = "password_encoded";
  private static final String FIRST_NAME = "first_name";
  private static final String LAST_NAME = "last_name";
  private static final Set<HealthInsurance> HEALTH_INSURANCES =
      new HashSet<>(Arrays.asList(HealthInsurance.OSDE, HealthInsurance.OMINT));
  private static final Image IMAGE = null;
  private static final Specialty SPECIALTY = Specialty.CARDIOLOGY;
  private static final City CITY = City.AYACUCHO;
  private static final String ADDRESS = "1234";
  private static final Set<AttendingHours> ATTENDING_HOURS =
      new HashSet<>(
          Arrays.asList(
              new AttendingHours(ID, DayOfWeek.MONDAY, ThirtyMinuteBlock.BLOCK_00_30),
              new AttendingHours(ID, DayOfWeek.TUESDAY, ThirtyMinuteBlock.BLOCK_00_30),
              new AttendingHours(ID, DayOfWeek.WEDNESDAY, ThirtyMinuteBlock.BLOCK_00_30),
              new AttendingHours(ID, DayOfWeek.THURSDAY, ThirtyMinuteBlock.BLOCK_00_30),
              new AttendingHours(ID, DayOfWeek.FRIDAY, ThirtyMinuteBlock.BLOCK_00_30),
              new AttendingHours(ID, DayOfWeek.SATURDAY, ThirtyMinuteBlock.BLOCK_00_30),
              new AttendingHours(ID, DayOfWeek.SUNDAY, ThirtyMinuteBlock.BLOCK_00_30)));
  private static final Float RATING = 3F;
  private static final Integer RATING_COUNT = 1;
  private static final Doctor DOCTOR =
      new Doctor(
          ID,
          EMAIL,
          PASSWORD_ENCODED,
          FIRST_NAME,
          LAST_NAME,
          IMAGE,
          HEALTH_INSURANCES,
          SPECIALTY,
          CITY,
          ADDRESS,
          ATTENDING_HOURS,
          new ArrayList<>(),
          RATING,
          RATING_COUNT);

  private static final String EMAIL_NEW = "new_email";
  private static final String FIRST_NAME_NEW = "new_fist_name";
  private static final String LAST_NAME_NEW = "new_last_name";
  private static final Set<HealthInsurance> HEALTH_INSURANCES_NEW =
      new HashSet<>(Arrays.asList(HealthInsurance.NONE, HealthInsurance.SWISS_MEDICAL));
  private static final Specialty SPECIALTY_NEW = Specialty.ALLERGY_AND_IMMUNOLOGY;
  private static final City CITY_NEW = City.ARRECIFES;
  private static final String ADDRESS_NEW = "1234asdsa";
  private static final Set<AttendingHours> ATTENDING_HOURS_NEW =
      new HashSet<>(
          Arrays.asList(
              new AttendingHours(ID, DayOfWeek.MONDAY, ThirtyMinuteBlock.BLOCK_00_30),
              new AttendingHours(ID, DayOfWeek.TUESDAY, ThirtyMinuteBlock.BLOCK_00_30),
              new AttendingHours(ID, DayOfWeek.WEDNESDAY, ThirtyMinuteBlock.BLOCK_00_30),
              new AttendingHours(ID, DayOfWeek.THURSDAY, ThirtyMinuteBlock.BLOCK_00_30),
              new AttendingHours(ID, DayOfWeek.FRIDAY, ThirtyMinuteBlock.BLOCK_00_30),
              new AttendingHours(ID, DayOfWeek.SATURDAY, ThirtyMinuteBlock.BLOCK_00_30),
              new AttendingHours(ID, DayOfWeek.SUNDAY, ThirtyMinuteBlock.BLOCK_00_30)));

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

  private static final List<Review> REVIEWS_FOR_DOCTOR =
      new ArrayList<>(
          Arrays.asList(
              new Review(
                  6L, DOCTOR, PATIENT_5, LocalDate.of(2023, 5, 17), "Muy buen doctor", (short) 5),
              new Review(
                  7L, DOCTOR, PATIENT_5, LocalDate.of(2023, 5, 16), "Buen doctor", (short) 4),
              new Review(
                  8L, DOCTOR, PATIENT_5, LocalDate.of(2023, 5, 15), "Regular doctor", (short) 3),
              new Review(
                  9L, DOCTOR, PATIENT_5, LocalDate.of(2023, 5, 14), "Malo doctor", (short) 2),
              new Review(
                  10L,
                  DOCTOR,
                  PATIENT_5,
                  LocalDate.of(2023, 5, 13),
                  "Muy malo doctor",
                  (short) 1)));
  private static final Doctor DOCTOR_UPDATED =
      new Doctor(
          ID,
          EMAIL_NEW,
          PASSWORD,
          FIRST_NAME_NEW,
          LAST_NAME_NEW,
          IMAGE,
          HEALTH_INSURANCES_NEW,
          SPECIALTY_NEW,
          CITY_NEW,
          ADDRESS_NEW,
          ATTENDING_HOURS_NEW,
          new ArrayList<>(),
          RATING,
          RATING_COUNT);

  private static final Doctor DOCTOR_UPDATED_REVIEWS =
      new Doctor(
          ID,
          EMAIL,
          PASSWORD,
          FIRST_NAME,
          LAST_NAME,
          IMAGE,
          HEALTH_INSURANCES,
          SPECIALTY,
          CITY,
          ADDRESS,
          ATTENDING_HOURS,
          REVIEWS_FOR_DOCTOR,
          RATING,
          RATING_COUNT);

  @Mock private DoctorDao doctorDao;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private UserService userService;

  @InjectMocks private DoctorServiceImpl ds;

  @Test
  public void testCreateDoctor() throws DoctorAlreadyExistsException, EmailAlreadyExistsException, EmailInUseException
      {
    // 1. Precondiciones
    Mockito.when(passwordEncoder.encode(Mockito.eq(PASSWORD))).thenReturn(PASSWORD_ENCODED);
    Mockito.when(
            doctorDao.createDoctor(
                new Doctor(
                    null,
                    EMAIL,
                    PASSWORD_ENCODED,
                    FIRST_NAME,
                    LAST_NAME,
                    IMAGE,
                    HEALTH_INSURANCES,
                    SPECIALTY,
                    CITY,
                    ADDRESS,
                    ATTENDING_HOURS,
                    new ArrayList<>(),
                    0f,
                    0)))
        .thenReturn(DOCTOR);
    // 2. Ejercitar la class under test
    Doctor doctor =
        ds.createDoctor(
            EMAIL,
            PASSWORD,
            FIRST_NAME,
            LAST_NAME,
            SPECIALTY,
            CITY,
            ADDRESS,
            HEALTH_INSURANCES,
            ATTENDING_HOURS);
    // 3. Meaningful assertions
    Assert.assertEquals(DOCTOR, doctor);
  }

  @Test(expected = IllegalStateException.class)
  public void testCreateDoctorAlreadyExists() throws DoctorAlreadyExistsException, EmailAlreadyExistsException, EmailInUseException
      {
    // 1. Precondiciones
    Mockito.when(passwordEncoder.encode(Mockito.eq(PASSWORD))).thenReturn(PASSWORD_ENCODED);
    Mockito.when(
            doctorDao.createDoctor(
                new Doctor(
                    null,
                    EMAIL,
                    PASSWORD_ENCODED,
                    FIRST_NAME,
                    LAST_NAME,
                    IMAGE,
                    HEALTH_INSURANCES,
                    SPECIALTY,
                    CITY,
                    ADDRESS,
                    ATTENDING_HOURS,
                    new ArrayList<>(),
                    0f,
                    0)))
        .thenThrow(DoctorAlreadyExistsException.class);
    // 2. Ejercitar la class under test
    ds.createDoctor(
        EMAIL,
        PASSWORD,
        FIRST_NAME,
        LAST_NAME,
        SPECIALTY,
        CITY,
        ADDRESS,
        HEALTH_INSURANCES,
        ATTENDING_HOURS);
    // 3. Meaningful assertions
  }

  @Test(expected = IllegalStateException.class)
  public void testCreateDoctorUserAlreadyExists() throws DoctorAlreadyExistsException, EmailAlreadyExistsException, EmailInUseException
      {
    // 1. Precondiciones
    Mockito.when(passwordEncoder.encode(Mockito.eq(PASSWORD))).thenReturn(PASSWORD_ENCODED);
    Mockito.when(
            doctorDao.createDoctor(
                new Doctor(
                    null,
                    EMAIL,
                    PASSWORD_ENCODED,
                    FIRST_NAME,
                    LAST_NAME,
                    IMAGE,
                    HEALTH_INSURANCES,
                    SPECIALTY,
                    CITY,
                    ADDRESS,
                    ATTENDING_HOURS,
                    new ArrayList<>(),
                    0f,
                    0)))
        .thenThrow(IllegalStateException.class);
    // 2. Ejercitar la class under test
    ds.createDoctor(
        EMAIL,
        PASSWORD,
        FIRST_NAME,
        LAST_NAME,
        SPECIALTY,
        CITY,
        ADDRESS,
        HEALTH_INSURANCES,
        ATTENDING_HOURS);
    // 3. Meaningful assertions
  }

  @Test
  public void testUpdateDoctor() throws DoctorNotFoundException, EmailInUseException, ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException {
    // 1. Precondiciones
    Mockito.when(
            doctorDao.updateDoctorInfo(
                ID,
                SPECIALTY_NEW,
                CITY_NEW,
                ADDRESS_NEW,
                HEALTH_INSURANCES_NEW,
                ATTENDING_HOURS_NEW))
        .thenReturn(DOCTOR_UPDATED);
    // 2. Ejercitar la class under test
    Doctor doctor =
        ds.updateDoctor(
            ID,
            EMAIL_NEW,
            FIRST_NAME_NEW,
            LAST_NAME_NEW,
            SPECIALTY_NEW,
            CITY_NEW,
            ADDRESS_NEW,
            HEALTH_INSURANCES_NEW,
            ATTENDING_HOURS_NEW,
            IMAGE);
    // 3. Meaningful assertions
    Assert.assertEquals(DOCTOR_UPDATED, doctor);
  }

  @Test
  public void testUpdateReviews() throws DoctorNotFoundException, ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException {
    // 1. Precondiciones
    Mockito.when(doctorDao.updateReviews(ID, REVIEWS_FOR_DOCTOR))
        .thenReturn(DOCTOR_UPDATED_REVIEWS);
    // 2. Ejercitar la class under test
    Doctor doctor = ds.updateReviews(ID, REVIEWS_FOR_DOCTOR);
    // 3. Meaningful assertions
    Assert.assertEquals(DOCTOR_UPDATED_REVIEWS, doctor);
  }

  @Test(expected = ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException.class)
  public void testUpdateDoctorDoesNotExist() throws DoctorNotFoundException, EmailInUseException, ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException {
    // 1. Precondiciones
    Mockito.when(
            doctorDao.updateDoctorInfo(
                ID,
                SPECIALTY_NEW,
                CITY_NEW,
                ADDRESS_NEW,
                HEALTH_INSURANCES_NEW,
                ATTENDING_HOURS_NEW))
        .thenThrow(DoctorNotFoundException.class);
    // 2. Ejercitar la class under test
    ds.updateDoctor(
        ID,
        EMAIL_NEW,
        FIRST_NAME_NEW,
        LAST_NAME_NEW,
        SPECIALTY_NEW,
        CITY_NEW,
        ADDRESS_NEW,
        HEALTH_INSURANCES_NEW,
        ATTENDING_HOURS_NEW,
        IMAGE);
    // 3. Meaningful assertions
  }

  @Test(expected = ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException.class)
  public void testUpdateDoctorUserDoesNotExist()
      throws EmailInUseException, ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException, UserNotFoundException {
    // 1. Precondiciones
    Mockito.when(userService.updateUser(ID, EMAIL_NEW, FIRST_NAME_NEW, LAST_NAME_NEW, IMAGE))
        .thenThrow(UserNotFoundException.class);
    // 2. Ejercitar la class under test
    ds.updateDoctor(
        ID,
        EMAIL_NEW,
        FIRST_NAME_NEW,
        LAST_NAME_NEW,
        SPECIALTY_NEW,
        CITY_NEW,
        ADDRESS_NEW,
        HEALTH_INSURANCES_NEW,
        ATTENDING_HOURS_NEW,
        IMAGE);
    // 3. Meaningful assertions
  }

  @Test
  public void testGetDoctorById() {
    // 1. Precondiciones
    Mockito.when(doctorDao.getDoctorById(ID)).thenReturn(Optional.of(DOCTOR));
    // 2. Ejercitar la class under test
    Optional<Doctor> doctor = ds.getDoctorById(ID);
    // 3. Meaningful assertions
    Assert.assertTrue(doctor.isPresent());
    Assert.assertEquals(DOCTOR, doctor.get());
  }

  @Test
  public void testGetDoctorByIdDoesNotExist() {
    // 1. Precondiciones
    Mockito.when(doctorDao.getDoctorById(ID)).thenReturn(Optional.empty());
    // 2. Ejercitar la class under test
    Optional<Doctor> doctor = ds.getDoctorById(ID);
    // 3. Meaningful assertions
    Assert.assertFalse(doctor.isPresent());
  }

  @Test
  public void testCreateReview() {}

  @Test
  public void testCreateReviewCanNotReview() {}

  @Test
  public void testGetReviewsForDoctor() {}

  @Test
  public void testCanReviewTrue() {}

  @Test
  public void testCanReviewFalse() {}
}
