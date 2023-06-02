package ar.edu.itba.paw.persistence;

import static org.junit.Assert.assertThrows;

import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.models.AttendingHours;
import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.persistence.config.TestConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

  private static final long INSERTED_DOCTOR_ID = 3;
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
  private static final Long INSERTED_DOCTOR_PFP_ID = null;

  private static final Float INSERTED_DOCTOR_RATING = null;
  private static final Integer INSERTED_DOCTOR_RATING_COUNT = 0;

  private static final long AUX_DOCTOR_ID = 4;
  private static final String AUX_DOCTOR_EMAIL = "notdoctor@email.com";
  private static final String AUX_DOCTOR_PASSWORD = "notdoctor_password";
  private static final String AUX_DOCTOR_FIRST_NAME = "notdoctor_first_name";
  private static final String AUX_DOCTOR_LAST_NAME = "notdoctor_last_name";
  private static final List<HealthInsurance> AUX_DOCTOR_INSURANCES =
      Arrays.asList(HealthInsurance.NONE, HealthInsurance.SWISS_MEDICAL);
  private static final Specialty AUX_DOCTOR_SPECIALTY = Specialty.NEPHROLOGY;
  private static final City AUX_DOCTOR_CITY = City.CASEROS;
  private static final String AUX_DOCTOR_ADDRESS = "notdoctor_address";
  private static final List<ThirtyMinuteBlock> attendingHoursForDay =
      Arrays.asList(ThirtyMinuteBlock.BLOCK_02_00);
  private static final AttendingHours AUX_DOCTOR_ATTENDING_HOURS =
      new AttendingHours(
          attendingHoursForDay,
          attendingHoursForDay,
          attendingHoursForDay,
          attendingHoursForDay,
          attendingHoursForDay,
          attendingHoursForDay,
          attendingHoursForDay);

  private static final Location LOCATION_FOR_DOCTOR_3 =
      new Location(1, INSERTED_DOCTOR_CITY, INSERTED_DOCTOR_ADDRESS);
  private static final Doctor DOCTOR_3 =
      new Doctor(
          INSERTED_DOCTOR_ID,
          INSERTED_DOCTOR_EMAIL,
          INSERTED_DOCTOR_PASSWORD,
          INSERTED_DOCTOR_FIRST_NAME,
          INSERTED_DOCTOR_LAST_NAME,
          INSERTED_DOCTOR_PFP_ID,
          INSERTED_DOCTOR_INSURANCES,
          INSERTED_DOCTOR_SPECIALTY,
          LOCATION_FOR_DOCTOR_3,
          INSERTED_DOCTOR_ATTENDING_HOURS, 
          INSERTED_DOCTOR_RATING,
          INSERTED_DOCTOR_RATING_COUNT);

  @Autowired private DataSource ds;

  private JdbcTemplate jdbcTemplate;

  @Autowired private DoctorDaoImpl doctorDao;

  @Before
  public void setUp() {
    jdbcTemplate = new JdbcTemplate(ds);
  }

  @Test
  public void testCreateDoctor() throws DoctorAlreadyExistsException {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Doctor doctor =
        doctorDao.createDoctor(
            AUX_DOCTOR_ID,
            AUX_DOCTOR_SPECIALTY,
            AUX_DOCTOR_CITY,
            AUX_DOCTOR_ADDRESS,
            AUX_DOCTOR_INSURANCES,
            AUX_DOCTOR_ATTENDING_HOURS);
    // 3. Meaningful assertions
    Assert.assertEquals(AUX_DOCTOR_ID, doctor.getId());
    Assert.assertEquals(AUX_DOCTOR_EMAIL, doctor.getEmail());
    Assert.assertEquals(AUX_DOCTOR_PASSWORD, doctor.getPassword());
    Assert.assertEquals(AUX_DOCTOR_FIRST_NAME, doctor.getFirstName());
    Assert.assertEquals(AUX_DOCTOR_LAST_NAME, doctor.getLastName());
    Assert.assertEquals(INSERTED_DOCTOR_PFP_ID, doctor.getProfilePictureId());
    Assert.assertEquals(AUX_DOCTOR_INSURANCES, doctor.getHealthInsurances());
    Assert.assertEquals(AUX_DOCTOR_ADDRESS, doctor.getLocation().getAddress());
    Assert.assertEquals(AUX_DOCTOR_CITY, doctor.getLocation().getCity());
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
            doctorDao.createDoctor(
                INSERTED_DOCTOR_ID,
                AUX_DOCTOR_SPECIALTY,
                AUX_DOCTOR_CITY,
                AUX_DOCTOR_ADDRESS,
                AUX_DOCTOR_INSURANCES,
                AUX_DOCTOR_ATTENDING_HOURS));
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
    Assert.assertEquals(INSERTED_DOCTOR_PFP_ID, doctor.getProfilePictureId());
    Assert.assertEquals(AUX_DOCTOR_SPECIALTY, doctor.getSpecialty());
    Assert.assertEquals(AUX_DOCTOR_CITY, doctor.getLocation().getCity());
    Assert.assertEquals(AUX_DOCTOR_ADDRESS, doctor.getLocation().getAddress());
    Assert.assertEquals(AUX_DOCTOR_INSURANCES, doctor.getHealthInsurances());
    Assert.assertEquals(AUX_DOCTOR_ATTENDING_HOURS, doctor.getAttendingHours());
  }

  @Test
  public void testUpdateDoctorInfoDoctorNotFound() throws DoctorNotFoundException {
    // 1.Precondiciones
    // 2. Ejercitar la class under test
    
    assertThrows(DoctorNotFoundException.class,
    () -> doctorDao.updateDoctorInfo(
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
    // 2. Ejercitar la class under test
    Optional<Doctor> maybeDoctor = doctorDao.getDoctorById(INSERTED_DOCTOR_ID);
    // 3. Meaningful assertions
    Assert.assertTrue(maybeDoctor.isPresent());
    Assert.assertEquals(DOCTOR_3, maybeDoctor.get());
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
    // 2. Ejercitar la class under test
    Page<Doctor> doctors = doctorDao.getFilteredDoctors(null, null, null, null, null, null, null, null, null);
    // 3. Meaningful assertions
    Assert.assertNull(doctors.getTotalPages());
    Assert.assertNull(doctors.getCurrentPage());
    Assert.assertEquals(1, doctors.getContent().size());
    Assert.assertEquals(DOCTOR_3, doctors.getContent().get(0));
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
    Assert.assertEquals(INSERTED_DOCTOR_PFP_ID, doctors.get(0).getProfilePictureId());
    Assert.assertEquals(INSERTED_DOCTOR_INSURANCES, doctors.get(0).getHealthInsurances());
    Assert.assertEquals(INSERTED_DOCTOR_ADDRESS, doctors.get(0).getLocation().getAddress());
    Assert.assertEquals(INSERTED_DOCTOR_CITY, doctors.get(0).getLocation().getCity());
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
}