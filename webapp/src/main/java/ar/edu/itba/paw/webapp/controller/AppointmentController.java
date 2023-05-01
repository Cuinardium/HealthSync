package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.AppointmentForm;

import java.time.LocalDate;
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
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppointmentController {

  private final DoctorService doctorService;
  private final AppointmentService appointmentService;

  @Autowired
  public AppointmentController(
      final DoctorService doctorService, final AppointmentService appointmentService) {
    this.doctorService = doctorService;
    this.appointmentService = appointmentService;
  }

  // TODO: revisar porque no tira 404 /-1/appointment
  //                        vvvvvvvv
  @RequestMapping(value = "/{id:\\d+}/appointment", method = RequestMethod.GET)
  public ModelAndView appointmentForm(
      @PathVariable("id") final int medicId,
      @ModelAttribute("appointmentForm") final AppointmentForm appointmentForm) {

    Doctor doctor = doctorService.getDoctorById(medicId).orElseThrow(UserNotFoundException::new);

    String email = doctor.getEmail();
    String address = doctor.getLocation().getAddress();
    String city = doctor.getLocation().getCity().getMessageID();

    final ModelAndView mav = new ModelAndView("appointment/appointment");

    mav.addObject("form", appointmentForm);
    mav.addObject("medicId", medicId);
    mav.addObject("email", email);
    mav.addObject("address", address);
    mav.addObject("city", city);

    return mav;
  }

  // this function will return void for now until we figure if we make a new view
  // or use a popup
  @RequestMapping(value = "/{id:\\d+}/appointment", method = RequestMethod.POST)
  public ModelAndView appointmentSubmit(
      @PathVariable("id") final int medicId,
      @Valid @ModelAttribute("appointmentForm") final AppointmentForm appointmentForm,
      final BindingResult errors,
      Locale locale) {

    if (errors.hasErrors()) {
      return appointmentForm(medicId, appointmentForm);
    }


    PawAuthUserDetails currentUser = (PawAuthUserDetails) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());

    try {
      appointmentService.createAppointment(
        currentUser.getId(),
        medicId,
        LocalDate.now(),
        ThirtyMinuteBlock.BLOCK_00_30,
        appointmentForm.getDescription()
      );
      
    } catch (RuntimeException e) {
      // TODO: CORRECT exception handling
    }

    return appointmentSent();
  }

  @RequestMapping(value = "/appointment_sent", method = RequestMethod.GET)
  public ModelAndView appointmentSent() {
    return new ModelAndView("appointment/appointmentSent");
  }
}
