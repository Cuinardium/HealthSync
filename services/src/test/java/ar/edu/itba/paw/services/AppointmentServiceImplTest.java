package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.AppointmentDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.AppointmentAlreadyExistsException;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.MailService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.interfaces.services.exceptions.*;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.AttendingHours;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.models.Vacation;
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
public class AppointmentServiceImplTest {

  // ================== Doctor Constants ==================
  private static final long DOCTOR_ID = 0;
  private static final String DOCTOR_EMAIL = "doctor_email";
  private static final String DOCTOR_PASSWORD = "doctor_password";
  private static final String DOCTOR_FIRST_NAME = "doctor_first_name";
  private static final String DOCTOR_LAST_NAME = "doctor_last_name";
  private static final Image DOCTOR_IMAGE = new Image.Builder(null, "images/png").build();

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
  private static final Locale DOCTOR_LOCALE = new Locale("en");
  private static final Vacation DOCTOR_VACATION =
      new Vacation.Builder(
              LocalDate.now().plusDays(1),
              ThirtyMinuteBlock.BLOCK_10_00,
              LocalDate.now().plusDays(3),
              ThirtyMinuteBlock.BLOCK_10_00)
          .build();
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
              DOCTOR_LOCALE)
          .id(DOCTOR_ID)
          .vacations(new HashSet<>(Arrays.asList(DOCTOR_VACATION)))
          .rating(RATING)
          .ratingCount(RATING_COUNT)
          .isVerified(true)
          .image(DOCTOR_IMAGE)
          .build();
  // ================== Patient Constants ==================
  private static final long PATIENT_ID = 1;
  private static final String PATIENT_EMAIL = "patient_email";
  private static final String PATIENT_PASSWORD = "patient_password";
  private static final String FIRST_NAME = "patient_first_name";
  private static final String PATIENT_LAST_NAME = "patient_last_name";
  private static final Image PATIENT_IMAGE = new Image.Builder(null, "images/png").build();

  private static final HealthInsurance PATIENT_HEALTH_INSURANCE = HealthInsurance.NONE;
  private static final Locale PATIENT_LOCALE = new Locale("en");
  private static final Patient PATIENT =
      new Patient.Builder(
              PATIENT_EMAIL,
              PATIENT_PASSWORD,
              FIRST_NAME,
              PATIENT_LAST_NAME,
              PATIENT_HEALTH_INSURANCE,
              PATIENT_LOCALE)
          .id(PATIENT_ID)
          .isVerified(true)
          .image(PATIENT_IMAGE)
          .build();
  private static final long APPOINTMENT_ID = 0;
  private static final LocalDate APPOINTMENT_DATE = LocalDate.now();
  private static final ThirtyMinuteBlock APPOINTMENT_TIME = ThirtyMinuteBlock.BLOCK_08_00;
  private static final ThirtyMinuteBlock UNAVAILABLE_APPOINTMENT_TIME =
      ThirtyMinuteBlock.BLOCK_00_00;
  private static final String APPOINTMENT_DESCRIPTION = "appointment_description";
  // ================== Appointment Constants ==================
  private static final Appointment CREATED_APPOINTMENT =
      new Appointment.Builder(
              PATIENT, DOCTOR, APPOINTMENT_DATE, APPOINTMENT_TIME, APPOINTMENT_DESCRIPTION)
          .id(APPOINTMENT_ID)
          .status(AppointmentStatus.CONFIRMED)
          .build();
  private static final List<Appointment> APPOINTMENTS =
      Collections.singletonList(CREATED_APPOINTMENT);
  private static final String CANCELLED_APPOINTMENT_DESCRIPTION =
      "cancelled_appointment_description";
  private static final Appointment CANCELLED_APPOINTMENT =
      new Appointment.Builder(
              PATIENT, DOCTOR, APPOINTMENT_DATE, APPOINTMENT_TIME, APPOINTMENT_DESCRIPTION)
          .id(APPOINTMENT_ID)
          .status(AppointmentStatus.CANCELLED)
          .cancelDescription(CANCELLED_APPOINTMENT_DESCRIPTION)
          .build();
  private static final Appointment COMPLETED_APPOINTMENT =
      new Appointment.Builder(
              PATIENT, DOCTOR, APPOINTMENT_DATE, APPOINTMENT_TIME, APPOINTMENT_DESCRIPTION)
          .id(APPOINTMENT_ID)
          .status(AppointmentStatus.COMPLETED)
          .build();
  private static final long FORBIDDEN_USER_ID = 2;

  private static final LocalDate RANGE_FROM = APPOINTMENT_DATE.minusDays(1);
  private static final LocalDate RANGE_TO = APPOINTMENT_DATE.plusDays(3);

  // ================== Mocks ==================

  @Mock private AppointmentDao appointmentDao;

  @Mock private PatientService patientService;

  @Mock private DoctorService doctorService;

  @Mock private MailService mailService;

  @InjectMocks private AppointmentServiceImpl as;

  // ================== createAppointment ==================

  @Test
  public void testCreateAppointment()
      throws DoctorNotFoundException, PatientNotFoundException, DoctorNotAvailableException,
          AppointmentAlreadyExistsException {
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

    Mockito.when(
            appointmentDao.getFilteredAppointments(
                DOCTOR_ID, null, APPOINTMENT_DATE, APPOINTMENT_DATE, null, null, false))
        .thenReturn(new Page<>(Collections.emptyList(), null, null, null));

    // Mock mailService
    Mockito.doNothing()
        .when(mailService)
        .sendAppointmentRequestMail(Mockito.any(Appointment.class));
    Mockito.doNothing()
        .when(mailService)
        .sendAppointmentReminderMail(Mockito.any(Appointment.class));

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

    Mockito.when(
            appointmentDao.getFilteredAppointments(
                DOCTOR_ID, null, APPOINTMENT_DATE, APPOINTMENT_DATE, null, null, false))
        .thenReturn(new Page<>(Collections.emptyList(), null, null, null));

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
    Mockito.when(
            appointmentDao.getFilteredAppointments(
                DOCTOR_ID, null, APPOINTMENT_DATE, APPOINTMENT_DATE, null, null, false))
        .thenReturn(
            new Page<>(
                new ArrayList<>(Collections.singletonList(CREATED_APPOINTMENT)), null, null, null));

    // 2. Ejercitar la class under test
    as.createAppointment(
        PATIENT_ID, DOCTOR_ID, APPOINTMENT_DATE, APPOINTMENT_TIME, APPOINTMENT_DESCRIPTION);
  }

  @Test
  public void testCreateAppointmentAlreadyTakenByCancelledAppointment()
      throws DoctorNotFoundException, PatientNotFoundException, DoctorNotAvailableException,
          AppointmentAlreadyExistsException {
    // 1. Precondiciones

    // Mock doctorService
    Mockito.when(doctorService.getDoctorById(DOCTOR_ID)).thenReturn(Optional.of(DOCTOR));

    // Mock patientService
    Mockito.when(patientService.getPatientById(PATIENT_ID)).thenReturn(Optional.of(PATIENT));

    // Mock appointmentDao
    Mockito.when(
            appointmentDao.getFilteredAppointments(
                DOCTOR_ID, null, APPOINTMENT_DATE, APPOINTMENT_DATE, null, null, false))
        .thenReturn(
            new Page<>(
                new ArrayList<>(Collections.singletonList(CANCELLED_APPOINTMENT)),
                null,
                null,
                null));

    Mockito.when(
            appointmentDao.createAppointment(
                PATIENT, DOCTOR, APPOINTMENT_DATE, APPOINTMENT_TIME, APPOINTMENT_DESCRIPTION))
        .thenReturn(CREATED_APPOINTMENT);

    // Mock mailService
    Mockito.doNothing()
        .when(mailService)
        .sendAppointmentRequestMail(Mockito.any(Appointment.class));
    Mockito.doNothing()
        .when(mailService)
        .sendAppointmentReminderMail(Mockito.any(Appointment.class));

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
          ar.edu.itba.paw.interfaces.persistence.exceptions.AppointmentNotFoundException,
          AppointmentInmutableException {
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
        .sendAppointmentCancelledByDoctorMail(Mockito.any(Appointment.class));

    // 2. Ejercitar la class under test
    Appointment appointment =
        as.cancelAppointment(APPOINTMENT_ID, CANCELLED_APPOINTMENT_DESCRIPTION, DOCTOR_ID);

    // 3. Meaningful assertions
    Assert.assertEquals(CANCELLED_APPOINTMENT, appointment);
  }

  @Test
  public void testCancelAppointmentByPatient()
      throws AppointmentNotFoundException, CancelForbiddenException,
          ar.edu.itba.paw.interfaces.persistence.exceptions.AppointmentNotFoundException,
          AppointmentInmutableException {
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
        .sendAppointmentCancelledByPatientMail(Mockito.any(Appointment.class));

    // 2. Ejercitar la class under test

    Appointment appointment =
        as.cancelAppointment(APPOINTMENT_ID, CANCELLED_APPOINTMENT_DESCRIPTION, PATIENT_ID);

    // 3. Meaningful assertions
    Assert.assertEquals(CANCELLED_APPOINTMENT, appointment);
  }

  @Test(expected = AppointmentNotFoundException.class)
  public void testCancelAppointmentDoesNotExist()
      throws AppointmentNotFoundException, CancelForbiddenException, AppointmentInmutableException {
    // 1. Precondiciones

    // Mock appointmentDao
    Mockito.when(appointmentDao.getAppointmentById(APPOINTMENT_ID)).thenReturn(Optional.empty());

    // 2. Ejercitar la class under test
    as.cancelAppointment(APPOINTMENT_ID, CANCELLED_APPOINTMENT_DESCRIPTION, DOCTOR_ID);
  }

  @Test(expected = CancelForbiddenException.class)
  public void testCancelAppointmentForbidden()
      throws AppointmentNotFoundException, CancelForbiddenException, AppointmentInmutableException {
    // 1. Precondiciones

    // Mock appointmentDao
    Mockito.when(appointmentDao.getAppointmentById(APPOINTMENT_ID))
        .thenReturn(Optional.of(CANCELLED_APPOINTMENT));

    // 2. Ejercitar la class under test
    as.cancelAppointment(APPOINTMENT_ID, CANCELLED_APPOINTMENT_DESCRIPTION, FORBIDDEN_USER_ID);
  }

  @Test(expected = AppointmentInmutableException.class)
  public void testCancelCancelledAppointment()
      throws AppointmentNotFoundException, CancelForbiddenException, AppointmentInmutableException {
    // 1. Precondiciones

    // Mock appointmentDao
    Mockito.when(appointmentDao.getAppointmentById(APPOINTMENT_ID))
        .thenReturn(Optional.of(CANCELLED_APPOINTMENT));

    // 2. Ejercitar la class under test
    as.cancelAppointment(APPOINTMENT_ID, CANCELLED_APPOINTMENT_DESCRIPTION, DOCTOR_ID);
  }

  @Test(expected = AppointmentInmutableException.class)
  public void testCancelCompletedAppointment()
      throws AppointmentNotFoundException, CancelForbiddenException, AppointmentInmutableException {
    // 1. Precondiciones

    // Mock appointmentDao
    Mockito.when(appointmentDao.getAppointmentById(APPOINTMENT_ID))
        .thenReturn(Optional.of(COMPLETED_APPOINTMENT));

    // 2. Ejercitar la class under test
    as.cancelAppointment(APPOINTMENT_ID, CANCELLED_APPOINTMENT_DESCRIPTION, DOCTOR_ID);
  }

  // ================== getAvailableHoursOnRange ==================

  @Test
  public void testGetAvailableHoursOnRange() throws DoctorNotFoundException {
    // 1. Precondiciones
    List<List<ThirtyMinuteBlock>> expectedAvailableHours = new ArrayList<>();

    // No appointments nor vacations in first day
    expectedAvailableHours.add(new ArrayList<>(ATTENDING_HOURS_FOR_DAY));

    // Appointments in second day
    List<ThirtyMinuteBlock> availableHoursForSecondDay = new ArrayList<>(ATTENDING_HOURS_FOR_DAY);
    availableHoursForSecondDay.remove(APPOINTMENT_TIME);

    expectedAvailableHours.add(availableHoursForSecondDay);

    // Vacations in third day from 10:00
    List<ThirtyMinuteBlock> availableHoursForThirdDay = new ArrayList<>(ATTENDING_HOURS_FOR_DAY);
    availableHoursForThirdDay.removeAll(
        ThirtyMinuteBlock.fromRange(ThirtyMinuteBlock.BLOCK_10_00, ThirtyMinuteBlock.BLOCK_23_30));

    expectedAvailableHours.add(availableHoursForThirdDay);

    // Vacations all day in fourth day
    expectedAvailableHours.add(Collections.emptyList());

    // Vacations in fifth day until 10:00
    List<ThirtyMinuteBlock> availableHoursForFifthDay = new ArrayList<>(ATTENDING_HOURS_FOR_DAY);
    availableHoursForFifthDay.removeAll(
        ThirtyMinuteBlock.fromRange(ThirtyMinuteBlock.BLOCK_00_00, ThirtyMinuteBlock.BLOCK_10_00));

    expectedAvailableHours.add(availableHoursForFifthDay);

    // Mock doctorService
    Mockito.when(doctorService.getDoctorById(DOCTOR_ID)).thenReturn(Optional.of(DOCTOR));

    // Mock appointmentDao
    Mockito.when(
            appointmentDao.getFilteredAppointments(
                DOCTOR_ID, null, RANGE_FROM, RANGE_TO, null, null, false))
        .thenReturn(new Page<>(APPOINTMENTS, null, null, null));

    // 2. Ejercitar la class under test
    List<List<ThirtyMinuteBlock>> availableHours =
        as.getAvailableHoursForDoctorOnRange(DOCTOR_ID, RANGE_FROM, RANGE_TO);

    // 3. Meaningful assertions

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

  @Test
  public void testGetOccupiedHours() throws DoctorNotFoundException, InvalidRangeException {

    DOCTOR_VACATION.setDoctor(DOCTOR);

    // 1. Precondiciones
    Map<LocalDate, List<ThirtyMinuteBlock>> expectedOccupiedHours = new HashMap<>();

    // Appointment in first day
    expectedOccupiedHours.put(APPOINTMENT_DATE, Collections.singletonList(APPOINTMENT_TIME));

    // Vacation from 10:00 in second day
    List<ThirtyMinuteBlock> occupiedHoursForSecondDay =
        ThirtyMinuteBlock.fromRange(ThirtyMinuteBlock.BLOCK_10_00, ThirtyMinuteBlock.BLOCK_23_30)
            .stream()
            .filter(DOCTOR.getAttendingBlocksForDate(APPOINTMENT_DATE.plusDays(1))::contains)
            .collect(Collectors.toList());

    expectedOccupiedHours.put(APPOINTMENT_DATE.plusDays(1), occupiedHoursForSecondDay);

    // Vacation all day in third day
    List<ThirtyMinuteBlock> occupiedHoursForThirdDay =
        ThirtyMinuteBlock.fromRange(ThirtyMinuteBlock.BLOCK_00_00, ThirtyMinuteBlock.BLOCK_23_30)
            .stream()
            .filter(DOCTOR.getAttendingBlocksForDate(APPOINTMENT_DATE.plusDays(2))::contains)
            .collect(Collectors.toList());

    expectedOccupiedHours.put(APPOINTMENT_DATE.plusDays(2), occupiedHoursForThirdDay);

    // Vacation until 10:00 in fourth day
    List<ThirtyMinuteBlock> occupiedHoursForFourthDay =
        ThirtyMinuteBlock.fromRange(ThirtyMinuteBlock.BLOCK_00_00, ThirtyMinuteBlock.BLOCK_10_00)
            .stream()
            .filter(DOCTOR.getAttendingBlocksForDate(APPOINTMENT_DATE.plusDays(3))::contains)
            .collect(Collectors.toList());

    expectedOccupiedHours.put(APPOINTMENT_DATE.plusDays(3), occupiedHoursForFourthDay);

    // Free day in fifth day

    // Mock doctorService
    Mockito.when(doctorService.getDoctorById(DOCTOR_ID)).thenReturn(Optional.of(DOCTOR));

    // Mock appointmentDao
    Mockito.when(
            appointmentDao.getFilteredAppointments(
                DOCTOR_ID,
                AppointmentStatus.CONFIRMED,
                APPOINTMENT_DATE,
                APPOINTMENT_DATE.plusDays(3),
                null,
                null,
                false))
        .thenReturn(new Page<>(APPOINTMENTS, null, null, null));

    // 2. Ejercitar la class under test
    Map<LocalDate, List<ThirtyMinuteBlock>> occupiedHours =
        as.getOccupiedHours(DOCTOR_ID, APPOINTMENT_DATE, APPOINTMENT_DATE.plusDays(3));

    // 3. Meaningful assertions
    Assert.assertEquals(expectedOccupiedHours, occupiedHours);
  }

  @Test(expected = DoctorNotFoundException.class)
  public void testGetOccupiedHoursDoctorNotFound()
      throws DoctorNotFoundException, InvalidRangeException {
    // 1. Precondiciones

    // Mock doctorService
    Mockito.when(doctorService.getDoctorById(DOCTOR_ID)).thenReturn(Optional.empty());

    // 2. Ejercitar la class under test
    as.getOccupiedHours(DOCTOR_ID, APPOINTMENT_DATE, APPOINTMENT_DATE.plusDays(3));
  }

  @Test(expected = InvalidRangeException.class)
  public void testGetOccupiedHoursInvalidRange()
      throws DoctorNotFoundException, InvalidRangeException {
    as.getOccupiedHours(DOCTOR_ID, APPOINTMENT_DATE.plusDays(3), APPOINTMENT_DATE);
  }

  @Test(expected = InvalidRangeException.class)
  public void testGetOccupiedHoursNullRange()
      throws DoctorNotFoundException, InvalidRangeException {
    as.getOccupiedHours(DOCTOR_ID, null, null);
  }
}
