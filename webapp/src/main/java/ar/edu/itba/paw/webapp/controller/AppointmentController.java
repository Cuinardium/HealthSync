package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import ar.edu.itba.paw.webapp.auth.UserRoles;
import ar.edu.itba.paw.webapp.exceptions.AppointmentForbiddenException;
import ar.edu.itba.paw.webapp.exceptions.AppointmentNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.AppointmentForm;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.IntPredicate;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppointmentController {

  private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentController.class);

  private final AppointmentService appointmentService;
  private final PatientService patientService;
  private final DoctorService doctorService;

  @Autowired
  public AppointmentController(
      final AppointmentService appointmentService,
      final PatientService patientService,
      final DoctorService doctorService) {
    this.appointmentService = appointmentService;
    this.patientService = patientService;
    this.doctorService = doctorService;
  }

  // ========================== Appointment Requests ==========================

  // TODO: revisar porque no tira 404 /-1/appointment
  //                        vvvvvvvv
  @RequestMapping(value = "/{id:\\d+}/appointment", method = RequestMethod.GET)
  public ModelAndView appointmentForm(
      @PathVariable("id") final int doctorId,
      @RequestParam(name = "date", required = false) final String date,
      @RequestParam(name = "desc", required = false, defaultValue = "") final String description,
      @ModelAttribute("appointmentForm") final AppointmentForm appointmentForm) {

    final ModelAndView mav = new ModelAndView("appointment/appointment");

    LocalDate requestedDate = date == null ? LocalDate.now() : LocalDate.parse(date);
    List<ThirtyMinuteBlock> availableHours;

    try {
      availableHours =
          appointmentService.getAvailableHoursForDoctorOnDate(doctorId, requestedDate);
    } catch (RuntimeException e) {
      throw new UserNotFoundException();
    }

    appointmentForm.setDate(requestedDate);
    appointmentForm.setDescription(description);

    mav.addObject("form", appointmentForm);
    mav.addObject("doctorId", doctorId);
    mav.addObject("availableHours", availableHours);

    return mav;
  }

  // this function will return void for now until we figure if we make a new view
  // or use a popup
  @RequestMapping(value = "/{id:\\d+}/appointment", method = RequestMethod.POST)
  public ModelAndView appointmentSubmit(
      @PathVariable("id") final int doctorId,
      @Valid @ModelAttribute("appointmentForm") final AppointmentForm appointmentForm,
      final BindingResult errors,
      Locale locale) {

    if (errors.hasErrors()) {
      return appointmentForm(
          doctorId,
          appointmentForm.getDate().toString(),
          appointmentForm.getDescription(),
          appointmentForm);
    }

    PawAuthUserDetails currentUser =
        (PawAuthUserDetails)
            (SecurityContextHolder.getContext().getAuthentication().getPrincipal());

    try {
      Appointment appointment =
          appointmentService.createAppointment(
              currentUser.getId(),
              doctorId,
              appointmentForm.getDate(),
              appointmentForm.getBlockEnum(),
              appointmentForm.getDescription());

      LOGGER.info("Created {}", appointment);
    } catch (RuntimeException e) {
      // TODO: CORRECT exception handling
      LOGGER.error(
          "Failed to create Appointment for patient {}, {}",
          currentUser.getId(),
          appointmentForm,
          new RuntimeException());
    }

    return appointmentSent();
  }

  @RequestMapping(value = "/appointment_sent", method = RequestMethod.GET)
  public ModelAndView appointmentSent() {
    return new ModelAndView("appointment/appointmentSent");
  }

  // ==================================  My Appointments   ========================================

  @RequestMapping(value = "/my-appointments", method = RequestMethod.GET)
  public ModelAndView getAppointments(
      @RequestParam(name = "from", required = false, defaultValue = "") final String from,
      @RequestParam(name = "to", required = false, defaultValue = "") final String to,
      @RequestParam(name = "selected_tab", required = false, defaultValue = "0")
          final int selectedTab) {

    LocalDate fromDate;
    LocalDate toDate;

    try {
      fromDate = from.isEmpty() ? null : LocalDate.parse(from);
    } catch (DateTimeParseException exception) {
      // TODO: error handling
      return getAppointments("", to, selectedTab);
    }

    try {
      toDate = to.isEmpty() ? null : LocalDate.parse(to);
    } catch (DateTimeParseException exception) {
      // TODO: error handling
      return getAppointments(from, "", selectedTab);
    }

    if (PawAuthUserDetails.getRole().equals(UserRoles.ROLE_PATIENT)) {
      return getAppointmentsForPatient(fromDate, toDate, selectedTab);
    }
    if (PawAuthUserDetails.getRole().equals(UserRoles.ROLE_DOCTOR)) {
      return getAppointmentsForDoctor(fromDate, toDate, selectedTab);
    }
    // TODO: what do i do here????
    return null;
  }

  @RequestMapping(value = "/my-appointments/{id:\\d+}/update", method = RequestMethod.POST)
  public ModelAndView updateAppointment(
      @PathVariable("id") final int appointmentId,
      @RequestParam(name = "status") final int status,
      @RequestParam(name = "from", required = false, defaultValue = "") final String from,
      @RequestParam(name = "to", required = false, defaultValue = "") final String to,
      @RequestParam(name = "selected_tab", required = false, defaultValue = "0")
          final int selectedTab) {

    // TODO: feedback?
    if (status < 0 || status >= AppointmentStatus.values().length) {
      return getAppointments(from, to, selectedTab);
    }

    // A patient can only cancel an appointment
    if (PawAuthUserDetails.getRole().equals(UserRoles.ROLE_PATIENT)
        && status != AppointmentStatus.CANCELLED.ordinal()) {
      return getAppointments(from, to, selectedTab);
    }

    AppointmentStatus appointmentStatus = AppointmentStatus.values()[status];

    try {
      appointmentService.updateAppointmentStatus(
        appointmentId, appointmentStatus, PawAuthUserDetails.getCurrentUserId());
    } catch (RuntimeException e) {
      throw new UserNotFoundException();
    }
    
    return getAppointments(from, to, selectedTab);
  }

  // ================================== Detailed Appointment =======================================

  @RequestMapping(value = "/{id:\\d+}/detailed_appointment", method = RequestMethod.GET)
  public ModelAndView getDetailedAppointment(
      @PathVariable("id") final int appointmentId,
      @RequestParam(name = "selected_tab", required = false, defaultValue = "0")
          final int selectedTab,
      @RequestParam(name = "from", required = false, defaultValue = "") final String from,
      @RequestParam(name = "to", required = false, defaultValue = "") final String to) {

    Appointment appointment =
        appointmentService.getAppointmentById(appointmentId).orElseThrow(AppointmentNotFoundException::new);

    // Get Appointment patient
    Patient patient =
        patientService
            .getPatientById(appointment.getPatientId())
            .orElseThrow(UserNotFoundException::new);

    Doctor doctor =
        doctorService.getDoctorById(appointment.getDoctorId()).orElseThrow(UserNotFoundException::new);

    // If user is nor the patient nor the doctor, unauthorized
    if (PawAuthUserDetails.getCurrentUserId() != patient.getId()
        && PawAuthUserDetails.getCurrentUserId() != doctor.getId()) {
      throw new AppointmentForbiddenException();
    }

    ModelAndView mav = new ModelAndView("appointment/detailedAppointment");

    // Add values to model
    mav.addObject("appointmentDesc", appointment.getDescription());
    mav.addObject(
        "appointmentDateTime",
        appointment.getDate() + " " + appointment.getTimeBlock().getBlockBeginning());
    mav.addObject("appointmentStatusMessageId", appointment.getStatus().getMessageID());
    mav.addObject("patientName", patient.getFirstName() + " " + patient.getLastName());
    mav.addObject("doctorName", doctor.getFirstName() + " " + doctor.getLastName());
    mav.addObject("cityMessageId", doctor.getLocation().getCity().getMessageID());
    mav.addObject("address", doctor.getLocation().getAddress());
    mav.addObject("patientHealthInsuranceMessageId", patient.getHealthInsurance().getMessageID());

    mav.addObject("selectedTab", selectedTab);
    mav.addObject("from", from);
    mav.addObject("to", to);

    return mav;
  }

  // ==================================  Private   =================================================

  private ModelAndView getAppointmentsForDoctor(
      LocalDate fromDate, LocalDate toDate, int selectedTab) {

    ModelAndView mav = new ModelAndView("appointment/appointments");

    // Get relevant appointments
    List<Appointment> upcomingAppointments =
        appointmentService.getFilteredAppointmentsForDoctor(
            PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.ACCEPTED, fromDate, toDate);

    List<Appointment> pendingAppointments =
        appointmentService.getFilteredAppointmentsForDoctor(
            PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.PENDING, fromDate, toDate);

    List<Appointment> cancelledAppointments =
        appointmentService.getFilteredAppointmentsForDoctor(
            PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.CANCELLED, fromDate, toDate);

    List<Appointment> completedAppointments =
        appointmentService.getFilteredAppointmentsForDoctor(
            PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.COMPLETED, fromDate, toDate);

    // Create tabs
    List<AppointmentTab> tabs = new ArrayList<>();

    // Doctor can confirm or reject a pending appointment
    List<AllowedActions> requestAllowedActions = new ArrayList<>();
    requestAllowedActions.add(AllowedActions.CONFIRM);
    requestAllowedActions.add(AllowedActions.REJECT);
    tabs.add(
        new AppointmentTab(
            "requests",
            (x) -> (x <= 0 || x >= 4),
            pendingAppointments,
            requestAllowedActions,
            "appointments.requests"));

    // Doctor can cancel an upcoming appointment
    List<AllowedActions> confirmedAllowedActions = new ArrayList<>();
    confirmedAllowedActions.add(AllowedActions.CANCEL);
    tabs.add(
        new AppointmentTab(
            "confirmed",
            (x) -> (x == 1),
            upcomingAppointments,
            confirmedAllowedActions,
            "appointments.upcoming"));

    // Doctor can only see cancelled and completed appointments
    tabs.add(
        new AppointmentTab(
            "cancelled",
            (x) -> (x == 2),
            cancelledAppointments,
            new ArrayList<>(),
            "appointments.cancelled"));
    tabs.add(
        new AppointmentTab(
            "history",
            (x) -> (x == 3),
            completedAppointments,
            new ArrayList<>(),
            "appointments.history"));

    // Add values to model
    mav.addObject("from", fromDate == null ? "" : fromDate.toString());
    mav.addObject("to", toDate == null ? "" : toDate.toString());

    mav.addObject("selectedTab", selectedTab);
    mav.addObject("tabs", tabs);
    return mav;
  }

  private ModelAndView getAppointmentsForPatient(
      LocalDate fromDate, LocalDate toDate, int selectedTab) {

    ModelAndView mav = new ModelAndView("appointment/appointments");

    // Get relevant appointments
    List<Appointment> upcomingAppointments =
        appointmentService.getFilteredAppointmentsForPatient(
            PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.ACCEPTED, fromDate, toDate);

    List<Appointment> pendingAppointments =
        appointmentService.getFilteredAppointmentsForPatient(
            PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.PENDING, fromDate, toDate);

    List<Appointment> rejectedAppointments =
        appointmentService.getFilteredAppointmentsForPatient(
            PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.REJECTED, fromDate, toDate);

    List<Appointment> cancelledAppointments =
        appointmentService.getFilteredAppointmentsForPatient(
            PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.CANCELLED, fromDate, toDate);

    List<Appointment> completedAppointments =
        appointmentService.getFilteredAppointmentsForPatient(
            PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.COMPLETED, fromDate, toDate);

    // Create tabs
    List<AppointmentTab> tabs = new ArrayList<>();

    // Patient can only see pending appointments
    tabs.add(
        new AppointmentTab(
            "requests",
            (x) -> (x <= 0 || x >= 5),
            pendingAppointments,
            new ArrayList<>(),
            "appointments.pending"));

    // Patient can cancel an upcoming appointment
    List<AllowedActions> confirmedAllowedActions = new ArrayList<>();
    confirmedAllowedActions.add(AllowedActions.CANCEL);
    tabs.add(
        new AppointmentTab(
            "confirmed",
            (x) -> (x == 1),
            upcomingAppointments,
            confirmedAllowedActions,
            "appointments.confirmed"));

    // Patient can only see rejected, cancelled and completed appointments
    tabs.add(
        new AppointmentTab(
            "rejected",
            (x) -> (x == 2),
            rejectedAppointments,
            new ArrayList<>(),
            "appointments.rejected"));
    tabs.add(
        new AppointmentTab(
            "cancelled",
            (x) -> (x == 3),
            cancelledAppointments,
            new ArrayList<>(),
            "appointments.cancelled"));
    tabs.add(
        new AppointmentTab(
            "history",
            (x) -> (x == 4),
            completedAppointments,
            new ArrayList<>(),
            "appointments.history"));

    // Add values to model
    mav.addObject("from", fromDate == null ? "" : fromDate.toString());
    mav.addObject("to", toDate == null ? "" : toDate.toString());

    mav.addObject("selectedTab", selectedTab);
    mav.addObject("tabs", tabs);

    return mav;
  }

  // ==================================  Inner Classes  ===========================================
  public enum AllowedActions {
    CONFIRM(AppointmentStatus.ACCEPTED.ordinal(), "btn-success", "appointments.confirm"),
    REJECT(AppointmentStatus.REJECTED.ordinal(), "btn-danger", "appointments.reject"),
    CANCEL(AppointmentStatus.CANCELLED.ordinal(), "btn-danger", "appointments.cancel");

    private final Integer statusCode;
    private final String buttonClass;
    private final String messageID;

    private AllowedActions(Integer statusCode, String buttonClass, String messageID) {
      this.statusCode = statusCode;
      this.buttonClass = buttonClass;
      this.messageID = messageID;
    }

    public Integer getStatusCode() {
      return statusCode;
    }

    public String getButtonClass() {
      return buttonClass;
    }

    public String getMessageID() {
      return messageID;
    }
  }

  public class AppointmentTab {
    private final String tabName;
    private final IntPredicate intPredicate;
    private final List<Appointment> appointments;
    private final List<AllowedActions> allowedActions;
    private final String messageID;

    private AppointmentTab(
        String tabName,
        IntPredicate intPredicate,
        List<Appointment> appointments,
        List<AllowedActions> allowedActions,
        String messageID) {
      this.tabName = tabName;
      this.intPredicate = intPredicate;
      this.appointments = appointments;
      this.allowedActions = allowedActions;
      this.messageID = messageID;
    }

    public String getTabName() {
      return tabName;
    }

    public List<Appointment> getAppointments() {
      return appointments;
    }

    public List<AllowedActions> getAllowedActions() {
      return allowedActions;
    }

    public boolean isActive(Integer status) {
      return intPredicate.test(status);
    }

    public String getMessageID() {
      return messageID;
    }
  }
}
