package ar.edu.itba.paw.webapp.controller;

import java.util.Locale;

import javax.validation.Valid;

import ar.edu.itba.paw.models.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.MailService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.AppointmentForm;

@Controller
public class AppointmentController {

  
  private final UserService userService;
  private final DoctorService doctorService;
  private final MailService mailService;

  @Autowired
  public AppointmentController(
      final UserService userService,
      final MailService mailService,
      final DoctorService doctorService) {
    this.userService = userService;
    this.mailService = mailService;
    this.doctorService = doctorService;
  }

  // TODO: revisar porque no tira 404 /-1/appointment
  //                        vvvvvvvv
  @RequestMapping(value = "/{id:\\d+}/appointment", method = RequestMethod.GET)
  public ModelAndView appointmentForm(
      @PathVariable("id") final int medicId,
      @ModelAttribute("appointmentForm") final AppointmentForm appointmentForm) {

    Doctor doctor =
        doctorService.getDoctorById(medicId).orElseThrow(UserNotFoundException::new);

    String email= doctor.getEmail();
    String address= doctor.getLocation().getAddress();
    String city= doctor.getLocation().getCity().getMessageID();

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

    try {
      userService.createUser(
          appointmentForm.getEmail(),
          appointmentForm.getName(),
          appointmentForm.getLastname(),
          appointmentForm.getHealthcare());
    } catch (RuntimeException e) {
      // TODO: CORRECT exception handling
    }
    mailService.sendAppointmentRequestMail(
        appointmentForm.getEmail(),
        appointmentForm.getDocEmail(),
        appointmentForm.getName() + " " + appointmentForm.getLastname(),
        appointmentForm.getHealthcare(),
        appointmentForm.getDate(),
        appointmentForm.getDescription(),
        locale);
    mailService.sendAppointmentReminderMail(appointmentForm.getEmail(),
            appointmentForm.getDocEmail(),
            appointmentForm.getAddress(),
            appointmentForm.getCity(),
            appointmentForm.getName() + " " + appointmentForm.getLastname(),
            appointmentForm.getHealthcare(),
            appointmentForm.getDate(),
            appointmentForm.getDescription(),
            locale);
    return appointmentSent();
  }

  @RequestMapping(value = "/appointment_sent", method = RequestMethod.GET)
  public ModelAndView appointmentSent() {
    return new ModelAndView("appointment/appointmentSent");
  }
}
