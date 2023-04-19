package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.MailService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.*;
import java.util.List;
import java.util.Locale;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloWorldController {

  private final UserService userService;
  private final DoctorService doctorService;
  private final MailService mailService;

  @Autowired
  public HelloWorldController(
      final UserService userService,
      final MailService mailService,
      final DoctorService doctorService) {
    this.userService = userService;
    this.mailService = mailService;
    this.doctorService = doctorService;
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ModelAndView landingPage() {
    return new ModelAndView("helloworld/home");
  }

  @RequestMapping("/{id:\\d+}")
  public ModelAndView profile(@PathVariable("id") final long userId) {
    final ModelAndView mav = new ModelAndView("helloworld/profile");
    mav.addObject("user", userService.findById(userId).orElseThrow(UserNotFoundException::new));

    return mav;
  }

  // TODO: revisar porque no tira 404 /-1/appointment
  //                        vvvvvvvv
  @RequestMapping(value = "/{id:\\d+}/appointment", method = RequestMethod.GET)
  public ModelAndView appointmentForm(
      @PathVariable("id") final int medicId,
      @ModelAttribute("appointmentForm") final AppointmentForm appointmentForm) {

    String email =
        doctorService.getDoctorById(medicId).orElseThrow(UserNotFoundException::new).getEmail();

    final ModelAndView mav = new ModelAndView("helloworld/appointment");

    mav.addObject("form", appointmentForm);
    mav.addObject("medicId", medicId);
    mav.addObject("email", email);

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

    return appointmentSent();
  }

  @RequestMapping(value = "/doctorDashboard", method = RequestMethod.GET)
  public ModelAndView doctorDashboard(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "city", required = false) String city,
      @RequestParam(value = "specialty", required = false) String specialty,
      @RequestParam(value = "healthcare", required = false) String healthcare) {
    final ModelAndView mav = new ModelAndView("helloworld/doctorDashboard");

    // If the parameter is empty, set it to null
    name = name == null || name.isEmpty() ? null : name;
    specialty = specialty == null || specialty.isEmpty() ? null : specialty;
    city = city == null || city.isEmpty() ? null : city;
    healthcare = healthcare == null || healthcare.isEmpty() ? null : healthcare;

    List<Doctor> doctors = doctorService.getFilteredDoctors(name, specialty, city, healthcare);

    mav.addObject("doctors", doctors);

    return mav;
  }

  @RequestMapping(value = "/appointment_sent", method = RequestMethod.GET)
  public ModelAndView appointmentSent() {
    return new ModelAndView("helloworld/appointmentSent");
  }
}
