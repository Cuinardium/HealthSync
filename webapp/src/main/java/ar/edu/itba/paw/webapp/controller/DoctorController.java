package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import ar.edu.itba.paw.webapp.auth.UserRoles;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DoctorController {
  private final DoctorService doctorService;

  @Autowired
  public DoctorController(final DoctorService doctorService) {
    this.doctorService = doctorService;
  }

  @RequestMapping(value = "/{id:\\d+}/detailed_doctor", method = RequestMethod.GET)
  public ModelAndView detailedDoctor(@PathVariable("id") final int doctorId) {

    Doctor doctor = doctorService.getDoctorById(doctorId).orElseThrow(UserNotFoundException::new);

    final ModelAndView mav = new ModelAndView("doctor/detailedDoctor");

    mav.addObject("doctor", doctor);

    // Only patients can book appointments
    boolean canBook = PawAuthUserDetails.getRole().equals(UserRoles.ROLE_PATIENT);

    mav.addObject("canBook", canBook);
    return mav;
  }
}
