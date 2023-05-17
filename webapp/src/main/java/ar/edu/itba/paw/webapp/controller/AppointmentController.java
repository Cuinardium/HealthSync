package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import ar.edu.itba.paw.webapp.auth.UserRoles;
import ar.edu.itba.paw.webapp.exceptions.AppointmentForbiddenException;
import ar.edu.itba.paw.webapp.exceptions.AppointmentNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.ModalForm;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppointmentController {
  private final AppointmentService appointmentService;
  private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentController.class);

  @Autowired
  public AppointmentController(final AppointmentService appointmentService) {
    this.appointmentService = appointmentService;
  }

  // ==================================  My Appointments   ========================================

  @RequestMapping(value = "/my-appointments", method = RequestMethod.GET)
  public ModelAndView getAppointments(
      @ModelAttribute("modalForm") final ModalForm modalForm,
      @RequestParam(name = "selected_tab", required = false, defaultValue = "1")
          final int selectedTab) {

    ModelAndView mav = new ModelAndView("appointment/appointments");

    boolean isPatient = PawAuthUserDetails.getRole().equals(UserRoles.ROLE_PATIENT);
    long userId = PawAuthUserDetails.getCurrentUserId();

    // Get relevant appointments
    List<Appointment> upcomingAppointments = appointmentService.getFilteredAppointments(userId, AppointmentStatus.CONFIRMED, null, null, isPatient).getContent();
       
    List<Appointment> cancelledAppointments = appointmentService.getFilteredAppointments(userId, AppointmentStatus.CANCELLED, null, null, isPatient).getContent();
        
    List<Appointment> completedAppointments = appointmentService.getFilteredAppointments(userId, AppointmentStatus.COMPLETED, null, null, isPatient).getContent();
        
    // Create tabs
    List<AppointmentTab> tabs = new ArrayList<>();

    // User can cancel an upcoming appointment
    List<AllowedActions> confirmedAllowedActions = new ArrayList<>();
    confirmedAllowedActions.add(AllowedActions.CANCEL);
    tabs.add(
        new AppointmentTab(
            "confirmed",
            (x) -> (x == 1),
            upcomingAppointments,
            confirmedAllowedActions,
            "appointments.upcoming"));
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
    mav.addObject("selectedTab", selectedTab);
    mav.addObject("tabs", tabs);
    mav.addObject("modalForm", modalForm);

    LOGGER.debug("Patient requested his appointments");

    return mav;
  }

  @RequestMapping(value = "/my-appointments/{id:\\d+}/update", method = RequestMethod.POST)
  public ModelAndView updateAppointment(
      @ModelAttribute("modalForm") final ModalForm modalForm,
      @PathVariable("id") final int appointmentId,
      @RequestParam(name = "status") final int status,
      @RequestParam(name = "selected_tab", required = false, defaultValue = "0")
          final int selectedTab) {

    // TODO: feedback?
    if (status < 0 || status >= AppointmentStatus.values().length) {
      return getAppointments(modalForm, selectedTab);
    }

    // A patient can only cancel an appointment
    if (PawAuthUserDetails.getRole().equals(UserRoles.ROLE_PATIENT)
        && status != AppointmentStatus.CANCELLED.ordinal()) {
      return getAppointments(modalForm, selectedTab);
    }

    AppointmentStatus appointmentStatus = AppointmentStatus.values()[status];

    try {
      Appointment appointment =
          appointmentService.updateAppointment(
              appointmentId,
              appointmentStatus,
              modalForm.getDescription(),
              PawAuthUserDetails.getCurrentUserId());
      LOGGER.info("Updated {}", appointment);
    } catch (RuntimeException e) {
      LOGGER.error("Appointment could not be updated, because user did not exist");
      throw new UserNotFoundException();
    }

    return getAppointments(modalForm, selectedTab);
  }

  // ================================== Detailed Appointment =======================================

  @RequestMapping(value = "/{id:\\d+}/detailed_appointment", method = RequestMethod.GET)
  public ModelAndView getDetailedAppointment(
      @ModelAttribute("modalForm") final ModalForm modalForm,
      @PathVariable("id") final int appointmentId,
      @RequestParam(name = "selected_tab", required = false, defaultValue = "0")
          final int selectedTab) {

    Appointment appointment =
        appointmentService
            .getAppointmentById(appointmentId)
            .orElseThrow(AppointmentNotFoundException::new);

    // If user is neither the patient nor the doctor, unauthorized
    if (PawAuthUserDetails.getCurrentUserId() != appointment.getPatient().getId()
        && PawAuthUserDetails.getCurrentUserId() != appointment.getDoctor().getId()) {
      throw new AppointmentForbiddenException();
    }

    ModelAndView mav = new ModelAndView("appointment/detailedAppointment");
    List<AllowedActions> allowedActions = new ArrayList<>();

    if (appointment.getStatus().equals(AppointmentStatus.CONFIRMED)) {
      allowedActions.add(AllowedActions.CANCEL);
    }

    // Add values to model
    mav.addObject("appointmentId", appointmentId);
    mav.addObject("actions", allowedActions);
    mav.addObject("appointmentDesc", appointment.getDescription());
    mav.addObject("cancelDescription", appointment.getCancelDesc());
    mav.addObject(
        "appointmentDateTime",
        appointment.getDate() + " " + appointment.getTimeBlock().getBlockBeginning());
    mav.addObject("appointmentStatusMessageId", appointment.getStatus().getMessageID());
    mav.addObject(
        "patientName",
        appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName());
    mav.addObject(
        "doctorName",
        appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName());
    mav.addObject("cityMessageId", appointment.getDoctor().getLocation().getCity().getMessageID());
    mav.addObject("address", appointment.getDoctor().getLocation().getAddress());
    mav.addObject(
        "patientHealthInsuranceMessageId",
        appointment.getPatient().getHealthInsurance().getMessageID());

    mav.addObject("selectedTab", selectedTab);

    return mav;
  }

  // ==================================  Private   =================================================

  // ==================================  Inner Classes  ===========================================
  public enum AllowedActions {
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
