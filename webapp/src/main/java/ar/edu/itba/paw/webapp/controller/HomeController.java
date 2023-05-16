package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import ar.edu.itba.paw.webapp.auth.UserRoles;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.DoctorFilterForm;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

  private static final int DEFAULT_PAGE_SIZE = 10;

  private final DoctorService doctorService;

  private final PatientService patientService;

  @Autowired
  public HomeController(final DoctorService doctorService, PatientService patientService) {
    this.doctorService = doctorService;
    this.patientService = patientService;
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

  // TODO: REFACTOR THIS
  @RequestMapping(value = "/doctorDashboard", method = RequestMethod.GET)
  public ModelAndView doctorDashboard(
      @ModelAttribute("doctorFilterForm") DoctorFilterForm doctorFilterForm,
      @RequestParam(value = "page", required = false, defaultValue = "1") String page) {

    // Parse page here to catch NumberFormatException
    // If done in parameter, it would be caught by the ExceptionHandler
    int parsedPage;
    try {
      parsedPage = Integer.parseInt(page);
    } catch (NumberFormatException e) {
      parsedPage = 1;
    }
    parsedPage = parsedPage < 1 ? 1 : parsedPage;

    final ModelAndView mav = new ModelAndView("home/doctorDashboard");

    // Get used specialties, cities and health insurances
    Map<Specialty, Integer> usedSpecialties = doctorService.getUsedSpecialties();
    Map<City, Integer> usedCities = doctorService.getUsedCities();
    Map<HealthInsurance, Integer> usedHealthInsurances = doctorService.getUsedHealthInsurances();

    // Get filters
    int specialtyCode = doctorFilterForm.getSpecialtyCode();
    int cityCode = doctorFilterForm.getCityCode();
    int healthInsuranceCode = doctorFilterForm.getHealthInsuranceCode();

    Specialty specialty =
        specialtyCode < 0 || specialtyCode >= Specialty.values().length
            ? null
            : Specialty.values()[specialtyCode];

    City city = cityCode < 0 || cityCode >= City.values().length ? null : City.values()[cityCode];

    HealthInsurance healthInsurance =
        healthInsuranceCode < 0 || healthInsuranceCode >= HealthInsurance.values().length
            ? null
            : HealthInsurance.values()[healthInsuranceCode];

    String name = doctorFilterForm.getName();

    // Get doctors
    Page<Doctor> doctors =
        doctorService.getFilteredDoctors(
            name, specialty, city, healthInsurance, parsedPage - 1, DEFAULT_PAGE_SIZE);

    if (PawAuthUserDetails.getRole().equals(UserRoles.ROLE_PATIENT)) {
      PawAuthUserDetails currentUser =
          (PawAuthUserDetails)
              (SecurityContextHolder.getContext().getAuthentication().getPrincipal());
      HealthInsurance patientHealthInsurance =
          patientService
              .getPatientById(currentUser.getId())
              .orElseThrow(UserNotFoundException::new)
              .getHealthInsurance();

      mav.addObject("patientHealthInsurance", patientHealthInsurance);
    } else {
      mav.addObject("patientHealthInsurance", null);
    }

    mav.addObject("name", name);
    mav.addObject("doctors", doctors.getContent());
    mav.addObject("cityCode", cityCode);
    mav.addObject("cityMap", usedCities);
    mav.addObject("specialtyCode", specialtyCode);
    mav.addObject("specialtyMap", usedSpecialties);
    mav.addObject("healthInsuranceCode", healthInsuranceCode);
    mav.addObject("healthInsuranceMap", usedHealthInsurances);

    // Pagination
    mav.addObject("currentPage", doctors.getCurrentPage() + 1);
    mav.addObject("totalPages", doctors.getContent().size() == 0 ? 1 : doctors.getTotalPages());

    // Only patients can book appointments
    boolean notLogged = PawAuthUserDetails.getRole().equals(UserRoles.ROLE_NULL);
    mav.addObject("notLogged", notLogged);
    boolean canBook = PawAuthUserDetails.getRole().equals(UserRoles.ROLE_PATIENT) || notLogged;
    mav.addObject("canBook", canBook);

    return mav;
  }
}
