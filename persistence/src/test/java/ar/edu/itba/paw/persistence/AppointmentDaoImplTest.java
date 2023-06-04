package ar.edu.itba.paw.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import ar.edu.itba.paw.interfaces.persistence.exceptions.AppointmentNotFoundException;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.persistence.config.TestConfig;

import java.time.LocalDate;
import java.util.*;
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
public class AppointmentDaoImplTest {

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
  private static final Image INSERTED_DOCTOR_IMAGE = null;

  private static final Float INSERTED_DOCTOR_RATING = null;
  private static final Integer INSERTED_DOCTOR_RATING_COUNT = 0;

  private static final Location LOCATION_FOR_DOCTOR_7 =
      new Location(INSERTED_DOCTOR_CITY, INSERTED_DOCTOR_ADDRESS);
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
          new HashSet<>(),
          new ArrayList<>(),
          INSERTED_DOCTOR_RATING,
          INSERTED_DOCTOR_RATING_COUNT);

  private static final ThirtyMinuteBlock INSERTED_TIME = ThirtyMinuteBlock.BLOCK_00_30;
  private static final Long INSERTED_APP_ID = 3L;
  private static final LocalDate INSERTED_LOCAL_DATE = LocalDate.of(2023, 5, 17);
  private static final AppointmentStatus INSERTED_STATUS = AppointmentStatus.CONFIRMED;
  private static final String INSERTED_DESC = "Me duele la cabeza";
  private static final String INSERTED_CANCEL_DESC = null;

  private static final LocalDate AUX_LOCAL_DATE = LocalDate.of(2023, 5, 18);
  private static final ThirtyMinuteBlock AUX_TIME = ThirtyMinuteBlock.BLOCK_06_00;
  private static final String AUX_DESC = "Revision medica";
  private static final AppointmentStatus AUX_STATUS = AppointmentStatus.CANCELLED;
  private static final String AUX_CANCEL_DESC = "Que me importa";
  private static final Long AUX_APP_ID = 5L;

  private static final Appointment APPOINTMENT_1 =
      new Appointment(
          INSERTED_APP_ID,
          PATIENT_5,
          DOCTOR_7,
          INSERTED_LOCAL_DATE,
          INSERTED_TIME,
          INSERTED_STATUS,
          INSERTED_DESC,
          INSERTED_CANCEL_DESC);

  @Autowired private DataSource ds;

  private JdbcTemplate jdbcTemplate;

  @Autowired private AppointmentDaoImpl appointmentDao;

  @Before
  public void setUp() {
    jdbcTemplate = new JdbcTemplate(ds);
  }

  @Test
  public void testCreateAppointment() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Appointment appointment =
        appointmentDao.createAppointment(PATIENT_5, DOCTOR_7, AUX_LOCAL_DATE, AUX_TIME, AUX_DESC);
    // 3. Meaninful assertions

    assertEquals(PATIENT_5, appointment.getPatient());
    assertEquals(DOCTOR_7, appointment.getDoctor());
    assertEquals(AUX_LOCAL_DATE, appointment.getDate());
    assertEquals(AUX_TIME, appointment.getTimeBlock());
    assertEquals(AUX_DESC, appointment.getDescription());

    Assert.assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, "appointment"));
  }

  // TODO: revisar
  @Test
  public void testCreateAppointmentAlreadyExists() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Appointment appointment =
        appointmentDao.createAppointment(
            PATIENT_5, DOCTOR_7, AUX_LOCAL_DATE, INSERTED_TIME, AUX_DESC);
    // 3. Meaninful assertions

    Assert.assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, "appointment"));
  }

  @Test
  public void testUpdateAppointment() throws AppointmentNotFoundException {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Appointment appointment =
        appointmentDao.updateAppointment(INSERTED_APP_ID, AUX_STATUS, AUX_CANCEL_DESC);
    // 3. Meaninful assertions
    assertEquals(INSERTED_APP_ID, appointment.getId());
    assertEquals(PATIENT_5, appointment.getPatient());
    assertEquals(DOCTOR_7, appointment.getDoctor());
    assertEquals(INSERTED_LOCAL_DATE, appointment.getDate());
    assertEquals(INSERTED_TIME, appointment.getTimeBlock());
    assertEquals(INSERTED_DESC, appointment.getDescription());
    assertEquals(AUX_STATUS, appointment.getStatus());
    assertEquals(AUX_CANCEL_DESC, appointment.getCancelDesc());
  }

  @Test
  public void testUpdateAppointmentNotFound() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    assertThrows(
        AppointmentNotFoundException.class,
        () -> appointmentDao.updateAppointment(AUX_APP_ID, AUX_STATUS, AUX_CANCEL_DESC));
    // 3. Meaninful assertions
  }

  @Test
  public void testGetAppointmentById() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Optional<Appointment> maybeAppointment = appointmentDao.getAppointmentById(INSERTED_APP_ID);
    // 3. Meaninful assertions
    Assert.assertTrue(maybeAppointment.isPresent());
    Assert.assertEquals(APPOINTMENT_1, maybeAppointment.get());
  }

  @Test
  public void testGetAppointmentByIdAppointmentDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Optional<Appointment> maybeAppointment = appointmentDao.getAppointmentById(AUX_APP_ID);
    // 3. Meaninful assertions
    Assert.assertFalse(maybeAppointment.isPresent());
  }

  @Test
  public void testGetAppointment() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Optional<Appointment> maybeAppointment =
        appointmentDao.getAppointment(DOCTOR_7.getId(), INSERTED_LOCAL_DATE, INSERTED_TIME);
    // 3. Meaninful assertions
    Assert.assertTrue(maybeAppointment.isPresent());
    Assert.assertEquals(APPOINTMENT_1, maybeAppointment.get());
  }

  @Test
  public void testGetAppointmentsForPatient() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    List<Appointment> appointments = appointmentDao.getAppointments(PATIENT_5.getId(), true);
    // 3. Meaninful assertions
    Assert.assertEquals(2, appointments.size());
    Assert.assertEquals(APPOINTMENT_1, appointments.get(0));
    // Assert.assertEquals(APPOINTMENT_2, appointments.get(1));
  }

  @Test
  public void testGetAppointmentsForDoctor() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    List<Appointment> appointments = appointmentDao.getAppointments(DOCTOR_7.getId(), false);
    // 3. Meaninful assertions
    Assert.assertEquals(2, appointments.size());
    Assert.assertEquals(APPOINTMENT_1, appointments.get(0));
    // Assert.assertEquals(APPOINTMENT_2, appointments.get(1));
  }

  @Test
  public void testGetFilteredAppointmentsForPatient() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Page<Appointment> appointments =
        appointmentDao.getFilteredAppointments(
            PATIENT_5.getId(), null, null, null, null, null, true);
    // 3. Meaninful assertions
    Assert.assertNull(appointments.getTotalPages());
    Assert.assertNull(appointments.getCurrentPage());
    Assert.assertEquals(APPOINTMENT_1, appointments.getContent().get(0));
    // Assert.assertEquals(APPOINTMENT_2, appointments.getContent().get(1));
  }

  @Test
  public void testGetFilteredAppointmentsForDoctor() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Page<Appointment> appointments =
        appointmentDao.getFilteredAppointments(
            DOCTOR_7.getId(), null, null, null, null, null, false);
    // 3. Meaninful assertions
    Assert.assertNull(appointments.getTotalPages());
    Assert.assertNull(appointments.getCurrentPage());
    Assert.assertEquals(APPOINTMENT_1, appointments.getContent().get(0));
    // Assert.assertEquals(APPOINTMENT_2, appointments.getContent().get(1));
  }
}
