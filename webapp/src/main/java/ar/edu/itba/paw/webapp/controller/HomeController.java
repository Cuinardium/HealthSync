package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.LocationService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import ar.edu.itba.paw.webapp.auth.UserRoles;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

  private final DoctorService doctorService;

  private final PatientService patientService;
  private final LocationService locationService;

  @Autowired
  public HomeController(final DoctorService doctorService, PatientService patientService, final LocationService locationService) {
    this.doctorService = doctorService;
    this.patientService = patientService;
    this.locationService = locationService;
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

    // Get used specialties, cities and health insurances
    Map<Specialty, Integer> usedSpecialties = doctorService.getUsedSpecialties();
    Map<City, Integer> usedCities = locationService.getUsedCities();
    Map<HealthInsurance, Integer> usedHealthInsurances = doctorService.getUsedHealthInsurances();

    // Get doctors
    List<Doctor> doctors =
        doctorService.getFilteredDoctors(name, specialtyCode, cityCode, healthInsuranceCode);


    if(PawAuthUserDetails.getRole().equals(UserRoles.ROLE_PATIENT)){
      PawAuthUserDetails currentUser = (PawAuthUserDetails) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());
      HealthInsurance patientHealthInsurance= patientService.getPatientById(currentUser.getId()).orElseThrow(UserNotFoundException::new).getHealthInsurance();

      mav.addObject("patientHealthInsurance", patientHealthInsurance);
    }else {
      mav.addObject("patientHealthInsurance", null);
    }

    mav.addObject("name", name);
    mav.addObject("doctors", doctors);
    mav.addObject("cityCode", cityCode);
    mav.addObject("cityMap", usedCities);
    mav.addObject("specialtyCode", specialtyCode);
    mav.addObject("specialtyMap", usedSpecialties);
    mav.addObject("healthInsuranceCode", healthInsuranceCode);
    mav.addObject("healthInsuranceMap", usedHealthInsurances);

    // Only patients can book appointments
    boolean notLogged=PawAuthUserDetails.getRole().equals(UserRoles.ROLE_NULL);
    mav.addObject("notLogged", notLogged);
    boolean canBook =
        PawAuthUserDetails.getRole().equals(UserRoles.ROLE_PATIENT)
            || notLogged;
    mav.addObject("canBook", canBook);

    return mav;
  }
}
