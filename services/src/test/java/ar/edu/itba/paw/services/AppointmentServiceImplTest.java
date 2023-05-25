package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.AppointmentDao;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.MailService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotAvailableException;
import ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.PatientNotFoundException;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.AttendingHours;
import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

// Le permite a Mockito tomar control de JUnit y permite anotaciones que sino no estarian
// disponibles
@RunWith(MockitoJUnitRunner.class)
public class AppointmentServiceImplTest {

  // ================== Doctor Constants ==================
  private static final long DOCTOR_ID = 0;
  private static final String DOCTOR_EMAIL = "doctor_email";
  private static final String DOCTOR_PASSWORD = "doctor_password";
  private static final String DOCTOR_FIRST_NAME = "doctor_first_name";
  private static final String DOCTOR_LAST_NAME = "doctor_last_name";
  private static final Long DOCTOR_PFP_ID = null;

  private static final List<HealthInsurance> DOCTOR_HEALTH_INSURANCES =
      Arrays.asList(HealthInsurance.OSDE, HealthInsurance.OMINT);
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

  private static final Doctor DOCTOR =
      new Doctor(
          DOCTOR_ID,
          DOCTOR_EMAIL,
          DOCTOR_PASSWORD,
          DOCTOR_FIRST_NAME,
          DOCTOR_LAST_NAME,
          DOCTOR_PFP_ID,
          DOCTOR_HEALTH_INSURANCES,
          SPECIALTY,
          LOCATION,
          ATTENDING_HOURS,
          RATING,
          RATING_COUNT);

  // ================== Patient Constants ==================

  private static final long PATIENT_ID = 1;
  private static final String PATIENT_EMAIL = "patient_email";
  private static final String PATIENT_PASSWORD = "patient_password";
  private static final String FIRST_NAME = "patient_first_name";
  private static final String PATIENT_LAST_NAME = "patient_last_name";
  private static final Long PATIENT_PFP_ID = null;

  private static final HealthInsurance PATIENT_HEALTH_INSURANCE = HealthInsurance.NONE;
  private static final Patient PATIENT =
      new Patient(
          PATIENT_ID,
          PATIENT_EMAIL,
          PATIENT_PASSWORD,
          FIRST_NAME,
          PATIENT_LAST_NAME,
          PATIENT_PFP_ID,
          PATIENT_HEALTH_INSURANCE);

  // ================== Appointment Constants ==================

  private static final long APPOINTMENT_ID = 0;
  private static final LocalDate APPOINTMENT_DATE = LocalDate.now();
  private static final ThirtyMinuteBlock APPOINTMENT_TIME = ThirtyMinuteBlock.BLOCK_00_30;
  private static final ThirtyMinuteBlock UNAVAILABLE_APPOINTMENT_TIME =
      ThirtyMinuteBlock.BLOCK_00_00;
  private static final String APPOINTMENT_DESCRIPTION = "appointment_description";
  private static final String CANCELLED_APPOINTMENT_DESCRIPTION =
      "cancelled_appointment_description";

  private static final Appointment CREATED_APPOINTMENT =
      new Appointment(
          APPOINTMENT_ID,
          PATIENT,
          DOCTOR,
          APPOINTMENT_DATE,
          APPOINTMENT_TIME,
          AppointmentStatus.CONFIRMED,
          APPOINTMENT_DESCRIPTION,
          null);

  private static final Appointment CANCELLED_APPOINTMENT =
      new Appointment(
          APPOINTMENT_ID,
          PATIENT,
          DOCTOR,
          APPOINTMENT_DATE,
          APPOINTMENT_TIME,
          AppointmentStatus.CANCELLED,
          APPOINTMENT_DESCRIPTION,
          CANCELLED_APPOINTMENT_DESCRIPTION);

  // ================== Mocks ==================

  @Mock private AppointmentDao appointmentDao;

  @Mock private PatientService patientService;

  @Mock private DoctorService doctorService;

  @Mock private MailService mailService;

  @InjectMocks private AppointmentServiceImpl as;

  @Before
  public void setup() {
    // Mock mailService
    Mockito.doNothing()
        .when(mailService)
        .sendAppointmentRequestMail(Mockito.any(Appointment.class), Mockito.any(Locale.class));
    Mockito.doNothing()
        .when(mailService)
        .sendAppointmentReminderMail(Mockito.any(Appointment.class), Mockito.any(Locale.class));
  }

  // ================== createAppointment ==================

  @Test
  public void testCreateAppointment()
      throws DoctorNotFoundException, PatientNotFoundException, DoctorNotAvailableException {
    // 1. Precondiciones
    // Mock doctorService
    Mockito.when(doctorService.getDoctorById(DOCTOR_ID)).thenReturn(Optional.of(DOCTOR));

    // Mock patientService
    Mockito.when(patientService.getPatientById(PATIENT_ID)).thenReturn(Optional.of(PATIENT));

    // Mock appointmentDao
    Mockito.when(
            appointmentDao.createAppointment(
                PATIENT, DOCTOR, APPOINTMENT_DATE, APPOINTMENT_TIME, APPOINTMENT_DESCRIPTION))
        .thenReturn(CREATED_APPOINTMENT);

    Mockito.when(appointmentDao.getAppointment(DOCTOR_ID, APPOINTMENT_DATE, APPOINTMENT_TIME))
        .thenReturn(Optional.empty());

    // 2. Ejercitar la class under test
    Appointment appointment =
        as.createAppointment(
            PATIENT_ID, DOCTOR_ID, APPOINTMENT_DATE, APPOINTMENT_TIME, APPOINTMENT_DESCRIPTION);

    // 3. Meaningful assertions
    Assert.assertEquals(CREATED_APPOINTMENT, appointment);
  }

