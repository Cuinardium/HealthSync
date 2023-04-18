package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.MailService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.MedicRegisterForm;
import ar.edu.itba.paw.webapp.form.RegisterForm;
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
  private final MailService mailService;

  @Autowired
  public AuthController(
      final UserService userService,
      final MailService mailService,
      final DoctorService doctorService) {
    this.userService = userService;
    this.mailService = mailService;
    this.doctorService = doctorService;
  }

  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public ModelAndView login() {
    return new ModelAndView("auth/login");
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

    final User user =
        userService.createUser(registerForm.getEmail(), registerForm.getPassword(), "", "");

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

    final ModelAndView mav = new ModelAndView("auth/registerSuccesful");
    mav.addObject("user", user);
    return mav;
  }

  @RequestMapping(value = "/register_medic", method = RequestMethod.GET)
  public ModelAndView registerMedicForm(
      @ModelAttribute("medicRegisterForm") final MedicRegisterForm medicRegisterForm) {
    final ModelAndView mav = new ModelAndView("auth/register_medic");
    mav.addObject("form", medicRegisterForm);
    return mav;
  }
}
