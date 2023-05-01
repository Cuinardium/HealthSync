package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import ar.edu.itba.paw.webapp.auth.UserRoles;
import ar.edu.itba.paw.webapp.form.AppointmentForm;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.validation.Valid;
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

  private final AppointmentService appointmentService;

  @Autowired
  public AppointmentController(final AppointmentService appointmentService) {
    this.appointmentService = appointmentService;
  }

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
      appointmentService.createAppointment(
          currentUser.getId(),
          doctorId,
          appointmentForm.getDate(),
          appointmentForm.getBlockEnum(),
          appointmentForm.getDescription());

    } catch (RuntimeException e) {
      // TODO: CORRECT exception handling
    }

    return appointmentSent();
  }

  @RequestMapping(value = "/appointment_sent", method = RequestMethod.GET)
  public ModelAndView appointmentSent() {
    return new ModelAndView("appointment/appointmentSent");
  }

  @RequestMapping(value = "/my-appointments", method = RequestMethod.GET)
  public ModelAndView getAppointments() {
    ModelAndView mav = new ModelAndView("appointment/myAppointments");

    List<Appointment> appointments = getAppointmentsForCurrentUser();
    mav.addObject("appointments", appointments);
    return mav;
  }

  private List<Appointment> getAppointmentsForCurrentUser() {
    if(PawAuthUserDetails.getRole().equals(UserRoles.ROLE_PATIENT)){
        return appointmentService.getAppointmentsForPatient(PawAuthUserDetails.getCurrentUserId());
    }
    if(PawAuthUserDetails.getRole().equals(UserRoles.ROLE_DOCTOR)){
      return appointmentService.getAppointmentsForDoctor(PawAuthUserDetails.getCurrentUserId());
    }
    return null;
  }
}
