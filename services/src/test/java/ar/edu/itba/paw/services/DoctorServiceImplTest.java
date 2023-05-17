package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.DoctorDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.AttendingHours;
import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.models.User;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
public class DoctorServiceImplTest {

  private static final long ID = 0;
  private static final String EMAIL = "email";
  private static final String PASSWORD = "password";
  private static final String FIRST_NAME = "first_name";
  private static final String LAST_NAME = "last_name";
  private static final List<HealthInsurance> HEALTH_INSURANCES =
      Arrays.asList(HealthInsurance.OSDE, HealthInsurance.OMINT);
  private static final Long PFP_ID = null;
  private static final Specialty SPECIALTY = Specialty.CARDIOLOGY;
  private static final City CITY = City.AYACUCHO;
  private static final String ADDRESS = "1234";
  private static final Location LOCATION = new Location(1, CITY, ADDRESS);
  private static final List<ThirtyMinuteBlock> attendingHoursForDay =
      Arrays.asList(ThirtyMinuteBlock.BLOCK_00_30);
  private static final AttendingHours ATTENDING_HOURS =
      new AttendingHours(
          attendingHoursForDay,
          attendingHoursForDay,
          attendingHoursForDay,
          attendingHoursForDay,
          attendingHoursForDay,
          attendingHoursForDay,
          attendingHoursForDay);
  private static final Float RATING = 3F;
  private static final Integer RATING_COUNT = 1;
  private static final User USER = new User(ID, EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, PFP_ID);
  private static final Doctor DOCTOR =
      new Doctor(
          ID,
          EMAIL,
          PASSWORD,
          FIRST_NAME,
          LAST_NAME,
          PFP_ID,
          HEALTH_INSURANCES,
          SPECIALTY,
          LOCATION,
          ATTENDING_HOURS,
          RATING,
          RATING_COUNT);

  private static final String EMAIL_NEW = "new_email";
  private static final String FIRST_NAME_NEW = "new_fist_name";
  private static final String LAST_NAME_NEW = "new_last_name";
  private static final Image IMAGE = new Image(new byte[0]);
  private static final List<HealthInsurance> HEALTH_INSURANCES_NEW =
      Arrays.asList(HealthInsurance.NONE, HealthInsurance.SWISS_MEDICAL);
  private static final Specialty SPECIALTY_NEW = Specialty.ALLERGY_AND_IMMUNOLOGY;
  private static final City CITY_NEW = City.ARRECIFES;
  private static final String ADDRESS_NEW = "1234asdsa";
  private static final Location LOCATION_NEW = new Location(2, CITY_NEW, ADDRESS_NEW);
  private static final List<ThirtyMinuteBlock> attendingHoursForDayNew =
      Arrays.asList(ThirtyMinuteBlock.BLOCK_00_30);
  private static final AttendingHours ATTENDING_HOURS_NEW =
      new AttendingHours(
          attendingHoursForDayNew,
          attendingHoursForDayNew,
          attendingHoursForDayNew,
          attendingHoursForDayNew,
          attendingHoursForDayNew,
          attendingHoursForDayNew,
          attendingHoursForDayNew);
  private static final Doctor DOCTOR_UPDATED =
      new Doctor(
          ID,
          EMAIL_NEW,
          PASSWORD,
          FIRST_NAME_NEW,
          LAST_NAME_NEW,
          PFP_ID,
          HEALTH_INSURANCES_NEW,
          SPECIALTY_NEW,
          LOCATION_NEW,
          ATTENDING_HOURS_NEW,
          RATING,
          RATING_COUNT);

  @Mock private DoctorDao doctorDao;
  @Mock private UserService userService;

  @InjectMocks private DoctorServiceImpl ds;

  @Test
  public void testCreateDoctor()
      throws IllegalStateException, DoctorAlreadyExistsException, EmailInUseException {
    // 1. Precondiciones
    Mockito.when(userService.createUser(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME)).thenReturn(USER);
    Mockito.when(
            doctorDao.createDoctor(
                ID, SPECIALTY, CITY, ADDRESS, HEALTH_INSURANCES, ATTENDING_HOURS))
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
  public void testCreateDoctorAlreadyExists()
      throws IllegalStateException, DoctorAlreadyExistsException, EmailInUseException {
    // 1. Precondiciones
    Mockito.when(userService.createUser(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME)).thenReturn(USER);
    Mockito.when(
            doctorDao.createDoctor(
                ID, SPECIALTY, CITY, ADDRESS, HEALTH_INSURANCES, ATTENDING_HOURS))
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

  @Test(expected = EmailInUseException.class)
  public void testCreateDoctorUserAlreadyExists()
      throws IllegalStateException, DoctorAlreadyExistsException, EmailInUseException {
    // 1. Precondiciones
    Mockito.when(userService.createUser(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME))
        .thenThrow(EmailInUseException.class);
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
  public void testUpdateDoctor() throws DoctorNotFoundException, UserNotFoundException {
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

  // TODO: cng excep
  @Test(expected = RuntimeException.class)
  public void testUpdateDoctorDoesNotExist() throws DoctorNotFoundException, UserNotFoundException {
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

  @Test(expected = UserNotFoundException.class)
  public void testUpdateDoctorUserDoesNotExist()
      throws DoctorNotFoundException, UserNotFoundException {
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

  // TODO: Testear estos? (son pasamanos)
  @Test
  public void testGetFilteredDoctors() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testGetDoctors() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testGetUsedSpecialties() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testGetUsedHealthInsurances() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testGetUsedCities() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }
}
