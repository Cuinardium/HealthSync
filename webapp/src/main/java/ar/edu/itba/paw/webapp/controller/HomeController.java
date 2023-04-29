package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Specialty;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

  private final DoctorService doctorService;

  @Autowired
  public HomeController(final DoctorService doctorService) {
    this.doctorService = doctorService;
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ModelAndView landingPage() {
    return new ModelAndView("home/home");
  }

  @RequestMapping(value = "/doctorDashboard", method = RequestMethod.GET)
  public ModelAndView doctorDashboard(
      @RequestParam(value = "name", required = false, defaultValue = "") String name,
      @RequestParam(value = "cityCode", required = false, defaultValue = "-1") Integer cityCode,
      @RequestParam(value = "specialtyCode", required = false, defaultValue = "-1")
          Integer specialtyCode,
      @RequestParam(value = "healthInsuranceCode", required = false, defaultValue = "-1")
          Integer healthInsuranceCode) {
    final ModelAndView mav = new ModelAndView("home/doctorDashboard");

    List<Doctor> doctors =
        doctorService.getFilteredDoctors(name, cityCode, specialtyCode, healthInsuranceCode);

    mav.addObject("doctors", doctors);
    mav.addObject("cityCode", cityCode);
    mav.addObject("cities", Arrays.asList(City.values()));
    mav.addObject("specialtyCode", specialtyCode);
    mav.addObject("specialties", Arrays.asList(Specialty.values()));
    mav.addObject("healthInsuranceCode", healthInsuranceCode);
    mav.addObject("healthInsurances", Arrays.asList(HealthInsurance.values()));
    return mav;
  }
}
