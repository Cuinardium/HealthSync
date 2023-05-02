package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Specialty;
import java.util.ArrayList;
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
  public HomeController(final DoctorService doctorService, final UserService us) {
    this.doctorService = doctorService;
  }

  @RequestMapping(value = "/")
  public ModelAndView landingPage() {
    ModelAndView mav = new ModelAndView("/home/home");

    List<Specialty> featuredSpecialties = new ArrayList<>();
    featuredSpecialties.add(Specialty.CARDIOLOGY);
    featuredSpecialties.add(Specialty.DERMATOLOGY);
    featuredSpecialties.add(Specialty.NEUROLOGY);
    featuredSpecialties.add(Specialty.NUTRITION);
    featuredSpecialties.add(Specialty.OPHTHALMOLOGY);
    featuredSpecialties.add(Specialty.PEDIATRICS);
    featuredSpecialties.add(Specialty.UROLOGY);

    mav.addObject("featuredSpecialties", featuredSpecialties);
    return mav;
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
        doctorService.getFilteredDoctors(name, specialtyCode, cityCode, healthInsuranceCode);

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
