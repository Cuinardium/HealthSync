package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.exceptions.ForbiddenCancelException;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import ar.edu.itba.paw.webapp.auth.UserRoles;
import ar.edu.itba.paw.webapp.exceptions.AppointmentForbiddenException;
import ar.edu.itba.paw.webapp.exceptions.AppointmentNotFoundException;
import ar.edu.itba.paw.webapp.form.ModalForm;
import java.util.List;
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
    List<Appointment> upcomingAppointments =
        appointmentService
            .getFilteredAppointments(userId, AppointmentStatus.CONFIRMED, null, null, isPatient)
            .getContent();

    List<Appointment> cancelledAppointments =
        appointmentService
            .getFilteredAppointments(userId, AppointmentStatus.CANCELLED, null, null, isPatient)
            .getContent();

    List<Appointment> completedAppointments =
        appointmentService
            .getFilteredAppointments(userId, AppointmentStatus.COMPLETED, null, null, isPatient)
            .getContent();

    // Add values to model
    mav.addObject("selectedTab", selectedTab >= 1 && selectedTab <= 3 ? selectedTab : 1);
    mav.addObject("upcomingAppointments", upcomingAppointments);
    mav.addObject("cancelledAppointments", cancelledAppointments);
    mav.addObject("completedAppointments", completedAppointments);
    mav.addObject("modalForm", modalForm);

    LOGGER.debug("Patient requested his appointments");

    return mav;
  }

  @RequestMapping(value = "/my-appointments/{id:\\d+}/cancel", method = RequestMethod.POST)
  public ModelAndView cancelAppointment(
      @ModelAttribute("modalForm") final ModalForm modalForm,
      @PathVariable("id") final int appointmentId,
      @RequestParam(name = "selected_tab", required = false, defaultValue = "1")
          final int selectedTab) {

    try {
      Appointment appointment =
          appointmentService.cancelAppointment(
              appointmentId, modalForm.getDescription(), PawAuthUserDetails.getCurrentUserId());

      LOGGER.info("Cancelled {}", appointment);

    } catch (ar.edu.itba.paw.interfaces.services.exceptions.AppointmentNotFoundException e) {
      LOGGER.error("Could not cancel appointment {} because it does not exist", appointmentId);

      throw new AppointmentNotFoundException();
    } catch (ForbiddenCancelException e) {
      LOGGER.error("Could not cancel the appointment {} because user {} is not in the appointment");

      throw new AppointmentForbiddenException();
    }

    return new ModelAndView("redirect:/my-appointments?selected_tab=" + selectedTab);
  }

  // ================================== Detailed Appointment =======================================

  @RequestMapping(value = "/{id:\\d+}/detailed-appointment", method = RequestMethod.GET)
  public ModelAndView getDetailedAppointment(
      @ModelAttribute("modalForm") final ModalForm modalForm,
      @PathVariable("id") final int appointmentId,
      @RequestParam(name = "selected_tab", required = false, defaultValue = "1")
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

    // Add values to model
    mav.addObject("appointment", appointment);
    mav.addObject("selectedTab", selectedTab >= 1 && selectedTab <= 3 ? selectedTab : 1);

    return mav;
  }
}
