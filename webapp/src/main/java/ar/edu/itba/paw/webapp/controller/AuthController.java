package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.form.LoginForm;
import ar.edu.itba.paw.webapp.form.MedicRegisterForm;
import ar.edu.itba.paw.webapp.form.RegisterForm;
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

  private final UserService userService;
  private final DoctorService doctorService;

  private final PatientService patientService;

  @Autowired
  public AuthController(final UserService userService, final DoctorService doctorService, final PatientService patientService) {
    this.userService = userService;
    this.doctorService = doctorService;
    this.patientService= patientService;
  }

  // @RequestMapping(value = "/login", method = RequestMethod.POST)
  // public ModelAndView login(
  //     @Valid @ModelAttribute("loginForm") final LoginForm loginForm, final BindingResult errors) {
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
  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public ModelAndView register(
      @Valid @ModelAttribute("registerForm") final RegisterForm registerForm,
      final BindingResult errors) {
    if (errors.hasErrors()) {
      return registerForm(registerForm);
    }

    final User user = patientService.createPatient(registerForm.getEmail(), registerForm.getPassword(), registerForm.getName(), registerForm.getLastname(), registerForm.getHealthInsuranceCode());
    final ModelAndView mav = new ModelAndView("auth/registerSuccesful");
    mav.addObject("user", user);
    return mav;
  }

  // register user?
  @RequestMapping(value = "/register", method = RequestMethod.GET)
  public ModelAndView registerForm(
      @ModelAttribute("registerForm") final RegisterForm registerForm) {
    final ModelAndView mav = new ModelAndView("auth/register");
    mav.addObject("form", registerForm);
    mav.addObject("healthInsurances", Arrays.asList(HealthInsurance.values()));

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
    user =
        doctorService.createDoctor(
            medicRegisterForm.getEmail(),
            medicRegisterForm.getPassword(),
            medicRegisterForm.getName(),
            medicRegisterForm.getLastname(),
            medicRegisterForm.getHealthInsuranceCode(),
            medicRegisterForm.getSpecialtyCode(),
            medicRegisterForm.getCityCode(),
            medicRegisterForm.getAddress());

    final ModelAndView mav = new ModelAndView("auth/registerSuccesful");
    mav.addObject("user", user);
    return mav;
  }

  @RequestMapping(value = "/register_medic", method = RequestMethod.GET)
  public ModelAndView registerMedicForm(
      @ModelAttribute("medicRegisterForm") final MedicRegisterForm medicRegisterForm) {
    final ModelAndView mav = new ModelAndView("auth/register_medic");
    mav.addObject("form", medicRegisterForm);
    mav.addObject("cities", Arrays.asList(City.values()));
    mav.addObject("specialties", Arrays.asList(Specialty.values()));
    mav.addObject("healthInsurances", Arrays.asList(HealthInsurance.values()));
    return mav;
  }
}
