package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.IndicationDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.interfaces.services.exceptions.*;
import ar.edu.itba.paw.models.*;
import org.junit.Test;
import org.mockito.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class IndicationServiceImplTest {

    // ================== Doctor Constants ==================
    private static final long DOCTOR_ID = 0;
    private static final String DOCTOR_EMAIL = "doctor_email";
    private static final String DOCTOR_PASSWORD = "doctor_password";
    private static final String DOCTOR_FIRST_NAME = "doctor_first_name";
    private static final String DOCTOR_LAST_NAME = "doctor_last_name";
    private static final Image IMAGE = new Image.Builder(null, "images/png").build();
    private static final Locale LOCALE = new Locale("en");
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
                    LOCALE)
                    .id(DOCTOR_ID)
                    .rating(RATING)
                    .ratingCount(RATING_COUNT)
                    .isVerified(true)
                    .image(IMAGE)
                    .build();
    // ================== Patient Constants ==================
    private static final long PATIENT_ID = 1;
    private static final String PATIENT_EMAIL = "patient_email";
    private static final String PATIENT_PASSWORD = "patient_password";
    private static final String FIRST_NAME = "patient_first_name";
    private static final String PATIENT_LAST_NAME = "patient_last_name";

    private static final HealthInsurance PATIENT_HEALTH_INSURANCE = HealthInsurance.NONE;
    private static final Patient PATIENT =
            new Patient.Builder(
                    PATIENT_EMAIL,
                    PATIENT_PASSWORD,
                    FIRST_NAME,
                    PATIENT_LAST_NAME,
                    PATIENT_HEALTH_INSURANCE,
                    LOCALE)
                    .id(PATIENT_ID)
                    .isVerified(true)
                    .image(IMAGE)
                    .build();


    // ================== Appointment Constants ==================
    private static final long APPOINTMENT_ID = 0;
    private static final LocalDate APPOINTMENT_DATE = LocalDate.now();
    private static final ThirtyMinuteBlock APPOINTMENT_TIME = ThirtyMinuteBlock.BLOCK_08_00;
    private static final ThirtyMinuteBlock UNAVAILABLE_APPOINTMENT_TIME =
            ThirtyMinuteBlock.BLOCK_00_00;
    private static final String APPOINTMENT_DESCRIPTION = "appointment_description";
    private static final Appointment CONFIRMED_APPOINTMENT =
            new Appointment.Builder(
                    PATIENT, DOCTOR, APPOINTMENT_DATE, APPOINTMENT_TIME, APPOINTMENT_DESCRIPTION)
                    .id(APPOINTMENT_ID)
                    .status(AppointmentStatus.CONFIRMED)
                    .build();

    private static final Appointment COMPLETED_APPOINTMENT =
            new Appointment.Builder(
                    PATIENT, DOCTOR, APPOINTMENT_DATE, APPOINTMENT_TIME, APPOINTMENT_DESCRIPTION)
                    .id(APPOINTMENT_ID)
                    .status(AppointmentStatus.COMPLETED)
                    .build();

    private static final Appointment CANCELLED_APPOINTMENT =
            new Appointment.Builder(
                    PATIENT, DOCTOR, APPOINTMENT_DATE, APPOINTMENT_TIME, APPOINTMENT_DESCRIPTION)
                    .id(APPOINTMENT_ID)
                    .status(AppointmentStatus.CANCELLED)
                    .build();

    // ================== Indication Constants ==================
    private static final long INDICATION_ID = 0;
    private static final String TEST_DESCRIPTION = "test_description";
    private static final File FILE = new File.Builder(null, "file/png").build();
    private static final int PAGE = 0;
    private static final int PAGE_SIZE = 10;
    private static final Indication INDICATION =
            new Indication.Builder(COMPLETED_APPOINTMENT, DOCTOR, LocalDate.now(), TEST_DESCRIPTION)
                    .id(INDICATION_ID)
                    .file(FILE)
                    .build();

    private static final Page<Indication> INDICATION_PAGE = new Page<>(Collections.singletonList(INDICATION), 1, 1, 1);

    @Mock
    private final UserService userService = mock(UserService.class);

    @Mock
    private final AppointmentService appointmentService = mock(AppointmentService.class);

    @Mock
    private final IndicationDao indicationDao = mock(IndicationDao.class);

    @Mock
    private final NotificationService notificationService = mock(NotificationService.class);

    @Mock
    private final FileService fileService = mock(FileService.class);

    @InjectMocks
    private final IndicationServiceImpl indicationService = new IndicationServiceImpl(
            userService, appointmentService, indicationDao, notificationService, fileService
    );



    @Test
    public void testCreateIndicationSuccess() throws Exception {
        // Arrange
        when(appointmentService.getAppointmentById(APPOINTMENT_ID))
                .thenReturn(Optional.of(COMPLETED_APPOINTMENT));
        when(userService.getUserById(DOCTOR_ID))
                .thenReturn(Optional.of(DOCTOR));
        when(notificationService.getUserAppointmentNotification(PATIENT_ID, APPOINTMENT_ID))
                .thenReturn(Optional.empty());
        when(indicationDao.createIndication(any(Indication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(fileService.uploadFile(FILE))
                .thenReturn(FILE);

        // Act
        Indication indication = indicationService.createIndication(
                APPOINTMENT_ID, DOCTOR_ID, TEST_DESCRIPTION, FILE);

        // Assert
        assertNotNull(indication);
        assertEquals(TEST_DESCRIPTION, indication.getDescription());
        assertEquals(FILE, indication.getFile());
        verify(notificationService).createNotification(PATIENT_ID, APPOINTMENT_ID);
        verify(indicationDao).createIndication(any(Indication.class));
    }

    @Test(expected = AppointmentNotFoundException.class)
    public void testCreateIndicationAppointmentNotFound() throws Exception {
        // Arrange
        when(appointmentService.getAppointmentById(APPOINTMENT_ID))
                .thenReturn(Optional.empty());

        // Act
        indicationService.createIndication(APPOINTMENT_ID, DOCTOR_ID, TEST_DESCRIPTION, null);
    }

    @Test(expected = NotCompletedException.class)
    public void testCreateIndicationNotCompletedCancelled() throws Exception {
        // Arrange
        when(appointmentService.getAppointmentById(APPOINTMENT_ID))
                .thenReturn(Optional.of(CANCELLED_APPOINTMENT));
        when(userService.getUserById(DOCTOR_ID))
                .thenReturn(Optional.of(DOCTOR));

        // Act
        indicationService.createIndication(APPOINTMENT_ID, DOCTOR_ID, TEST_DESCRIPTION, null);
    }

    @Test(expected = NotCompletedException.class)
    public void testCreateIndicationNotCompletedConfirmed() throws Exception {
        // Arrange
        when(appointmentService.getAppointmentById(APPOINTMENT_ID))
                .thenReturn(Optional.of(CONFIRMED_APPOINTMENT));
        when(userService.getUserById(DOCTOR_ID))
                .thenReturn(Optional.of(DOCTOR));

        // Act
        indicationService.createIndication(APPOINTMENT_ID, DOCTOR_ID, TEST_DESCRIPTION, null);
    }

    @Test
    public void testGetIndicationsForAppointment() throws AppointmentNotFoundException {
        // Arrange
        when(appointmentService.getAppointmentById(APPOINTMENT_ID))
                .thenReturn(Optional.of(COMPLETED_APPOINTMENT));
        when(indicationDao.getIndicationsForAppointment(APPOINTMENT_ID, PAGE, PAGE_SIZE))
                .thenReturn(INDICATION_PAGE);

        // Act
        Page<Indication> result =
                indicationService.getIndicationsForAppointment(APPOINTMENT_ID, PAGE, PAGE_SIZE);

        // Assert
        assertNotNull(result);
        verify(indicationDao).getIndicationsForAppointment(APPOINTMENT_ID, PAGE, PAGE_SIZE);
    }
}
