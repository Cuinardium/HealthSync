package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.form.DoctorRegisterForm;
import ar.edu.itba.paw.webapp.form.LoginForm;
import ar.edu.itba.paw.webapp.form.PatientRegisterForm;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

  private final DoctorService doctorService;

  private final PatientService patientService;

  private final AuthenticationManager authenticationManager;

  @Autowired
  public AuthController(
      final DoctorService doctorService,
      final PatientService patientService,
      AuthenticationManager authenticationManager) {
    this.doctorService = doctorService;
    this.patientService = patientService;
    this.authenticationManager = authenticationManager;
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
  public ModelAndView loginForm(
      @ModelAttribute("loginForm") final LoginForm loginForm,
      @RequestParam(value = "error", required = false) String error) {

    ModelAndView mav = new ModelAndView("auth/login");

    // si ?error no esta -> error es null, en cambio si ?error esta -> error es un string vacio
    mav.addObject("hasError", error != null);
    return mav;
  }

  @RequestMapping(value = "/logout", method = RequestMethod.POST)
  public ModelAndView logout() {
    // TODO: Log out logic here
    //
    return new ModelAndView("home/home");
  }

  // register user?
  @RequestMapping(value = "/patient-register", method = RequestMethod.POST)
  public ModelAndView patientRegisterSubmit(
      @Valid @ModelAttribute("patientRegisterForm") final PatientRegisterForm patientRegisterForm,
      final BindingResult errors) {
    if (errors.hasErrors()) {
      return patientRegister(patientRegisterForm);
    }

    // TODO: check for exceptions
    final Patient patient =
        patientService.createPatient(
            patientRegisterForm.getEmail(),
            patientRegisterForm.getPassword(),
            patientRegisterForm.getName(),
            patientRegisterForm.getLastname(),
            patientRegisterForm.getHealthInsuranceCode());

    LOGGER.info("Registered {}", patient);
    authUser(patient.getEmail(), patient.getPassword());

    final ModelAndView mav = new ModelAndView("components/operationSuccessful");
    mav.addObject("showHeader", false);
    mav.addObject("user", patient);
    mav.addObject("operationTitle", "registerMedic.registerSuccessfulTitle");
    mav.addObject("operationMsg", "registerMedic.registerSuccessfulMsg");
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

    // TODO: check for exceptions
    final Doctor doctor =
        doctorService.createDoctor(
            doctorRegisterForm.getEmail(),
            doctorRegisterForm.getPassword(),
            doctorRegisterForm.getName(),
            doctorRegisterForm.getLastname(),
            doctorRegisterForm.getHealthInsuranceCodes(),
            doctorRegisterForm.getSpecialtyCode(),
            doctorRegisterForm.getCityCode(),
            doctorRegisterForm.getAddress(),
            AttendingHours.DEFAULT_ATTENDING_HOURS);

    LOGGER.info("Registered {}", doctor);
    authUser(doctor.getEmail(), doctor.getPassword());

    final ModelAndView mav = new ModelAndView("components/operationSuccessful");
    mav.addObject("showHeader", false);
    mav.addObject("user", doctor);
    mav.addObject("operationTitle", "registerMedic.registerSuccessfulTitle");
    mav.addObject("operationMsg", "registerMedic.registerSuccessfulMsg");
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

  private void authUser(String username, String password) {
    UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(username, password);
    Authentication authentication = authenticationManager.authenticate(authToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
