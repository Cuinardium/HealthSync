package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.MailService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.User;
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

  @RequestMapping(value = "/register_succesful", method = RequestMethod.GET)
  public ModelAndView helloWorld() {
    final ModelAndView mav = new ModelAndView("registerSuccesful");
    mav.addObject("user", new User(0, "hello hello", "hello", "hrlo", "hello", true, 1));

    return mav;
  }

  @RequestMapping("/{id}")
  public ModelAndView profile(@PathVariable("id") final long userId) {
    final ModelAndView mav = new ModelAndView("helloworld/profile");
    mav.addObject("user", userService.findById(userId).orElseThrow(UserNotFoundException::new));

    return mav;
  }

  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public ModelAndView register(
      @Valid @ModelAttribute("registerForm") final RegisterForm registerForm,
      final BindingResult errors) {
    if (errors.hasErrors()) {
      return registerForm(registerForm);
    }

    final User user =
        userService.createUser(registerForm.getEmail(), registerForm.getPassword(), "", "");

    final ModelAndView mav = new ModelAndView("registerSuccesful");
    mav.addObject("user", user);
    return mav;
  }

  @RequestMapping(value = "/register", method = RequestMethod.GET)
  public ModelAndView registerForm(
      @ModelAttribute("registerForm") final RegisterForm registerForm) {
    final ModelAndView mav = new ModelAndView("helloworld/register");
    mav.addObject("form", registerForm);

    return mav;
  }

  // TODO: revisar campos
  @RequestMapping(value = "/register_medic", method = RequestMethod.POST)
  public ModelAndView registerMedicSubmit(
      @Valid @ModelAttribute("medicRegisterForm") final MedicRegisterForm medicRegisterForm,
      final BindingResult errors) {
    if (errors.hasErrors()) {
      return registerMedicForm(medicRegisterForm);
    }
    final User user;
    try {
      user =
          doctorService.createDoctor(
              medicRegisterForm.getEmail(),
              medicRegisterForm.getPassword(),
              medicRegisterForm.getName(),
              medicRegisterForm.getLastname(),
              medicRegisterForm.getHealthcare(),
              medicRegisterForm.getSpecialization(),
              medicRegisterForm.getCity(),
              medicRegisterForm.getAddress());
    } catch (RuntimeException e) {
      // TODO: coorect exception handling and show error msg for repeated medic email
      return registerMedicForm(medicRegisterForm);
    }

    final ModelAndView mav = new ModelAndView("helloworld/registerSuccesful");
    mav.addObject("user", user);
    return mav;
  }

  @RequestMapping(value = "/register_medic", method = RequestMethod.GET)
  public ModelAndView registerMedicForm(
      @ModelAttribute("medicRegisterForm") final MedicRegisterForm medicRegisterForm) {
    final ModelAndView mav = new ModelAndView("helloworld/register_medic");
    mav.addObject("form", medicRegisterForm);
    return mav;
  }

  @RequestMapping(value = "/{id}/appointment", method = RequestMethod.GET)
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
  @RequestMapping(value = "/{id}/appointment", method = RequestMethod.POST)
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
          appointmentForm.getEmail(), appointmentForm.getName(), appointmentForm.getLastname(), appointmentForm.getHealthcare());
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
