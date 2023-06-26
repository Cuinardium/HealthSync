package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.DoctorDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.EmailAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.VacationCollisionException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.VacationNotFoundException;
import ar.edu.itba.paw.interfaces.services.MailService;
import ar.edu.itba.paw.interfaces.services.TokenService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.VacationInvalidException;
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
  private static final Locale LOCALE = new Locale("en");
  private static final Specialty SPECIALTY = Specialty.CARDIOLOGY;
  private static final String CITY = "Ayacucho";
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

  private static final Vacation VACATION =
      new Vacation(
          ID,
          LocalDate.of(2020, 1, 1),
          ThirtyMinuteBlock.BLOCK_00_00,
          LocalDate.of(2020, 1, 10),
          ThirtyMinuteBlock.BLOCK_00_00);

  private static final Float RATING = 3F;
  private static final Integer RATING_COUNT = 1;
  private static final User USER =
      new User(
          ID,
          EMAIL,
          PASSWORD_ENCODED,
          FIRST_NAME,
          LAST_NAME,
          IMAGE,
          LOCALE,
          true);
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
          Collections.emptySet(),
          RATING,
          RATING_COUNT,
          LOCALE,
          true);

  private static final Doctor DOCTOR_WITH_VACATIONS =
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
          new HashSet<>(Arrays.asList(VACATION)),
          RATING,
          RATING_COUNT,
          LOCALE,
          true);

  private static final String EMAIL_NEW = "new_email";
  private static final String FIRST_NAME_NEW = "new_fist_name";
  private static final String LAST_NAME_NEW = "new_last_name";
  private static final Set<HealthInsurance> HEALTH_INSURANCES_NEW =
      new HashSet<>(Arrays.asList(HealthInsurance.NONE, HealthInsurance.SWISS_MEDICAL));
  private static final Specialty SPECIALTY_NEW = Specialty.ALLERGY_AND_IMMUNOLOGY;
  private static final String CITY_NEW = "Arrecifes";
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

  private static final Locale LOCALE_NEW = new Locale("es");

  private static final Vacation VACATION_NEW =
      new Vacation(
          ID,
          LocalDate.now().plusDays(1),
          ThirtyMinuteBlock.BLOCK_00_00,
          LocalDate.now().plusDays(10),
          ThirtyMinuteBlock.BLOCK_00_00);

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
          Collections.emptySet(),
          RATING,
          RATING_COUNT,
          LOCALE,
          true);

  @Mock private DoctorDao doctorDao;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private UserService userService;
  @Mock private MailService mailService;
  @Mock private TokenService tokenService;

  @InjectMocks private DoctorServiceImpl ds;

  // ====================== Create doctor ======================

  @Test
  public void testCreateDoctor()
      throws DoctorAlreadyExistsException, EmailAlreadyExistsException, EmailInUseException {
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
                    Collections.emptySet(),
                    null,
                    null,
                    LOCALE,
                    false)))
        .thenReturn(DOCTOR);
    Mockito.when(userService.getUserById(Mockito.eq(ID))).thenReturn(Optional.of(USER));
    Mockito.when(tokenService.createToken(Mockito.any(User.class)))
        .thenReturn(Mockito.mock(VerificationToken.class));
    Mockito.doNothing()
        .when(mailService)
        .sendConfirmationMail(Mockito.any(VerificationToken.class));

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
            ATTENDING_HOURS,
            LOCALE);

    // 3. Meaningful assertions
    Assert.assertEquals(DOCTOR, doctor);
  }

  @Test(expected = IllegalStateException.class)
  public void testCreateDoctorAlreadyExists()
      throws DoctorAlreadyExistsException, EmailAlreadyExistsException, EmailInUseException {
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
                    Collections.emptySet(),
                    null,
                    null,
                    LOCALE,
                    false)))
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
        ATTENDING_HOURS,
        LOCALE);
  }

  @Test(expected = IllegalStateException.class)
  public void testCreateDoctorUserAlreadyExists()
      throws DoctorAlreadyExistsException, EmailAlreadyExistsException, EmailInUseException {

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
                    Collections.emptySet(),
                    null,
                    null,
                    LOCALE,
                    false)))
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
        ATTENDING_HOURS,
        LOCALE);
  }

  @Test(expected = EmailInUseException.class)
  public void testCreateDoctorEmailInUse()
      throws DoctorAlreadyExistsException, EmailAlreadyExistsException, EmailInUseException {

    // 1. Precondiciones
    Mockito.when(userService.getUserByEmail(EMAIL))
        .thenReturn(Optional.of(Mockito.mock(User.class)));

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
        ATTENDING_HOURS,
        LOCALE);
  }

  // ======================== Update doctor ========================

  @Test
  public void testUpdateDoctor()
      throws DoctorNotFoundException, EmailInUseException,
          ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException {
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
            IMAGE,
            LOCALE_NEW);

    // 3. Meaningful assertions
    Assert.assertEquals(DOCTOR_UPDATED, doctor);
  }

  @Test(expected = ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException.class)
  public void testUpdateDoctorDoesNotExist()
      throws DoctorNotFoundException, EmailInUseException,
          ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException {

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
        IMAGE,
        LOCALE_NEW);
  }

  @Test(expected = ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException.class)
  public void testUpdateDoctorUserDoesNotExist()
      throws EmailInUseException,
          ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException,
          UserNotFoundException {
    // 1. Precondiciones
    Mockito.when(
            userService.updateUser(ID, EMAIL_NEW, FIRST_NAME_NEW, LAST_NAME_NEW, IMAGE, LOCALE_NEW))
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
        IMAGE,
        LOCALE_NEW);
  }

  @Test(expected = EmailInUseException.class)
  public void testUpdateDoctorEmailInUse()
      throws EmailInUseException,
          ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException,
          UserNotFoundException {

    // 1. Precondiciones
    Mockito.when(
            userService.updateUser(ID, EMAIL_NEW, FIRST_NAME_NEW, LAST_NAME_NEW, IMAGE, LOCALE_NEW))
        .thenThrow(EmailInUseException.class);

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
        IMAGE,
        LOCALE_NEW);
  }

  // ======================== Add Vacation ========================

  @Test
  public void testAddVacation()
      throws DoctorNotFoundException,
          ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException,
          VacationInvalidException, VacationCollisionException {
    // 1. Precondiciones
    Mockito.when(doctorDao.addVacation(ID, VACATION_NEW)).thenReturn(DOCTOR_WITH_VACATIONS);

    // 2. Ejercitar la class under test
    Doctor doctor = ds.addVacation(ID, VACATION_NEW);

    // 3. Meaningful assertions
    Assert.assertEquals(DOCTOR_WITH_VACATIONS, doctor);
  }

  @Test(expected = ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException.class)
  public void testAddVacationDoesNotExist()
      throws DoctorNotFoundException,
          ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException,
          VacationInvalidException, VacationCollisionException {
    // 1. Precondiciones
    Mockito.when(doctorDao.addVacation(ID, VACATION_NEW)).thenThrow(DoctorNotFoundException.class);

    // 2. Ejercitar la class under test
    ds.addVacation(ID, VACATION_NEW);
  }

  @Test(expected = VacationInvalidException.class)
  public void testAddVacationFromAfterTo()
      throws DoctorNotFoundException,
          ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException,
          VacationInvalidException {
    // 1. Precondiciones
    Vacation invalidVacation =
        new Vacation(
            ID,
            LocalDate.now(),
            ThirtyMinuteBlock.BLOCK_00_00,
            LocalDate.now().minusDays(1),
            ThirtyMinuteBlock.BLOCK_00_00);

    // 2. Ejercitar la class under test
    ds.addVacation(ID, invalidVacation);
  }

  @Test(expected = VacationInvalidException.class)
  public void testAddVacationBeforeNow()
      throws ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException,
          VacationInvalidException {
    // 1. Precondiciones
    Vacation invalidVacation =
        new Vacation(
            ID,
            LocalDate.now().minusDays(1),
            ThirtyMinuteBlock.BLOCK_00_00,
            LocalDate.now().plusDays(1),
            ThirtyMinuteBlock.BLOCK_00_00);

    // 2. Ejercitar la class under test
    ds.addVacation(ID, invalidVacation);
  }

  @Test(expected = VacationInvalidException.class)
  public void testAddVacationCollsion()
      throws ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException,
          VacationInvalidException, DoctorNotFoundException, VacationCollisionException {
    // 1. Precondiciones
    Mockito.when(doctorDao.addVacation(ID, VACATION_NEW))
        .thenThrow(VacationCollisionException.class);

    // 2. Ejercitar la class under test
    ds.addVacation(ID, VACATION_NEW);
  }

  // ======================== Remove Vacation ========================

  @Test
  public void testRemoveVacation()
      throws DoctorNotFoundException,
          ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException,
          VacationNotFoundException,
          ar.edu.itba.paw.interfaces.services.exceptions.VacationNotFoundException {
    // 1. Precondiciones
    Mockito.when(doctorDao.removeVacation(ID, VACATION_NEW)).thenReturn(DOCTOR);

    // 2. Ejercitar la class under test
    Doctor doctor = ds.removeVacation(ID, VACATION_NEW);

    // 3. Meaningful assertions
    Assert.assertEquals(DOCTOR, doctor);
  }

  @Test(expected = ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException.class)
  public void testRemoveVacationDoctorDoesNotExist()
      throws DoctorNotFoundException,
          ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException,
          VacationNotFoundException,
          ar.edu.itba.paw.interfaces.services.exceptions.VacationNotFoundException {
    // 1. Precondiciones
    Mockito.when(doctorDao.removeVacation(ID, VACATION_NEW))
        .thenThrow(DoctorNotFoundException.class);

    // 2. Ejercitar la class under test
    ds.removeVacation(ID, VACATION_NEW);
  }

  @Test(expected = ar.edu.itba.paw.interfaces.services.exceptions.VacationNotFoundException.class)
  public void testRemoveVacationVacationDoesNotExist()
      throws DoctorNotFoundException,
          ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException,
          VacationNotFoundException,
          ar.edu.itba.paw.interfaces.services.exceptions.VacationNotFoundException {
    // 1. Precondiciones
    Mockito.when(doctorDao.removeVacation(ID, VACATION_NEW))
        .thenThrow(VacationNotFoundException.class);

    // 2. Ejercitar la class under test
    ds.removeVacation(ID, VACATION_NEW);
  }

  // ======================== Get doctor by id ========================

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
}
