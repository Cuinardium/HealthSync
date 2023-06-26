package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import ar.edu.itba.paw.webapp.auth.UserRole;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.DoctorFilterForm;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

  private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

  @Autowired
  public HomeController(final DoctorService doctorService, PatientService patientService) {
    this.doctorService = doctorService;
    this.patientService = patientService;
  }

  @RequestMapping(value = "/")
  public ModelAndView landingPage() {
    ModelAndView mav = new ModelAndView("/home/home");

    List<Specialty> featuredSpecialties = doctorService.getPopularSpecialties();

    mav.addObject("featuredSpecialties", featuredSpecialties);
    LOGGER.debug("Landing page requested");
    return mav;
  }

  // TODO: REFACTOR THIS
  @RequestMapping(value = "/doctor-dashboard", method = RequestMethod.GET)
  public ModelAndView doctorDashboard(
      @ModelAttribute("doctorFilterForm") DoctorFilterForm doctorFilterForm,
      @RequestParam(value = "page", required = false, defaultValue = "1") int page) {

    page = Math.max(page, 1);

    final ModelAndView mav = new ModelAndView("home/doctorDashboard");

    // Get used specialties, cities and health insurances
    Map<Specialty, Integer> usedSpecialties = doctorService.getUsedSpecialties();
    Map<String, Integer> usedCities = doctorService.getUsedCities();
    Map<HealthInsurance, Integer> usedHealthInsurances = doctorService.getUsedHealthInsurances();

    // Get filters
    String name = doctorFilterForm.getName();
    Set<Integer> specialtyCodes = doctorFilterForm.getSpecialtyCodes();
    Set<String> cities = doctorFilterForm.getCities();
    Set<Integer> healthInsuranceCodes = doctorFilterForm.getHealthInsuranceCodes();
    LocalDate date = doctorFilterForm.getDate();
    int fromOrdinal = doctorFilterForm.getFrom();
    int toOrdinal = doctorFilterForm.getTo();
    int minRating = doctorFilterForm.getMinRating();

    ThirtyMinuteBlock fromTime = ThirtyMinuteBlock.values()[fromOrdinal];
    ThirtyMinuteBlock toTime = ThirtyMinuteBlock.values()[toOrdinal];

    Set<Specialty> specialties = new HashSet<>();
    if(specialtyCodes != null) {
      for (Integer specialtyCode : specialtyCodes) {
        if (specialtyCode < 0 || specialtyCode >= Specialty.values().length) {
          continue;
        }
        specialties.add(Specialty.values()[specialtyCode]);
      }
    }

    Set<HealthInsurance> healthInsurances = new HashSet<>();
    if(healthInsuranceCodes != null){
      for (Integer healthInsuranceCode : healthInsuranceCodes) {
        if (healthInsuranceCode < 0 || healthInsuranceCode >= HealthInsurance.values().length) {
          continue;
        }
        healthInsurances.add(HealthInsurance.values()[healthInsuranceCode]);
      }
    }

    // Get doctors
    Page<Doctor> doctors =
        doctorService.getFilteredDoctors(
            name,
            date,
            fromTime,
            toTime,
            specialties,
            cities,
            healthInsurances,
            minRating,
            page - 1,
            DEFAULT_PAGE_SIZE);

    // TODO: hacer esto en spring sec
    if (PawAuthUserDetails.getRole().equals(UserRole.ROLE_PATIENT)) {
      PawAuthUserDetails currentUser = PawAuthUserDetails.getCurrentUser();

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
    mav.addObject("cityMap", usedCities);
    mav.addObject("cities", cities);
    mav.addObject("specialties", specialtyCodes);
    mav.addObject("specialtyMap", usedSpecialties);
    mav.addObject("healthInsurances", healthInsuranceCodes);
    mav.addObject("healthInsuranceMap", usedHealthInsurances);
    mav.addObject("dateFilter", date);
    mav.addObject("fromBlock", fromTime);
    mav.addObject("toBlock", toTime);
    mav.addObject("minRating", minRating);
    mav.addObject("possibleAttendingHours", ThirtyMinuteBlock.values());

    // Pagination
    mav.addObject("currentPage", doctors.getCurrentPage() + 1);
    mav.addObject("totalPages", doctors.getTotalPages());

    // Only patients can book appointments
    boolean notLogged = PawAuthUserDetails.getRole().equals(UserRole.ROLE_NULL);
    mav.addObject("notLogged", notLogged);
    boolean canBook = PawAuthUserDetails.getRole().equals(UserRole.ROLE_PATIENT) || notLogged;
    mav.addObject("canBook", canBook);

    return mav;
  }
}
