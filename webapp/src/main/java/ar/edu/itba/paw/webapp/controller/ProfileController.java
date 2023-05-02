package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.DoctorEditForm;
import ar.edu.itba.paw.webapp.form.PasswordForm;
import ar.edu.itba.paw.webapp.form.PatientEditForm;
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
public class ProfileController {
  private final DoctorService doctorService;
  private final PatientService patientService;

  private final UserService userService;

  @Autowired
  public ProfileController(
      final DoctorService doctorService,
      final PatientService patientService,
      final UserService userService) {
    this.doctorService = doctorService;
    this.patientService = patientService;
    this.userService = userService;
  }

  @RequestMapping(value = "/edit-doctor", method = RequestMethod.POST)
  public ModelAndView doctorEditSubmit(
      @Valid @ModelAttribute("doctorEditForm") final DoctorEditForm doctorEditForm,
      final BindingResult errors) {
    if (errors.hasErrors()) {
      return doctorEdit(doctorEditForm);
    }

    doctorService.updateInformation(
        PawAuthUserDetails.getCurrentUserId(),
        doctorEditForm.getEmail(),
        doctorEditForm.getName(),
        doctorEditForm.getLastname(),
        doctorEditForm.getHealthInsuranceCode(),
        doctorEditForm.getSpecialtyCode(),
        doctorEditForm.getCityCode(),
        doctorEditForm.getAddress());

    return new ModelAndView("auth/editSuccesful");
  }

  @RequestMapping(value = "/edit-doctor", method = RequestMethod.GET)
  public ModelAndView doctorEdit(
      @ModelAttribute("doctorEditForm") final DoctorEditForm doctorEditForm) {
    Doctor doctor =
        doctorService
            .getDoctorById(PawAuthUserDetails.getCurrentUserId())
            .orElseThrow(UserNotFoundException::new);

    doctorEditForm.setName(doctor.getFirstName());
    doctorEditForm.setLastname(doctor.getLastName());
    doctorEditForm.setEmail(doctor.getEmail());
    doctorEditForm.setHealthInsuranceCode(doctor.getHealthInsurance().ordinal());
    doctorEditForm.setAddress(doctor.getLocation().getAddress());
    doctorEditForm.setCityCode(doctor.getLocation().getCity().ordinal());
    doctorEditForm.setSpecialtyCode(doctor.getSpecialty().ordinal());

    final ModelAndView mav = new ModelAndView("user/doctorEdit");
    mav.addObject("form", doctorEditForm);
    mav.addObject("cities", Arrays.asList(City.values()));
    mav.addObject("specialties", Arrays.asList(Specialty.values()));
    mav.addObject("healthInsurances", Arrays.asList(HealthInsurance.values()));
    return mav;
  }

  @RequestMapping(value = "/edit-patient", method = RequestMethod.POST)
  public ModelAndView patientEditSubmit(
      @Valid @ModelAttribute("patientEditForm") final PatientEditForm patientEditForm,
      final BindingResult errors) {
    if (errors.hasErrors()) {
      return patientEdit(patientEditForm);
    }

    patientService.updateInformation(
        PawAuthUserDetails.getCurrentUserId(),
        patientEditForm.getEmail(),
        patientEditForm.getName(),
        patientEditForm.getLastname(),
        patientEditForm.getHealthInsuranceCode());

    return new ModelAndView("auth/registerSuccesful");
  }

  @RequestMapping(value = "/edit-patient", method = RequestMethod.GET)
  public ModelAndView patientEdit(
      @ModelAttribute("patientEditForm") final PatientEditForm patientEditForm) {

    Patient patient =
        patientService
            .getPatientById(PawAuthUserDetails.getCurrentUserId())
            .orElseThrow(UserNotFoundException::new);

    patientEditForm.setEmail(patient.getEmail());
    patientEditForm.setName(patient.getFirstName());
    patientEditForm.setLastname(patient.getLastName());
    patientEditForm.setHealthInsuranceCode(patient.getHealthInsurance().ordinal());

    final ModelAndView mav = new ModelAndView("user/patientEdit");
    mav.addObject("form", patientEditForm);
    mav.addObject("healthInsurances", Arrays.asList(HealthInsurance.values()));

    return mav;
  }

  @RequestMapping(value = "/change-password", method = RequestMethod.POST)
  public ModelAndView changePasswordSubmit(
      @Valid @ModelAttribute("changePasswordForm") final PasswordForm passwordForm,
      final BindingResult errors) {
    if (errors.hasErrors()) {
      return changePassword(passwordForm);
    }

    userService.changePassword(PawAuthUserDetails.getCurrentUserId(), passwordForm.getPassword());

    return new ModelAndView("auth/registerSuccesful");
  }

  @RequestMapping(value = "/change-password", method = RequestMethod.GET)
  public ModelAndView changePassword(
      @ModelAttribute("changePasswordForm") final PasswordForm passwordForm) {
    final ModelAndView mav = new ModelAndView("user/changePassword");
    mav.addObject("form", passwordForm);
    mav.addObject("healthInsurances", Arrays.asList(HealthInsurance.values()));

    return mav;
  }
}
