package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.form.DoctorRegisterForm;
import ar.edu.itba.paw.webapp.form.LoginForm;
import ar.edu.itba.paw.webapp.form.PatientRegisterForm;
import java.util.Arrays;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

  private final DoctorService doctorService;

  private final PatientService patientService;

  @Autowired
  public AuthController(final DoctorService doctorService, final PatientService patientService) {
    this.doctorService = doctorService;
    this.patientService = patientService;
  }

  // @RequestMapping(value = "/login", method = RequestMethod.POST)
  // public ModelAndView login(
  //     @Valid @ModelAttribute("loginForm") final LoginForm loginForm, final BindingResult errors)
  // {
  //
  //   // TODO: IF LOGIN UNSUCCESFULL
  //   // show errors in view
  //   // return login form
  //   if (errors.hasErrors() /* || login unsuccesfull*/) {
  //     return loginForm(loginForm);
  //   }
  //
  //   // TODO: CHECK IF LOGIN SUCCESFULL
  //   final ModelAndView mav = new ModelAndView("home/doctorDashboard");
  //   return mav;
  // }

  @RequestMapping(value = "/login")
  public ModelAndView loginForm(@ModelAttribute("loginForm") final LoginForm loginForm) {
    return new ModelAndView("auth/login");
  }

  @RequestMapping(value = "/logout", method = RequestMethod.POST)
  public ModelAndView logout() {
    // TODO: Log out logic here
    //
    return new ModelAndView("home/home");
  }

  // TODO: register_succesful -> /verify?? should reroute to login?
  @RequestMapping(value = "/register_succesful", method = RequestMethod.GET)
  public ModelAndView registerSuccesful() {
    final ModelAndView mav = new ModelAndView("auth/registerSuccesful");
    return mav;
  }

  // register user?
  @RequestMapping(value = "/patient-register", method = RequestMethod.POST)
  public ModelAndView patientRegisterSubmit(
      @Valid @ModelAttribute("patientRegisterForm") final PatientRegisterForm patientRegisterForm,
      final BindingResult errors) {
    if (errors.hasErrors()) {
      return patientRegister(patientRegisterForm);
    }

    final User user =
        patientService.createPatient(
            patientRegisterForm.getEmail(),
            patientRegisterForm.getPassword(),
            patientRegisterForm.getName(),
            patientRegisterForm.getLastname(),
            patientRegisterForm.getHealthInsuranceCode());
    final ModelAndView mav = new ModelAndView("auth/registerSuccesful");
    mav.addObject("user", user);
    return mav;
  }

  // register user?
  @RequestMapping(value = "/patient-register", method = RequestMethod.GET)
  public ModelAndView patientRegister(
      @ModelAttribute("patientRegisterForm") final PatientRegisterForm patientRegisterForm) {
    final ModelAndView mav = new ModelAndView("auth/patientRegister");
    mav.addObject("form", patientRegisterForm);
    mav.addObject("healthInsurances", Arrays.asList(HealthInsurance.values()));

    return mav;
  }

  // TODO: revisar campos
  @RequestMapping(value = "/doctor-register", method = RequestMethod.POST)
  public ModelAndView doctorRegisterSubmit(
      @Valid @ModelAttribute("doctorRegisterForm") final DoctorRegisterForm doctorRegisterForm,
      final BindingResult errors) {
    if (errors.hasErrors()) {
      return doctorRegisterForm(doctorRegisterForm);
    }
    final User user;
    user =
        doctorService.createDoctor(
            doctorRegisterForm.getEmail(),
            doctorRegisterForm.getPassword(),
            doctorRegisterForm.getName(),
            doctorRegisterForm.getLastname(),
            doctorRegisterForm.getHealthInsuranceCode(),
            doctorRegisterForm.getSpecialtyCode(),
            doctorRegisterForm.getCityCode(),
            doctorRegisterForm.getAddress(),
            AttendingHours.DEFAULT_ATTENDING_HOURS);

    final ModelAndView mav = new ModelAndView("auth/registerSuccesful");
    mav.addObject("user", user);
    return mav;
  }

  @RequestMapping(value = "/doctor-register", method = RequestMethod.GET)
  public ModelAndView doctorRegisterForm(
      @ModelAttribute("doctorRegisterForm") final DoctorRegisterForm doctorRegisterForm) {
    final ModelAndView mav = new ModelAndView("auth/doctorRegister");
    mav.addObject("form", doctorRegisterForm);
    mav.addObject("cities", Arrays.asList(City.values()));
    mav.addObject("specialties", Arrays.asList(Specialty.values()));
    mav.addObject("healthInsurances", Arrays.asList(HealthInsurance.values()));
    return mav;
  }
}
