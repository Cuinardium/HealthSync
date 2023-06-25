package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.IndicationService;
import ar.edu.itba.paw.interfaces.services.exceptions.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import ar.edu.itba.paw.webapp.auth.UserRole;
import ar.edu.itba.paw.webapp.exceptions.AppointmentForbiddenException;
import ar.edu.itba.paw.webapp.exceptions.AppointmentNotFoundException;
import ar.edu.itba.paw.webapp.form.IndicationForm;
import ar.edu.itba.paw.webapp.form.ModalForm;
import java.util.List;

import ar.edu.itba.paw.webapp.form.ReviewForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class AppointmentController {
  private final AppointmentService appointmentService;

  private final IndicationService indicationService;
  private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentController.class);

  private static final int PAGE_SIZE = 10;

  @Autowired
  public AppointmentController(final AppointmentService appointmentService, IndicationService indicationService) {
    this.appointmentService = appointmentService;
    this.indicationService = indicationService;
  }

  // ==================================  My Appointments   ========================================

  @RequestMapping(value = "/my-appointments", method = RequestMethod.GET)
  public ModelAndView getAppointments(
      @ModelAttribute("modalForm") final ModalForm modalForm,
      @RequestParam(name = "selected_tab", required = false, defaultValue = "1")
          final int selectedTab) {

    ModelAndView mav = new ModelAndView("appointment/appointments");

    boolean isPatient = PawAuthUserDetails.getRole().equals(UserRole.ROLE_PATIENT);
    long userId = PawAuthUserDetails.getCurrentUserId();

    // Get relevant appointments
    List<Appointment> todayAppointments =
        appointmentService
            .getTodayAppointments(userId, AppointmentStatus.CONFIRMED, null, null, isPatient)
            .getContent();

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
    mav.addObject("selectedTab", selectedTab >= 1 && selectedTab <= 4 ? selectedTab : 1);
    mav.addObject("todayAppointments", todayAppointments);
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
    } catch (CancelForbiddenException e) {
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
      @RequestParam(value = "page", required = false, defaultValue = "1") final int page,
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

    // Get Indications
    Page<Indication> indications;

    try {
      indications = indicationService.getIndicationsForAppointment(appointmentId, page - 1, PAGE_SIZE);

      LOGGER.debug("Indications for appointment {} are: {}", appointmentId, indications.getContent());
    } catch (ar.edu.itba.paw.interfaces.services.exceptions.AppointmentNotFoundException e) {
      LOGGER.error(
              "Failed to get indications for appointment {} because appointment was not found",
              appointmentId,
              new AppointmentNotFoundException());

      throw new RuntimeException();
    }

    ModelAndView mav = new ModelAndView("appointment/detailedAppointment");

    // Add values to model
    mav.addObject("appointment", appointment);
    mav.addObject("indications", indications.getContent());
    mav.addObject("currentPage", indications.getCurrentPage() + 1);
    mav.addObject("totalPages", indications.getTotalPages());
    mav.addObject("selectedTab", selectedTab >= 1 && selectedTab <= 3 ? selectedTab : 1);

    return mav;
  }


  // ========================== Review ==========================
  @RequestMapping(value = "/{id:\\d+}/indication", method = RequestMethod.GET)
  public ModelAndView indication(
          @PathVariable("id") final long appointmentId,
          @ModelAttribute("indicationForm") final IndicationForm indicationForm) {

    final ModelAndView mav = new ModelAndView("appointment/indication");
    mav.addObject("showModal", false);
    mav.addObject("appointmentId", appointmentId);
    mav.addObject("indicationForm", indicationForm);

    return mav;
  }

  @RequestMapping(value = "/{id:\\d+}/indication", method = RequestMethod.POST)
  public ModelAndView submitIndication(
          @PathVariable("id") final long appointmentId,
          @Valid @ModelAttribute("indicationForm") final IndicationForm indicationForm,
          final BindingResult errors) {

    if (errors.hasErrors()) {
      return indication(appointmentId, indicationForm);
    }

    Indication indication;

    try {
      indication =
              indicationService.createIndication(
                      appointmentId,
                      PawAuthUserDetails.getCurrentUserId(),
                      indicationForm.getIndications());

      LOGGER.info("Created indication {}", indication);
    } catch (ar.edu.itba.paw.interfaces.services.exceptions.AppointmentNotFoundException e) {
      LOGGER.error(
              "Failed to create indication for appointment {} because appointment was not found",
              appointmentId,
              new AppointmentNotFoundException());

      throw new RuntimeException(e);
    } catch (UserNotFoundException e) {
      LOGGER.error(
              "Failed to create indication for appointment {} because user was not found",
              appointmentId,
              new UserNotFoundException());

      throw new RuntimeException(e);
    }

    final ModelAndView mav = new ModelAndView("appointment/indication");

    mav.addObject("showModal", true);
    mav.addObject("appointmentId", appointmentId);
    mav.addObject("indicationForm", indicationForm);

    return mav;
  }
}
