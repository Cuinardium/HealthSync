package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.AppointmentDao;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.MailService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.interfaces.services.exceptions.AppointmentNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotAvailableException;
import ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.CancelForbiddenException;
import ar.edu.itba.paw.interfaces.services.exceptions.PatientNotFoundException;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.AttendingHours;
import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
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
  private static final Collection<ThirtyMinuteBlock> ATTENDING_HOURS_FOR_DAY =
      ThirtyMinuteBlock.fromRange(ThirtyMinuteBlock.BLOCK_08_00, ThirtyMinuteBlock.BLOCK_16_00);
  private static final AttendingHours ATTENDING_HOURS =
      new AttendingHours(
          ATTENDING_HOURS_FOR_DAY,
          ATTENDING_HOURS_FOR_DAY,
          ATTENDING_HOURS_FOR_DAY,
          ATTENDING_HOURS_FOR_DAY,
          ATTENDING_HOURS_FOR_DAY,
          ATTENDING_HOURS_FOR_DAY,
          ATTENDING_HOURS_FOR_DAY);
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
  private static final ThirtyMinuteBlock APPOINTMENT_TIME = ThirtyMinuteBlock.BLOCK_08_00;
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

  private static final List<Appointment> APPOINTMENTS = Arrays.asList(CREATED_APPOINTMENT);

  private static final long FORBIDDEN_USER_ID = 2;

  private static final LocalDate RANGE_FROM = APPOINTMENT_DATE.minusDays(1);
  private static final LocalDate RANGE_TO = APPOINTMENT_DATE.plusDays(1);

  // ================== Mocks ==================

  @Mock private AppointmentDao appointmentDao;

  @Mock private PatientService patientService;

  @Mock private DoctorService doctorService;

  @Mock private MailService mailService;

  @InjectMocks private AppointmentServiceImpl as;

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

    // Mock mailService
    Mockito.doNothing()
        .when(mailService)
        .sendAppointmentRequestMail(Mockito.any(Appointment.class), Mockito.any(Locale.class));
    Mockito.doNothing()
        .when(mailService)
        .sendAppointmentReminderMail(Mockito.any(Appointment.class), Mockito.any(Locale.class));

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

    // Mock mailService
    Mockito.doNothing()
        .when(mailService)
        .sendAppointmentRequestMail(Mockito.any(Appointment.class), Mockito.any(Locale.class));
    Mockito.doNothing()
        .when(mailService)
        .sendAppointmentReminderMail(Mockito.any(Appointment.class), Mockito.any(Locale.class));

    // 2. Ejercitar la class under test
    Appointment appointment =
        as.createAppointment(
            PATIENT_ID, DOCTOR_ID, APPOINTMENT_DATE, APPOINTMENT_TIME, APPOINTMENT_DESCRIPTION);

    // 3. Meaningful assertions
    Assert.assertEquals(CREATED_APPOINTMENT, appointment);
  }

  // ================== cancelAppointment ==================

  @Test
  public void testCancelAppointmentByDoctor()
      throws AppointmentNotFoundException, CancelForbiddenException,
          ar.edu.itba.paw.interfaces.persistence.exceptions.AppointmentNotFoundException {
    // 1. Precondiciones

    // Mock appointmentDao
    Mockito.when(appointmentDao.getAppointmentById(APPOINTMENT_ID))
        .thenReturn(Optional.of(CREATED_APPOINTMENT));

    Mockito.when(
            appointmentDao.updateAppointment(
                APPOINTMENT_ID, AppointmentStatus.CANCELLED, CANCELLED_APPOINTMENT_DESCRIPTION))
        .thenReturn(CANCELLED_APPOINTMENT);

    // Mock mailService
    Mockito.doNothing()
        .when(mailService)
        .sendAppointmentCancelledByDoctorMail(
            Mockito.any(Appointment.class), Mockito.any(Locale.class));

    // 2. Ejercitar la class under test
    Appointment appointment =
        as.cancelAppointment(APPOINTMENT_ID, CANCELLED_APPOINTMENT_DESCRIPTION, DOCTOR_ID);

    // 3. Meaningful assertions
    Assert.assertEquals(CANCELLED_APPOINTMENT, appointment);
  }

  @Test
  public void testCancelAppointmentByPatient()
      throws AppointmentNotFoundException, CancelForbiddenException,
          ar.edu.itba.paw.interfaces.persistence.exceptions.AppointmentNotFoundException {
    // 1. Precondiciones

    // Mock appointmentDao
    Mockito.when(appointmentDao.getAppointmentById(APPOINTMENT_ID))
        .thenReturn(Optional.of(CREATED_APPOINTMENT));

    Mockito.when(
            appointmentDao.updateAppointment(
                APPOINTMENT_ID, AppointmentStatus.CANCELLED, CANCELLED_APPOINTMENT_DESCRIPTION))
        .thenReturn(CANCELLED_APPOINTMENT);

    // Mock mailService
    Mockito.doNothing()
        .when(mailService)
        .sendAppointmentCancelledByPatientMail(
            Mockito.any(Appointment.class), Mockito.any(Locale.class));

    // 2. Ejercitar la class under test

    Appointment appointment =
        as.cancelAppointment(APPOINTMENT_ID, CANCELLED_APPOINTMENT_DESCRIPTION, PATIENT_ID);

    // 3. Meaningful assertions
    Assert.assertEquals(CANCELLED_APPOINTMENT, appointment);
  }

  @Test(expected = AppointmentNotFoundException.class)
  public void testCancelAppointmentDoesNotExist()
      throws AppointmentNotFoundException, CancelForbiddenException,
          ar.edu.itba.paw.interfaces.persistence.exceptions.AppointmentNotFoundException {
    // 1. Precondiciones

    // Mock appointmentDao
    Mockito.when(appointmentDao.getAppointmentById(APPOINTMENT_ID))
        .thenReturn(Optional.empty());

    // 2. Ejercitar la class under test
    as.cancelAppointment(APPOINTMENT_ID, CANCELLED_APPOINTMENT_DESCRIPTION, DOCTOR_ID);
  }

  @Test(expected = CancelForbiddenException.class)
  public void testCancelAppointmentForbidden()
      throws AppointmentNotFoundException, CancelForbiddenException,
          ar.edu.itba.paw.interfaces.persistence.exceptions.AppointmentNotFoundException {
    // 1. Precondiciones

    // Mock appointmentDao
    Mockito.when(appointmentDao.getAppointmentById(APPOINTMENT_ID))
        .thenReturn(Optional.of(CANCELLED_APPOINTMENT));

    // 2. Ejercitar la class under test
    as.cancelAppointment(APPOINTMENT_ID, CANCELLED_APPOINTMENT_DESCRIPTION, FORBIDDEN_USER_ID);
  }

  // ================== getAvailableHoursOnRange ==================

  @Test
  public void testGetAvailableHoursOnRange() throws DoctorNotFoundException {
    // 1. Precondiciones

    // Mock doctorService
    Mockito.when(doctorService.getDoctorById(DOCTOR_ID)).thenReturn(Optional.of(DOCTOR));

    // Mock appointmentDao
    Mockito.when(appointmentDao.getFilteredAppointments(DOCTOR_ID, null, RANGE_FROM, RANGE_TO, null, null, false))
        .thenReturn(new Page<>(APPOINTMENTS, null, null, null));

    // 2. Ejercitar la class under test
    List<List<ThirtyMinuteBlock>> availableHours =
        as.getAvailableHoursForDoctorOnRange(DOCTOR_ID, RANGE_FROM, RANGE_TO);

    // 3. Meaningful assertions
    
    List<List<ThirtyMinuteBlock>> expectedAvailableHours = new ArrayList<>();

    expectedAvailableHours.add(new ArrayList<>(ATTENDING_HOURS_FOR_DAY));

    List<ThirtyMinuteBlock> availableHoursForDay = new ArrayList<>(ATTENDING_HOURS_FOR_DAY);
    availableHoursForDay.remove(APPOINTMENT_TIME);
    expectedAvailableHours.add(availableHoursForDay);

    expectedAvailableHours.add(new ArrayList<>(ATTENDING_HOURS_FOR_DAY));

    Assert.assertEquals(expectedAvailableHours, availableHours);
  }

  @Test(expected = DoctorNotFoundException.class)
  public void testGetAvailableHoursOnRangeDoctorNotFound() throws DoctorNotFoundException {
    // 1. Precondiciones

    // Mock doctorService
    Mockito.when(doctorService.getDoctorById(DOCTOR_ID)).thenReturn(Optional.empty());

    // 2. Ejercitar la class under test
    as.getAvailableHoursForDoctorOnRange(DOCTOR_ID, RANGE_FROM, RANGE_TO);
  }
}