  @Test(expected = DoctorNotFoundException.class)
  public void testCreateAppointmentUnexistingDoctor()
      throws DoctorNotFoundException, PatientNotFoundException, DoctorNotAvailableException {
    // 1. Precondiciones

    // Mock doctorService
    Mockito.when(doctorService.getDoctorById(DOCTOR_ID)).thenReturn(Optional.empty());

    // 2. Ejercitar la class under test
    as.createAppointment(
        PATIENT_ID, DOCTOR_ID, APPOINTMENT_DATE, APPOINTMENT_TIME, APPOINTMENT_DESCRIPTION);
  }

  @Test(expected = PatientNotFoundException.class)
  public void testCreateAppointmentUnexistingPatient()
      throws DoctorNotFoundException, PatientNotFoundException, DoctorNotAvailableException {
    // 1. Precondiciones

    // Mock doctorService
    Mockito.when(doctorService.getDoctorById(DOCTOR_ID)).thenReturn(Optional.of(DOCTOR));

    // Mock patientService
    Mockito.when(patientService.getPatientById(PATIENT_ID)).thenReturn(Optional.empty());

    // 2. Ejercitar la class under test
    as.createAppointment(
        PATIENT_ID, DOCTOR_ID, APPOINTMENT_DATE, APPOINTMENT_TIME, APPOINTMENT_DESCRIPTION);
  }

  @Test(expected = DoctorNotAvailableException.class)
  public void testCreateAppointmentInUnavailableTime()
      throws DoctorNotFoundException, PatientNotFoundException, DoctorNotAvailableException {
    // 1. Precondiciones

    // Mock doctorService
    Mockito.when(doctorService.getDoctorById(DOCTOR_ID)).thenReturn(Optional.of(DOCTOR));

    // Mock patientService
    Mockito.when(patientService.getPatientById(PATIENT_ID)).thenReturn(Optional.of(PATIENT));

    // 2. Ejercitar la class under test
    as.createAppointment(
        PATIENT_ID,
        DOCTOR_ID,
        APPOINTMENT_DATE,
        UNAVAILABLE_APPOINTMENT_TIME,
        APPOINTMENT_DESCRIPTION);
  }

  @Test(expected = DoctorNotAvailableException.class)
  public void testCreateAppointmentAlreadyTaken()
      throws DoctorNotFoundException, PatientNotFoundException, DoctorNotAvailableException {
    // 1. Precondiciones

    // Mock doctorService
    Mockito.when(doctorService.getDoctorById(DOCTOR_ID)).thenReturn(Optional.of(DOCTOR));

    // Mock patientService
    Mockito.when(patientService.getPatientById(PATIENT_ID)).thenReturn(Optional.of(PATIENT));

    // Mock appointmentDao
    Mockito.when(appointmentDao.getAppointment(DOCTOR_ID, APPOINTMENT_DATE, APPOINTMENT_TIME))
        .thenReturn(Optional.of(CREATED_APPOINTMENT));

    // 2. Ejercitar la class under test
    as.createAppointment(
        PATIENT_ID, DOCTOR_ID, APPOINTMENT_DATE, APPOINTMENT_TIME, APPOINTMENT_DESCRIPTION);
  }

  public void testCreateAppointmentAlreadyTakenByCancelledAppointment()
      throws DoctorNotFoundException, PatientNotFoundException, DoctorNotAvailableException {
    // 1. Precondiciones

    // Mock doctorService
    Mockito.when(doctorService.getDoctorById(DOCTOR_ID)).thenReturn(Optional.of(DOCTOR));

    // Mock patientService
    Mockito.when(patientService.getPatientById(PATIENT_ID)).thenReturn(Optional.of(PATIENT));

    // Mock appointmentDao
    Mockito.when(appointmentDao.getAppointment(DOCTOR_ID, APPOINTMENT_DATE, APPOINTMENT_TIME))
        .thenReturn(Optional.of(CANCELLED_APPOINTMENT));

    Mockito.when(
            appointmentDao.createAppointment(
                PATIENT, DOCTOR, APPOINTMENT_DATE, APPOINTMENT_TIME, APPOINTMENT_DESCRIPTION))
        .thenReturn(CREATED_APPOINTMENT);

    // 2. Ejercitar la class under test
    Appointment appointment =
        as.createAppointment(
            PATIENT_ID, DOCTOR_ID, APPOINTMENT_DATE, APPOINTMENT_TIME, APPOINTMENT_DESCRIPTION);

    // 3. Meaningful assertions
    Assert.assertEquals(CREATED_APPOINTMENT, appointment);
  }

  // ================== updateAppointment ==================

  @Test
  public void testUpdateAppointment() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testUpdateAppointmentDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testGetAppointmentById() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testGetAppointmentByIdDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testGetAppointmentsForPatient() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testGetAppointmentsForPatientDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testGetAppointmentsForDoctor() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testGetAppointmentsForDoctorDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testGetFilteredAppointmentsForPatient() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testGetFilteredAppointmentsForPatientDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testGetFilteredAppointmentsForDoctor() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testGetFilteredAppointmentsForDoctorDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testGetAvailableHoursForDoctorOnDate() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testGetAvailableHoursForDoctorOnRange() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }
}
