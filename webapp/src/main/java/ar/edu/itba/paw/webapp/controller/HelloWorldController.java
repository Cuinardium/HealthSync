package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.MailService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloWorldController {

  private final UserService userService;
  private final MailService mailService;

  @Autowired
  public HelloWorldController(final UserService userService, final MailService mailService) {
    this.userService = userService;
    this.mailService = mailService;
  }

  @RequestMapping(value = "/hello", method = RequestMethod.GET)
  public ModelAndView helloWorld() {
    final ModelAndView mav = new ModelAndView("helloworld/hello");
    mav.addObject("user", userService.createUser("pepe@pepe.com", "secreta"));

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
      @RequestParam(value = "email", required = true) final String email,
      @RequestParam(value = "password", required = true) final String password) {
    final User user = userService.createUser(email, password);

    final ModelAndView mav = new ModelAndView("helloworld/hello");
    mav.addObject("user", user);
    return mav;
  }

  @RequestMapping(value = "/register", method = RequestMethod.GET)
  public ModelAndView registerForm() {
    return new ModelAndView("helloworld/register");
  }

  // TODO: revisar campos
  @RequestMapping(value = "/register_medic", method = RequestMethod.POST)
  public ModelAndView registerMedicForm(
      @RequestParam(value = "name", required = true) final String name,
      @RequestParam(value = "lastname", required = true) final String lastname,
      @RequestParam(value = "address", required = true) final String address,
      @RequestParam(value = "city", required = true) final String city,
      @RequestParam(value = "specialization", required = true) final String specialization,
      // TODO: buscar nombre para "obra social" :D
      @RequestParam(value = "obra_social", required = true) final String obra_social,
      @RequestParam(value = "email", required = true) final String email,
      @RequestParam(value = "password", required = true) final String password) {
    final User user = userService.createUser(email, password);

    final ModelAndView mav = new ModelAndView("helloworld/hello");
    mav.addObject("user", user);
    return mav;
  }

  @RequestMapping(value = "/register_medic", method = RequestMethod.GET)
  public ModelAndView registerMedicForm() {
    return new ModelAndView("helloworld/register_medic");
  }

  @RequestMapping(value = "/{id}/appointment", method = RequestMethod.GET)
  public ModelAndView appointmentForm(@PathVariable("id") final int medicId) {
    String email = userService.getEmail(medicId);
    final ModelAndView mav = new ModelAndView("helloworld/appointment");

    mav.addObject("email", email);

    return mav;
  }

  // this function will return void for now until we figure if we make a new view
  // or use a popup
  @RequestMapping(value = "/appointment", method = RequestMethod.POST)
  public void appointmentSubmit(
      @RequestParam(value = "Email", required = true) final String email,
      @RequestParam(value = "First name", required = true) final String name,
      @RequestParam(value = "Last name", required = true) final String lastname,
      @RequestParam(value = "Healthcare system", required = true) final String healthcare,
      @RequestParam(value = "Appointment date", required = true) final String date,
      @RequestParam(value = "Appointment description", required = true) final String desc,
      @RequestParam(value = "Doctor email", required = true) final String docEmail) {

    mailService.sendAppointmentRequestMail(
        email, docEmail, name + " " + lastname, healthcare, date, desc);
  }
}
