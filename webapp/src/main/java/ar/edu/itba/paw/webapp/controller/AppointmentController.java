package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import ar.edu.itba.paw.webapp.auth.UserRoles;
import ar.edu.itba.paw.webapp.form.AppointmentForm;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
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

  @Autowired
  public AppointmentController(final AppointmentService appointmentService) {
    this.appointmentService = appointmentService;
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

    List<ThirtyMinuteBlock> availableHours =
        appointmentService.getAvailableHoursForDoctorOnDate(doctorId, requestedDate);

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
  public ModelAndView getAppointments() {
    if (PawAuthUserDetails.getRole().equals(UserRoles.ROLE_PATIENT)) {
      return getAppointmentsForPatient();
    }
    if (PawAuthUserDetails.getRole().equals(UserRoles.ROLE_DOCTOR)) {
      return getAppointmentsForDoctor();
    }
    // TODO: what do i do here????
    return null;
  }

  @RequestMapping(value = "/my-appointments/{id:\\d+}/update", method = RequestMethod.POST)
  public ModelAndView updateAppointment(
      @PathVariable("id") final int appointmentId,
      @RequestParam(name = "status") final int status) {

    // TODO: feedback?
    if (status < 0 || status >= AppointmentStatus.values().length) {
      return getAppointments();
    }

    // A patient can only cancel an appointment
    if (PawAuthUserDetails.getRole().equals(UserRoles.ROLE_PATIENT)
        && status != AppointmentStatus.CANCELLED.ordinal()) {
      return getAppointments();
    }

    AppointmentStatus appointmentStatus = AppointmentStatus.values()[status];

    appointmentService.updateAppointmentStatus(appointmentId, appointmentStatus, PawAuthUserDetails.getCurrentUserId());

    return getAppointments();
  }

  // ==================================  Private   =================================================

  private ModelAndView getAppointmentsForDoctor() {

    ModelAndView mav = new ModelAndView("appointment/doctorAppointments");

    List<Appointment> upcomingAppointments =
        appointmentService.getAppointmentsForDoctorByStatus(
            PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.ACCEPTED);

    List<Appointment> pendingAppointments =
        appointmentService.getAppointmentsForDoctorByStatus(
            PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.PENDING);

    List<Appointment> cancelledAppointments =
        appointmentService.getAppointmentsForDoctorByStatus(
            PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.CANCELLED);

    List<Appointment> completedAppointments =
        appointmentService.getAppointmentsForDoctorByStatus(
            PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.COMPLETED);

    mav.addObject("upcomingAppointments", upcomingAppointments);
    mav.addObject("pendingAppointments", pendingAppointments);
    mav.addObject("cancelledAppointments", cancelledAppointments);
    mav.addObject("completedAppointments", completedAppointments);

    return mav;
  }

  private ModelAndView getAppointmentsForPatient() {

    ModelAndView mav = new ModelAndView("appointment/patientAppointments");

    List<Appointment> upcomingAppointments =
        appointmentService.getAppointmentsForPatientByStatus(
            PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.ACCEPTED);

    List<Appointment> pendingAppointments =
        appointmentService.getAppointmentsForPatientByStatus(
            PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.PENDING);

    List<Appointment> rejectedAppointments =
    appointmentService.getAppointmentsForPatientByStatus(
        PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.REJECTED);

    List<Appointment> cancelledAppointments =
        appointmentService.getAppointmentsForPatientByStatus(
            PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.CANCELLED);

    List<Appointment> completedAppointments =
        appointmentService.getAppointmentsForPatientByStatus(
            PawAuthUserDetails.getCurrentUserId(), AppointmentStatus.COMPLETED);

    mav.addObject("upcomingAppointments", upcomingAppointments);
    mav.addObject("pendingAppointments", pendingAppointments);
    mav.addObject("cancelledAppointments", cancelledAppointments);
    mav.addObject("rejectedAppointments", rejectedAppointments);
    mav.addObject("completedAppointments", completedAppointments);

    return mav;
  }
}
