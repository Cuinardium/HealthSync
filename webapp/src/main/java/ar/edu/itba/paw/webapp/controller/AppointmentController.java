package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import ar.edu.itba.paw.webapp.auth.UserRoles;
import ar.edu.itba.paw.webapp.exceptions.AppointmentForbiddenException;
import ar.edu.itba.paw.webapp.exceptions.AppointmentNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntPredicate;
import javax.validation.Valid;

import ar.edu.itba.paw.webapp.form.DoctorEditForm;
import ar.edu.itba.paw.webapp.form.ModalForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppointmentController {
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

  // ==================================  My Appointments   ========================================

  @RequestMapping(value = "/my-appointments", method = RequestMethod.GET)
  public ModelAndView getAppointments(
          @ModelAttribute("modalForm") final ModalForm modalForm,
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
      return getAppointments(modalForm,"", to, selectedTab);
    }

    try {
      toDate = to.isEmpty() ? null : LocalDate.parse(to);
    } catch (DateTimeParseException exception) {
      // TODO: error handling
      return getAppointments(modalForm,from, "", selectedTab);
    }

    if (PawAuthUserDetails.getRole().equals(UserRoles.ROLE_PATIENT)) {
      ModelAndView mav=getAppointmentsForPatient(fromDate, toDate, selectedTab);
      mav.addObject("modalForm", modalForm);
      return mav;
    }
    if (PawAuthUserDetails.getRole().equals(UserRoles.ROLE_DOCTOR)) {
      ModelAndView mav= getAppointmentsForDoctor( fromDate, toDate, selectedTab);
      mav.addObject("modalForm", modalForm);
      return mav;
    }
    // TODO: what do i do here????
    return null;
  }

  @RequestMapping(value = "/my-appointments/{id:\\d+}/update", method = RequestMethod.POST)
  public ModelAndView updateAppointment(
          @ModelAttribute("modalForm") final ModalForm modalForm,
      @PathVariable("id") final int appointmentId,
      @RequestParam(name = "status") final int status,
      @RequestParam(name = "from", required = false, defaultValue = "") final String from,
      @RequestParam(name = "to", required = false, defaultValue = "") final String to,
      @RequestParam(name = "selected_tab", required = false, defaultValue = "0")
          final int selectedTab) {

    // TODO: feedback?
    if (status < 0 || status >= AppointmentStatus.values().length) {
      return getAppointments(modalForm,from, to, selectedTab);
    }

    // A patient can only cancel an appointment
    if (PawAuthUserDetails.getRole().equals(UserRoles.ROLE_PATIENT)
        && status != AppointmentStatus.CANCELLED.ordinal()) {
      return getAppointments(modalForm,from, to, selectedTab);
    }

    AppointmentStatus appointmentStatus = AppointmentStatus.values()[status];

    try {
      appointmentService.updateAppointmentStatus(
        appointmentId, appointmentStatus, modalForm.getDescription(), PawAuthUserDetails.getCurrentUserId());
    } catch (RuntimeException e) {
      throw new UserNotFoundException();
    }
    
    return getAppointments(modalForm, from, to, selectedTab);
  }

  // ================================== Detailed Appointment =======================================

  @RequestMapping(value = "/{id:\\d+}/detailed_appointment", method = RequestMethod.GET)
  public ModelAndView getDetailedAppointment(
          @ModelAttribute("modalForm") final ModalForm modalForm,
      @PathVariable("id") final int appointmentId,
      @RequestParam(name = "selected_tab", required = false, defaultValue = "0")
          final int selectedTab,
      @RequestParam(name = "from", required = false, defaultValue = "") final String from,
      @RequestParam(name = "to", required = false, defaultValue = "") final String to) {

    Appointment appointment =
        appointmentService
            .getAppointmentById(appointmentId)
            .orElseThrow(AppointmentNotFoundException::new);

    // Get Appointment patient
    Patient patient =
        patientService
            .getPatientById(appointment.getPatientId())
            .orElseThrow(UserNotFoundException::new);

    Doctor doctor =
        doctorService
            .getDoctorById(appointment.getDoctorId())
            .orElseThrow(UserNotFoundException::new);

    // If user is nor the patient nor the doctor, unauthorized
    if (PawAuthUserDetails.getCurrentUserId() != patient.getId()
        && PawAuthUserDetails.getCurrentUserId() != doctor.getId()) {
      throw new AppointmentForbiddenException();
    }

    ModelAndView mav = new ModelAndView("appointment/detailedAppointment");
    List<AllowedActions> allowedActions= new ArrayList<>();
    if(appointment.getStatus().equals(AppointmentStatus.PENDING)){
      if(PawAuthUserDetails.getCurrentUserId()==doctor.getId()){
        allowedActions.add(AllowedActions.CONFIRM);
        allowedActions.add(AllowedActions.REJECT);
      }
    }
    else if(appointment.getStatus().equals(AppointmentStatus.ACCEPTED)){
      allowedActions.add(AllowedActions.CANCEL);
    }

    // Add values to model
    mav.addObject("appointmentId", appointmentId);
    mav.addObject("actions", allowedActions);
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
            PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.ACCEPTED, fromDate, toDate, -1, -1).getContent();

    List<Appointment> pendingAppointments =
        appointmentService.getFilteredAppointmentsForDoctor(
            PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.PENDING, fromDate, toDate, -1, -1).getContent();

    List<Appointment> cancelledAppointments =
        appointmentService.getFilteredAppointmentsForDoctor(
            PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.CANCELLED, fromDate, toDate, -1 ,-1).getContent();

    List<Appointment> completedAppointments =
        appointmentService.getFilteredAppointmentsForDoctor(
            PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.COMPLETED, fromDate, toDate, -1, -1).getContent();

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
            PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.ACCEPTED, fromDate, toDate, 0, 100).getContent();

    List<Appointment> pendingAppointments =
        appointmentService.getFilteredAppointmentsForPatient(
            PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.PENDING, fromDate, toDate, 0, 100).getContent();

    List<Appointment> rejectedAppointments =
        appointmentService.getFilteredAppointmentsForPatient(
            PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.REJECTED, fromDate, toDate, 0, 100).getContent();

    List<Appointment> cancelledAppointments =
        appointmentService.getFilteredAppointmentsForPatient(
            PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.CANCELLED, fromDate, toDate, 0, 100).getContent();

    List<Appointment> completedAppointments =
        appointmentService.getFilteredAppointmentsForPatient(
            PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.COMPLETED, fromDate, toDate, 0, 100).getContent();

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
