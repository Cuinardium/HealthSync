package ar.edu.itba.paw.webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.models.Doctor;

@Controller
public class HomeController {

  private final DoctorService doctorService;

  @Autowired
  public HomeController(
      final DoctorService doctorService) {
    this.doctorService = doctorService;
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ModelAndView landingPage() {
    return new ModelAndView("home/home");
  }

  @RequestMapping(value = "/doctorDashboard", method = RequestMethod.GET)
  public ModelAndView doctorDashboard(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "city", required = false) String city,
      @RequestParam(value = "specialty", required = false) String specialty,
      @RequestParam(value = "healthcare", required = false) String healthcare) {
    final ModelAndView mav = new ModelAndView("home/doctorDashboard");

    // If the parameter is empty, set it to null
    name = name == null || name.isEmpty() ? null : name;
    specialty = specialty == null || specialty.isEmpty() ? null : specialty;
    city = city == null || city.isEmpty() ? null : city;
    healthcare = healthcare == null || healthcare.isEmpty() ? null : healthcare;

    List<Doctor> doctors = doctorService.getFilteredDoctors(name, specialty, city, healthcare);

    mav.addObject("doctors", doctors);

    return mav;
  }
}