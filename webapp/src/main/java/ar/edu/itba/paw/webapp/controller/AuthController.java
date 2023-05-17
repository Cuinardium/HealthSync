package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.form.DoctorRegisterForm;
import ar.edu.itba.paw.webapp.form.LoginForm;
import ar.edu.itba.paw.webapp.form.PatientRegisterForm;
import java.util.Arrays;
import java.util.List;
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

    HealthInsurance healthInsurance =
        HealthInsurance.values()[patientRegisterForm.getHealthInsuranceCode()];

    // TODO: check for exceptions
    final Patient patient =
        patientService.createPatient(
            patientRegisterForm.getEmail(),
            patientRegisterForm.getPassword(),
            patientRegisterForm.getName(),
            patientRegisterForm.getLastname(),
            healthInsurance);

    LOGGER.info("Registered {}", patient);
    authUser(patient.getEmail(), patientRegisterForm.getPassword());

    final ModelAndView mav = new ModelAndView("auth/patientRegister");
    mav.addObject("form", patientRegisterForm);
    mav.addObject("healthInsurances", Arrays.asList(HealthInsurance.values()));
    mav.addObject("showModal", true);
    return mav;
  }

  // register user?
  @RequestMapping(value = "/patient-register", method = RequestMethod.GET)
  public ModelAndView patientRegister(
      @ModelAttribute("patientRegisterForm") final PatientRegisterForm patientRegisterForm) {
    final ModelAndView mav = new ModelAndView("auth/patientRegister");
    mav.addObject("form", patientRegisterForm);
    mav.addObject("healthInsurances", Arrays.asList(HealthInsurance.values()));
    mav.addObject("showModal", false);

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

    Specialty specialty = Specialty.values()[doctorRegisterForm.getSpecialtyCode()];
    City city = City.values()[doctorRegisterForm.getCityCode()];

    List<HealthInsurance> healthInsurances =
        doctorRegisterForm
            .getHealthInsuranceCodes()
            .stream()
            .map(code -> HealthInsurance.values()[code])
            .collect(Collectors.toList());

    // TODO: check for exceptions
    final Doctor doctor =
        doctorService.createDoctor(
            doctorRegisterForm.getEmail(),
            doctorRegisterForm.getPassword(),
            doctorRegisterForm.getName(),
            doctorRegisterForm.getLastname(),
            specialty,
            city,
            doctorRegisterForm.getAddress(),
            healthInsurances,
            AttendingHours.DEFAULT_ATTENDING_HOURS);

    LOGGER.info("Registered {}", doctor);
    authUser(doctor.getEmail(), doctorRegisterForm.getPassword());

    final ModelAndView mav = new ModelAndView("auth/doctorRegister");
    mav.addObject("form", doctorRegisterForm);
    mav.addObject("showModal", true);
    mav.addObject("cities", Arrays.asList(City.values()));
    mav.addObject("specialties", Arrays.asList(Specialty.values()));
    mav.addObject("healthInsurances", Arrays.asList(HealthInsurance.values()));

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
    mav.addObject("showModal", false);
    return mav;
  }

  private void authUser(String username, String password) {
    UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(username, password);
    Authentication authentication = authenticationManager.authenticate(authToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
