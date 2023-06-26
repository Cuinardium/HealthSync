package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.interfaces.services.TokenService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.interfaces.services.exceptions.TokenInvalidException;
import ar.edu.itba.paw.interfaces.services.exceptions.TokenNotFoundException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.DoctorRegisterForm;
import ar.edu.itba.paw.webapp.form.LoginForm;
import ar.edu.itba.paw.webapp.form.PatientRegisterForm;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
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

  private final UserService userService;

  private final TokenService tokenService;

  private final AuthenticationManager authenticationManager;

  @Autowired
  public AuthController(
      final DoctorService doctorService,
      final PatientService patientService,
      final UserService userService,
      final TokenService tokenService,
      AuthenticationManager authenticationManager) {
    this.doctorService = doctorService;
    this.patientService = patientService;
    this.userService = userService;
    this.tokenService = tokenService;
    this.authenticationManager = authenticationManager;
  }

  @RequestMapping(value = "/login")
  public ModelAndView loginForm(
      @ModelAttribute("loginForm") final LoginForm loginForm,
      @RequestParam(value = "error", required = false) String error) {

    ModelAndView mav = new ModelAndView("auth/login");

    // si ?error no esta -> error es null, en cambio si ?error esta -> error es un string vacio
    mav.addObject("hasError", error != null);
    if (error != null) {
      LOGGER.warn("Login failed due to bad credentials");
    } else {
      LOGGER.debug("Login page requested");
    }
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
      LOGGER.warn("Failed to register patient due to form errors");
      return patientRegister(patientRegisterForm, false);
    }

    HealthInsurance healthInsurance =
        HealthInsurance.values()[patientRegisterForm.getHealthInsuranceCode()];

    try {
      final Patient patient =
          patientService.createPatient(
              patientRegisterForm.getEmail(),
              patientRegisterForm.getPassword(),
              patientRegisterForm.getName(),
              patientRegisterForm.getLastname(),
              healthInsurance,
              LocaleContextHolder.getLocale());

      LOGGER.info("Registered {}", patient);

      final ModelAndView mav = new ModelAndView("auth/patientRegister");
      mav.addObject("form", patientRegisterForm);
      mav.addObject("healthInsurances", Arrays.asList(HealthInsurance.values()));
      mav.addObject("showModal", true);
      return mav;
    } catch (EmailInUseException e) {
      LOGGER.warn("Failed to register patient due to email unique constraint");
      return patientRegister(patientRegisterForm, true);
    }
  }

  @RequestMapping(value = "/patient-register", method = RequestMethod.GET)
  public ModelAndView patientRegister(
      @ModelAttribute("patientRegisterForm") final PatientRegisterForm patientRegisterForm,
      Boolean emailAlreadyInUse) {
    final ModelAndView mav = new ModelAndView("auth/patientRegister");
    mav.addObject("emailAlreadyInUse", emailAlreadyInUse);
    mav.addObject("form", patientRegisterForm);
    mav.addObject("healthInsurances", Arrays.asList(HealthInsurance.values()));
    mav.addObject("showModal", false);
    LOGGER.debug("Patient register page requested");
    return mav;
  }

  // TODO: revisar campos
  @RequestMapping(value = "/doctor-register", method = RequestMethod.POST)
  public ModelAndView doctorRegisterSubmit(
      @Valid @ModelAttribute("doctorRegisterForm") final DoctorRegisterForm doctorRegisterForm,
      final BindingResult errors) {
    if (errors.hasErrors()) {
      LOGGER.warn("Failed to register doctor due to form errors");
      return doctorRegisterForm(doctorRegisterForm, false);
    }

    Specialty specialty = Specialty.values()[doctorRegisterForm.getSpecialtyCode()];
    City city = City.values()[doctorRegisterForm.getCityCode()];

    Set<HealthInsurance> healthInsurances =
        doctorRegisterForm.getHealthInsuranceCodes().stream()
            .map(code -> HealthInsurance.values()[code])
            .collect(Collectors.toSet());

    ThirtyMinuteBlock[] values = ThirtyMinuteBlock.values();
    Set<AttendingHours> attendingHours = new HashSet<>();

    for (Entry<DayOfWeek, List<Integer>> aux : doctorRegisterForm.getAttendingHours().entrySet()) {
      for (Integer ordinal : aux.getValue()) {
        attendingHours.add(new AttendingHours(null, aux.getKey(), values[ordinal]));
      }
    }

    try {
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
              attendingHours,
              LocaleContextHolder.getLocale());
      LOGGER.info("Registered {}", doctor);

      final ModelAndView mav = new ModelAndView("auth/doctorRegister");
      mav.addObject("form", doctorRegisterForm);
      mav.addObject("showModal", true);
      mav.addObject("cities", Arrays.asList(City.values()));
      mav.addObject("specialties", Arrays.asList(Specialty.values()));
      mav.addObject("currentHealthInsurances", Collections.emptyList());
      mav.addObject("healthInsurances", Arrays.asList(HealthInsurance.values()));
      mav.addObject("timeEnumValues", ThirtyMinuteBlock.values());

      return mav;
    } catch (EmailInUseException e) {
      LOGGER.warn("Failed to register doctor due to email unique constraint");
      return doctorRegisterForm(doctorRegisterForm, true);
    }
  }

  @RequestMapping(value = "/doctor-register", method = RequestMethod.GET)
  public ModelAndView doctorRegisterForm(
      @ModelAttribute("doctorRegisterForm") final DoctorRegisterForm doctorRegisterForm,
      Boolean emailAlreadyInUse) {
    // Attending hours
    Set<AttendingHours> attendingHours = AttendingHours.DEFAULT_ATTENDING_HOURS;

    doctorRegisterForm.setAttendingHours(attendingHours);

    final ModelAndView mav = new ModelAndView("auth/doctorRegister");
    mav.addObject("emailAlreadyInUse", emailAlreadyInUse);
    mav.addObject("form", doctorRegisterForm);
    mav.addObject("cities", Arrays.asList(City.values()));
    mav.addObject("specialties", Arrays.asList(Specialty.values()));
    mav.addObject("healthInsurances", Arrays.asList(HealthInsurance.values()));
    mav.addObject("currentHealthInsurances", Collections.emptyList());
    mav.addObject("timeEnumValues", ThirtyMinuteBlock.values());
    mav.addObject("showModal", false);
    LOGGER.debug("Doctor register page requested");
    return mav;
  }

  // this mav recieves mail and token as query params
  @RequestMapping(value = "/verify", method = RequestMethod.GET)
  public ModelAndView verify(
      @RequestParam(value = "id", required = true) Long id,
      @RequestParam(value = "token", required = true) String token) {

    boolean successful = false;
    boolean tokenInvalid = false;
    boolean alreadyVerified = false;

    try {
      userService.confirmUser(id, token);
      successful = true;
    } catch (ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException e) {
      LOGGER.warn("User with id {} not found!", id);

      throw new UserNotFoundException();
    } catch (TokenNotFoundException e) {
      LOGGER.warn("Token for userId {} not found!", id);

      alreadyVerified = true;
    } catch (TokenInvalidException e) {
      LOGGER.debug("Token for userId {} is invalid!", id);

      tokenInvalid = true;
    }

    if (successful) {
      return new ModelAndView("redirect:/login");
    }

    final ModelAndView mav = new ModelAndView("auth/verify");
    mav.addObject("tokenInvalid", tokenInvalid);
    mav.addObject("alreadyVerified", alreadyVerified);

    return mav;
  }

  // this mav recieves mail and token as query params
  @RequestMapping(value = "/renew-token", method = RequestMethod.GET)
  public ModelAndView renewToken(@RequestParam(value = "id", required = true) long userId)
      throws UserNotFoundException {
    final ModelAndView mav = new ModelAndView("auth/renewToken");
    Boolean tokenRenewed = true;
    try {
      tokenService.renewUserToken(
          userService.getUserById(userId).orElseThrow(UserNotFoundException::new));
    } catch (TokenNotFoundException e) {
      LOGGER.warn("Token for userId {} not found!", userId);
      tokenRenewed = false;
    }
    mav.addObject("tokenRenewed", tokenRenewed);
    return mav;
  }

  private void authUser(String username, String password) {
    UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(username, password);
    Authentication authentication = authenticationManager.authenticate(authToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
