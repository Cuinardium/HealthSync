package ar.edu.itba.paw.persistence;

import static org.junit.Assert.assertThrows;

import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.EmailAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.VacationCollisionException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.VacationNotFoundException;
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
  private static final String INSERTED_DOCTOR_CITY = "Adolfo Gonzalez Chaves";
  private static final String INSERTED_DOCTOR_ADDRESS = "doctor_address";
  private static final Set<AttendingHours> INSERTED_DOCTOR_ATTENDING_HOURS =
      new HashSet<>(
          Arrays.asList(
              new AttendingHours(
                  INSERTED_DOCTOR_ID, DayOfWeek.MONDAY, ThirtyMinuteBlock.BLOCK_00_00),
              new AttendingHours(
                  INSERTED_DOCTOR_ID, DayOfWeek.TUESDAY, ThirtyMinuteBlock.BLOCK_00_00),
              new AttendingHours(
                  INSERTED_DOCTOR_ID, DayOfWeek.WEDNESDAY, ThirtyMinuteBlock.BLOCK_00_00),
              new AttendingHours(
                  INSERTED_DOCTOR_ID, DayOfWeek.THURSDAY, ThirtyMinuteBlock.BLOCK_00_00),
              new AttendingHours(
                  INSERTED_DOCTOR_ID, DayOfWeek.FRIDAY, ThirtyMinuteBlock.BLOCK_00_00)));

  private static final Vacation VACATION_NEW =
      new Vacation(
          INSERTED_DOCTOR_ID,
          LocalDate.of(2020, 3, 1),
          ThirtyMinuteBlock.BLOCK_00_00,
          LocalDate.of(2020, 3, 10),
          ThirtyMinuteBlock.BLOCK_00_00);

  private static final Vacation VACATION_TO_REMOVE =
      new Vacation(
          INSERTED_DOCTOR_ID,
          LocalDate.of(2020, 1, 1),
          ThirtyMinuteBlock.BLOCK_00_00,
          LocalDate.of(2020, 1, 10),
          ThirtyMinuteBlock.BLOCK_00_00);

  private static final Set<Vacation> INSERTED_DOCTOR_VACATIONS =
      new HashSet<>(
          Arrays.asList(
              VACATION_TO_REMOVE,
              new Vacation(
                  INSERTED_DOCTOR_ID,
                  LocalDate.of(2020, 2, 1),
                  ThirtyMinuteBlock.BLOCK_00_00,
                  LocalDate.of(2020, 2, 10),
                  ThirtyMinuteBlock.BLOCK_00_00)));

  private static final Image INSERTED_DOCTOR_IMAGE = null;
  private static final Locale INSERTED_LOCALE = new Locale("en");

  private static final Float INSERTED_DOCTOR_RATING = 3f;
  private static final Integer INSERTED_DOCTOR_RATING_COUNT = 5;

  private static final Long AUX_DOCTOR_ID = 8L;
  private static final String AUX_DOCTOR_EMAIL = "notdoctor_1@email.com";
  private static final String AUX_DOCTOR_PASSWORD = "notdoctor_password";
  private static final String AUX_DOCTOR_FIRST_NAME = "notdoctor_first_name";
  private static final String AUX_DOCTOR_LAST_NAME = "notdoctor_last_name";
  private static final Set<HealthInsurance> AUX_DOCTOR_INSURANCES =
      new HashSet<>(
          Arrays.asList(HealthInsurance.NONE, HealthInsurance.SWISS_MEDICAL, HealthInsurance.OSDE));
  private static final Specialty AUX_DOCTOR_SPECIALTY = Specialty.NEPHROLOGY;
  private static final String AUX_DOCTOR_CITY = "Caseros";
  private static final String AUX_DOCTOR_ADDRESS = "notdoctor_address";

  private static final Set<AttendingHours> AUX_DOCTOR_ATTENDING_HOURS =
      new HashSet<>(
          Arrays.asList(
              new AttendingHours(
                  INSERTED_DOCTOR_ID, DayOfWeek.MONDAY, ThirtyMinuteBlock.BLOCK_00_00),
              new AttendingHours(
                  INSERTED_DOCTOR_ID, DayOfWeek.TUESDAY, ThirtyMinuteBlock.BLOCK_02_00),
              new AttendingHours(
                  INSERTED_DOCTOR_ID, DayOfWeek.WEDNESDAY, ThirtyMinuteBlock.BLOCK_02_00),
              new AttendingHours(
                  INSERTED_DOCTOR_ID, DayOfWeek.THURSDAY, ThirtyMinuteBlock.BLOCK_02_00),
              new AttendingHours(
                  INSERTED_DOCTOR_ID, DayOfWeek.FRIDAY, ThirtyMinuteBlock.BLOCK_02_00),
              new AttendingHours(
                  INSERTED_DOCTOR_ID, DayOfWeek.SATURDAY, ThirtyMinuteBlock.BLOCK_02_00),
              new AttendingHours(
                  INSERTED_DOCTOR_ID, DayOfWeek.SUNDAY, ThirtyMinuteBlock.BLOCK_02_00)));

  private static final Locale AUX_LOCALE = new Locale("en");

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
          INSERTED_DOCTOR_VACATIONS,
          INSERTED_DOCTOR_RATING,
          INSERTED_DOCTOR_RATING_COUNT,
          INSERTED_LOCALE);

  @Autowired private DataSource ds;

  private JdbcTemplate jdbcTemplate;

  @PersistenceContext private EntityManager em;

  @Autowired private DoctorDaoJpa doctorDao;

  @Before
  public void setUp() {
    jdbcTemplate = new JdbcTemplate(ds);
  }

  // ============================== createDoctor ==============================

  @Test
  public void testCreateDoctor() throws DoctorAlreadyExistsException, EmailAlreadyExistsException {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Doctor doctor =
        doctorDao.createDoctor(
            new Doctor(
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
                Collections.emptySet(),
                INSERTED_DOCTOR_RATING,
                INSERTED_DOCTOR_RATING_COUNT,
                INSERTED_LOCALE));

    em.flush();

    // 3. Meaningful assertions
    // Assert.assertEquals(AUX_DOCTOR_ID, doctor.getId());
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
    Assert.assertEquals(AUX_LOCALE, doctor.getLocale());

    Assert.assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "doctor"));
  }

  @Test
  public void testCreateDoctorAlreadyExists() {
    // 1.Precondiciones
    // 2. Ejercitar la class under test
    assertThrows(
        DoctorAlreadyExistsException.class,
        () ->
            doctorDao.createDoctor(
                new Doctor(
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
                    Collections.emptySet(),
                    INSERTED_DOCTOR_RATING,
                    INSERTED_DOCTOR_RATING_COUNT,
                    INSERTED_LOCALE)));
  }

  // ============================== updateDoctorInfo ==============================

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
    Assert.assertEquals(INSERTED_LOCALE, doctor.getLocale());
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

  // ============================== addVacation ================================

  @Test
  public void testAddVacation() throws DoctorNotFoundException, VacationCollisionException {
    // 1.Precondiciones
    Set<Vacation> expectedVacations = new HashSet<>(DOCTOR_7.getVacations());
    expectedVacations.add(VACATION_NEW);

    // 2. Ejercitar la class under test
    Doctor doctor = doctorDao.addVacation(INSERTED_DOCTOR_ID, VACATION_NEW);

    // 3. Meaningful assertions
    Assert.assertEquals(expectedVacations, doctor.getVacations());
  }

  @Test
  public void testAddVacationDoctorNotFound() {
    // 1.Precondiciones
    // 2. Ejercitar la class under test
    assertThrows(
        DoctorNotFoundException.class, () -> doctorDao.addVacation(AUX_DOCTOR_ID, VACATION_NEW));
  }

  @Test
  public void testAddVacationCollision() {
    // 1.Precondiciones
    // 2. Ejercitar la class under test
    assertThrows(
        VacationCollisionException.class,
        () -> doctorDao.addVacation(INSERTED_DOCTOR_ID, DOCTOR_7.getVacations().iterator().next()));
  }

  // ============================== removeVacation ================================

  @Test
  public void testRemoveVacation() throws DoctorNotFoundException, VacationNotFoundException {
    // 1.Precondiciones
    Set<Vacation> expectedVacations = new HashSet<>(DOCTOR_7.getVacations());
    expectedVacations.remove(VACATION_TO_REMOVE);

    // 2. Ejercitar la class under test
    Doctor doctor = doctorDao.removeVacation(INSERTED_DOCTOR_ID, VACATION_TO_REMOVE);

    // 3. Meaningful assertions
    Assert.assertEquals(expectedVacations, doctor.getVacations());
  }

  @Test
  public void testRemoveVacationDoctorNotFound() {
    // 1.Precondiciones
    // 2. Ejercitar la class under test
    assertThrows(
        DoctorNotFoundException.class,
        () -> doctorDao.removeVacation(AUX_DOCTOR_ID, VACATION_TO_REMOVE));
  }

  @Test
  public void testRemoveVacationVacationNotFound() {
    // 1.Precondiciones
    // 2. Ejercitar la class under test
    assertThrows(
        VacationNotFoundException.class,
        () -> doctorDao.removeVacation(INSERTED_DOCTOR_ID, VACATION_NEW));
  }

  // ============================== getDoctorById ==============================

  @Test
  public void testGetDoctorById() {
    // 1.Precondiciones
    Doctor expectedDoctor = DOCTOR_7;

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

  // ============================== getFilteredDoctors ==============================

  @Test
  public void testGetFilteredDoctorsNoFilters() {
    // 1.Precondiciones
    Doctor expectedDoctor = DOCTOR_7;

    // 2. Ejercitar la class under test
    Page<Doctor> doctors =
        doctorDao.getFilteredDoctors(null, null, null, null, null, null, null, null, null, null);

    // 3. Meaningful assertions
    Assert.assertNull(doctors.getTotalPages());
    Assert.assertNull(doctors.getCurrentPage());
    Assert.assertEquals(1, doctors.getContent().size());
    Assert.assertEquals(expectedDoctor, doctors.getContent().get(0));
  }

  @Test
  public void testGetFilteredDoctors() {
    // 1.Precondiciones
    Doctor expectedDoctor = DOCTOR_7;

    LocalDate friday = LocalDate.of(2023, 6, 9);

    // 2. Ejercitar la class under test
    Page<Doctor> doctors =
        doctorDao.getFilteredDoctors(
            null,
            friday,
            ThirtyMinuteBlock.BLOCK_00_00,
            ThirtyMinuteBlock.BLOCK_00_00,
            INSERTED_DOCTOR_SPECIALTY,
            INSERTED_DOCTOR_CITY,
            HealthInsurance.OSDE,
            INSERTED_DOCTOR_RATING.intValue(),
            null,
            null);

    // 3. Meaningful assertions
    Assert.assertNull(doctors.getTotalPages());
    Assert.assertNull(doctors.getCurrentPage());
    Assert.assertEquals(1, doctors.getContent().size());
    Assert.assertEquals(expectedDoctor, doctors.getContent().get(0));
  }

  @Test
  public void testGetFilteredDoctorsOnVacation() {
    // 1.Precondiciones
    LocalDate mondayOnVacation = LocalDate.of(2020, 1, 11);

    // 2. Ejercitar la class under test
    Page<Doctor> doctors =
        doctorDao.getFilteredDoctors(
            null,
            mondayOnVacation,
            ThirtyMinuteBlock.BLOCK_00_00,
            ThirtyMinuteBlock.BLOCK_00_00,
            INSERTED_DOCTOR_SPECIALTY,
            INSERTED_DOCTOR_CITY,
            HealthInsurance.OSDE,
            INSERTED_DOCTOR_RATING.intValue(),
            null,
            null);

    // 3. Meaningful assertions
    Assert.assertNull(doctors.getTotalPages());
    Assert.assertNull(doctors.getCurrentPage());
    Assert.assertEquals(0, doctors.getContent().size());
  }

  // ============================== getDoctors ==============================

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
    Assert.assertEquals(INSERTED_LOCALE, doctors.get(0).getLocale());
  }

  // ============================== getUsedHealthInsurances ==============================

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

  // ============================== getUsedSpecialties ==============================

  @Test
  public void testGetUsedSpecialties() {
    // 1.Precondiciones
    // 2. Ejercitar la class under test
    Map<Specialty, Integer> specialties = doctorDao.getUsedSpecialties();
    // 3. Meaningful assertions
    Assert.assertEquals(1, specialties.size());
    Assert.assertEquals((Integer) 1, specialties.get(Specialty.PEDIATRIC_ALLERGY_AND_IMMUNOLOGY));
  }

  // ============================== getUsedCities ==============================

  @Test
  public void testGetUsedCities() {
    // 1.Precondiciones
    // 2. Ejercitar la class under test
    Map<String, Integer> cities = doctorDao.getUsedCities();
    // 3. Meaningful assertions
    Assert.assertEquals(1, cities.size());
    Assert.assertEquals((Integer) 1, cities.get("Adolfo Gonzalez Chaves"));
  }

  @Test
  public void testGetPopularSpecialties() {
    // 1.Precondiciones
    // 2. Ejercitar la class under test
    List<Specialty> specialties = doctorDao.getPopularSpecialties();
    // 3. Meaningful assertions
    Assert.assertEquals(1, specialties.size());
    Assert.assertEquals(Specialty.PEDIATRIC_ALLERGY_AND_IMMUNOLOGY, specialties.get(0));
  }
}
