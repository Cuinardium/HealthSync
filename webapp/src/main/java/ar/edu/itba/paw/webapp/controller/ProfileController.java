package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.interfaces.services.exceptions.PatientNotFoundException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.ChangePasswordForm;
import ar.edu.itba.paw.webapp.form.DoctorEditForm;
import ar.edu.itba.paw.webapp.form.PatientEditForm;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);

  @Autowired
  public ProfileController(
      final DoctorService doctorService,
      final PatientService patientService,
      final UserService userService) {
    this.doctorService = doctorService;
    this.patientService = patientService;
    this.userService = userService;
  }

  @RequestMapping(value = "/patient-profile", method = RequestMethod.GET)
  public ModelAndView patientProfile() {
    Patient patient =
        patientService
            .getPatientById(PawAuthUserDetails.getCurrentUserId())
            .orElseThrow(UserNotFoundException::new);

    final ModelAndView mav = new ModelAndView("user/patientProfile");
    mav.addObject("patient", patient);

    LOGGER.debug("Patient profile page requested");
    return mav;
  }

  @RequestMapping(value = "/doctor-profile", method = RequestMethod.GET)
  public ModelAndView doctorProfile() {
    Doctor doctor =
        doctorService
            .getDoctorById(PawAuthUserDetails.getCurrentUserId())
            .orElseThrow(UserNotFoundException::new);

    final ModelAndView mav = new ModelAndView("user/doctorProfile");

    mav.addObject("doctor", doctor);
    mav.addObject("days", DayOfWeek.values());
    mav.addObject("thirtyMinuteBlocks", ThirtyMinuteBlock.values());

    LOGGER.debug("Doctor profile page requested");
    return mav;
  }

  @RequestMapping(value = "/doctor-edit", method = RequestMethod.POST)
  public ModelAndView doctorEditSubmit(
      @Valid @ModelAttribute("doctorEditForm") final DoctorEditForm doctorEditForm,
      final BindingResult errors) {
    if (errors.hasErrors()) {
      LOGGER.warn("Failed to edit doctor due to form errors");
      return doctorEdit(doctorEditForm);
    }

    Specialty specialty = Specialty.values()[doctorEditForm.getSpecialtyCode()];
    City city = City.values()[doctorEditForm.getCityCode()];

    Set<HealthInsurance> healthInsurances =
        doctorEditForm
            .getHealthInsuranceCodes()
            .stream()
            .map(code -> HealthInsurance.values()[code])
            .collect(Collectors.toSet());

    ThirtyMinuteBlock[] values = ThirtyMinuteBlock.values();

    // TODO: make doctorEditForm return all attending hours
    Set<AttendingHours> attendingHours = new HashSet<>();

    for (Entry<DayOfWeek, List<Integer>> aux : doctorEditForm.getAttendingHours().entrySet()) {
      for (Integer ordinal : aux.getValue()) {
        attendingHours.add(
            new AttendingHours(
                PawAuthUserDetails.getCurrentUserId(), aux.getKey(), values[ordinal]));
      }
    }

    // Q: static fromMultiPartFile method
    try {
      Image image = null;
      if (!doctorEditForm.getImage().isEmpty()) {
        image = new Image(doctorEditForm.getImage().getBytes());
      }
      Doctor doctor =
          doctorService.updateDoctor(
              PawAuthUserDetails.getCurrentUserId(),
              doctorEditForm.getEmail(),
              doctorEditForm.getName(),
              doctorEditForm.getLastname(),
              specialty,
              city,
              doctorEditForm.getAddress(),
              healthInsurances,
              attendingHours,
              image,
              doctorEditForm.getLocale());
      LOGGER.info("Updated {}", doctor);
    } catch (IOException e) {
      // TODO: handle
    } catch (DoctorNotFoundException | EmailInUseException e) {
      LOGGER.error("Failed to update doctor because doctor does not exist");
      throw new UserNotFoundException();
    }

    final ModelAndView mav = new ModelAndView("user/doctorEdit");
    mav.addObject("showModal", true);
    mav.addObject("form", doctorEditForm);
    mav.addObject("cities", Arrays.asList(City.values()));
    mav.addObject("specialties", Arrays.asList(Specialty.values()));
    mav.addObject("currentHealthInsuranceCodes", doctorEditForm.getHealthInsuranceCodes());
    mav.addObject("healthInsurances", Arrays.asList(HealthInsurance.values()));
    mav.addObject("timeEnumValues", ThirtyMinuteBlock.values());
    return mav;
  }

  @RequestMapping(value = "/doctor-edit", method = RequestMethod.GET)
  public ModelAndView doctorEdit(
      @ModelAttribute("doctorEditForm") final DoctorEditForm doctorEditForm) {
    Doctor doctor =
        doctorService
            .getDoctorById(PawAuthUserDetails.getCurrentUserId())
            .orElseThrow(UserNotFoundException::new);

    doctorEditForm.setName(doctor.getFirstName());
    doctorEditForm.setLastname(doctor.getLastName());
    doctorEditForm.setEmail(doctor.getEmail());
    doctorEditForm.setHealthInsuranceCodes(
        doctor
            .getHealthInsurances()
            .stream()
            .map(HealthInsurance::ordinal)
            .collect(Collectors.toList()));
    doctorEditForm.setAddress(doctor.getAddress());
    doctorEditForm.setCityCode(doctor.getCity().ordinal());
    doctorEditForm.setSpecialtyCode(doctor.getSpecialty().ordinal());

    // Attending hours
    Set<AttendingHours> attendingHours = doctor.getAttendingHours();
    doctorEditForm.setAttendingHours(attendingHours);

    final ModelAndView mav = new ModelAndView("user/doctorEdit");
    mav.addObject("form", doctorEditForm);
    mav.addObject("cities", Arrays.asList(City.values()));
    mav.addObject("specialties", Arrays.asList(Specialty.values()));
    mav.addObject("currentHealthInsuranceCodes", doctorEditForm.getHealthInsuranceCodes());
    mav.addObject("healthInsurances", Arrays.asList(HealthInsurance.values()));
    mav.addObject("timeEnumValues", ThirtyMinuteBlock.values());
    mav.addObject("showModal", false);
    LOGGER.debug("Doctor edit page requested");
    return mav;
  }

  @RequestMapping(value = "/patient-edit", method = RequestMethod.POST)
  public ModelAndView patientEditSubmit(
      @Valid @ModelAttribute("patientEditForm") final PatientEditForm patientEditForm,
      final BindingResult errors) {
    if (errors.hasErrors()) {
      LOGGER.warn("Failed to edit patient due to form errors");
      return patientEdit(patientEditForm, false);
    }

    // Q: static fromMultiPartFile method
    try {
      Image image = null;
      if (!patientEditForm.getImage().isEmpty()) {
        image = new Image(patientEditForm.getImage().getBytes());
      }

      HealthInsurance healthInsurance =
          HealthInsurance.values()[patientEditForm.getHealthInsuranceCode()];

      Locale locale = patientEditForm.getLocale();

      Patient patient =
          patientService.updatePatient(
              PawAuthUserDetails.getCurrentUserId(),
              patientEditForm.getEmail(),
              patientEditForm.getName(),
              patientEditForm.getLastname(),
              healthInsurance,
              image,
              locale);
      LOGGER.info("Updated {}", patient);
    } catch (IOException e) {
      // TODO: handle this
    } catch (PatientNotFoundException e) {
      LOGGER.error("Failed to update patient because patient does not exist");
      throw new UserNotFoundException();
    } catch (EmailInUseException e) {
      return patientEdit(patientEditForm, true);
    }

    final ModelAndView mav = new ModelAndView("user/patientEdit");
    mav.addObject("showModal", true);
    mav.addObject("form", patientEditForm);
    mav.addObject("healthInsurances", Arrays.asList(HealthInsurance.values()));
    return mav;
  }

  @RequestMapping(value = "/patient-edit", method = RequestMethod.GET)
  public ModelAndView patientEdit(
      @ModelAttribute("patientEditForm") final PatientEditForm patientEditForm,
      Boolean emailAlreadyInUse) {
    Patient patient =
        patientService
            .getPatientById(PawAuthUserDetails.getCurrentUserId())
            .orElseThrow(UserNotFoundException::new);

    patientEditForm.setEmail(patient.getEmail());
    patientEditForm.setName(patient.getFirstName());
    patientEditForm.setLastname(patient.getLastName());
    patientEditForm.setHealthInsuranceCode(patient.getHealthInsurance().ordinal());

    final ModelAndView mav = new ModelAndView("user/patientEdit");
    mav.addObject("emailAlreadyInUse", emailAlreadyInUse);
    mav.addObject("form", patientEditForm);
    mav.addObject("showModal", false);
    mav.addObject("healthInsurances", Arrays.asList(HealthInsurance.values()));
    LOGGER.debug("Patient edit page requested");
    return mav;
  }

  @RequestMapping(value = "/change-password", method = RequestMethod.POST)
  public ModelAndView changePasswordSubmit(
      @Valid @ModelAttribute("changePasswordForm") final ChangePasswordForm changePasswordForm,
      final BindingResult errors,
      boolean OldPasswordDoesNotMatch) {
    if (errors.hasErrors()) {
      LOGGER.warn("Change password failed due to form errors");
      return changePassword(changePasswordForm, false);
    }

    try {
      boolean changedPassword =
          userService.updatePassword(
              PawAuthUserDetails.getCurrentUserId(),
              changePasswordForm.getOldPassword(),
              changePasswordForm.getPassword());
      if (!changedPassword) {
        LOGGER.warn("Change password failed due to old password not matching");
        return changePassword(changePasswordForm, true);
      }
      LOGGER.info("Updated password");
    } catch (ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException e) {
      LOGGER.error("Change password failed due to user not existing found");
      // TODO: q hago en este caso?
      throw new UserNotFoundException();
    }
    final ModelAndView mav = new ModelAndView("user/changePassword");
    mav.addObject("showModal", true);
    mav.addObject("oldPasswordDoesNotMatch", OldPasswordDoesNotMatch);
    mav.addObject("form", changePasswordForm);
    return mav;
  }

  @RequestMapping(value = "/change-password", method = RequestMethod.GET)
  public ModelAndView changePassword(
      @ModelAttribute("changePasswordForm") final ChangePasswordForm changePasswordForm,
      Boolean OldPasswordDoesNotMatch) {
    final ModelAndView mav = new ModelAndView("user/changePassword");
    mav.addObject("oldPasswordDoesNotMatch", OldPasswordDoesNotMatch);
    mav.addObject("form", changePasswordForm);
    mav.addObject("showModal", false);
    LOGGER.debug("Change password page requested");
    return mav;
  }
}
