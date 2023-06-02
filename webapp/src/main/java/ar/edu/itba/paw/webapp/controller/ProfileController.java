package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.ChangePasswordForm;
import ar.edu.itba.paw.webapp.form.DoctorEditForm;
import ar.edu.itba.paw.webapp.form.PatientEditForm;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;
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
      final UserService userService,
      final ImageService imageService) {
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

    List<HealthInsurance> healthInsurances =
        doctorEditForm
            .getHealthInsuranceCodes()
            .stream()
            .map(code -> HealthInsurance.values()[code])
            .collect(Collectors.toList());

    ThirtyMinuteBlock[] values = ThirtyMinuteBlock.values();
    AttendingHours attendingHours =
        new AttendingHours(
            doctorEditForm
                .getMondayAttendingHours()
                .stream()
                .map(i -> values[i])
                .collect(Collectors.toList()),
            doctorEditForm
                .getTuesdayAttendingHours()
                .stream()
                .map(i -> values[i])
                .collect(Collectors.toList()),
            doctorEditForm
                .getWednesdayAttendingHours()
                .stream()
                .map(i -> values[i])
                .collect(Collectors.toList()),
            doctorEditForm
                .getThursdayAttendingHours()
                .stream()
                .map(i -> values[i])
                .collect(Collectors.toList()),
            doctorEditForm
                .getFridayAttendingHours()
                .stream()
                .map(i -> values[i])
                .collect(Collectors.toList()),
            doctorEditForm
                .getSaturdayAttendingHours()
                .stream()
                .map(i -> values[i])
                .collect(Collectors.toList()),
            doctorEditForm
                .getSundayAttendingHours()
                .stream()
                .map(i -> values[i])
                .collect(Collectors.toList()));

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
              image);
      LOGGER.info("Updated {}", doctor);
    } catch (IOException e) {
      // TODO: handle
    } catch (ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException e) {
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
    doctorEditForm.setAddress(doctor.getLocation().getAddress());
    doctorEditForm.setCityCode(doctor.getLocation().getCity().ordinal());
    doctorEditForm.setSpecialtyCode(doctor.getSpecialty().ordinal());

    // Attending hours
    AttendingHours attendingHours = doctor.getAttendingHours();
    doctorEditForm.setMondayAttendingHours(
        attendingHours
            .getAttendingBlocksForDay(DayOfWeek.MONDAY)
            .stream()
            .map(ThirtyMinuteBlock::ordinal)
            .collect(Collectors.toList()));
    doctorEditForm.setTuesdayAttendingHours(
        attendingHours
            .getAttendingBlocksForDay(DayOfWeek.TUESDAY)
            .stream()
            .map(ThirtyMinuteBlock::ordinal)
            .collect(Collectors.toList()));
    doctorEditForm.setWednesdayAttendingHours(
        attendingHours
            .getAttendingBlocksForDay(DayOfWeek.WEDNESDAY)
            .stream()
            .map(ThirtyMinuteBlock::ordinal)
            .collect(Collectors.toList()));
    doctorEditForm.setThursdayAttendingHours(
        attendingHours
            .getAttendingBlocksForDay(DayOfWeek.THURSDAY)
            .stream()
            .map(ThirtyMinuteBlock::ordinal)
            .collect(Collectors.toList()));
    doctorEditForm.setFridayAttendingHours(
        attendingHours
            .getAttendingBlocksForDay(DayOfWeek.FRIDAY)
            .stream()
            .map(ThirtyMinuteBlock::ordinal)
            .collect(Collectors.toList()));
    doctorEditForm.setSaturdayAttendingHours(
        attendingHours
            .getAttendingBlocksForDay(DayOfWeek.SATURDAY)
            .stream()
            .map(ThirtyMinuteBlock::ordinal)
            .collect(Collectors.toList()));
    doctorEditForm.setSundayAttendingHours(
        attendingHours
            .getAttendingBlocksForDay(DayOfWeek.SUNDAY)
            .stream()
            .map(ThirtyMinuteBlock::ordinal)
            .collect(Collectors.toList()));

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
      return patientEdit(patientEditForm);
    }

    // Q: static fromMultiPartFile method
    try {
      Image image = null;
      if (!patientEditForm.getImage().isEmpty()) {
        image = new Image(patientEditForm.getImage().getBytes());
      }

      HealthInsurance healthInsurance =
          HealthInsurance.values()[patientEditForm.getHealthInsuranceCode()];

      Patient patient =
          patientService.updatePatient(
              PawAuthUserDetails.getCurrentUserId(),
              patientEditForm.getEmail(),
              patientEditForm.getName(),
              patientEditForm.getLastname(),
              healthInsurance,
              image);
      LOGGER.info("Updated {}", patient);
    } catch (IOException e) {
      // TODO: handle this
    } catch (ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException e) {
      LOGGER.error("Failed to update patient because patient does not exist");
      throw new UserNotFoundException();
    }

    final ModelAndView mav = new ModelAndView("user/patientEdit");
    mav.addObject("showModal", true);
    mav.addObject("form", patientEditForm);
    mav.addObject("healthInsurances", Arrays.asList(HealthInsurance.values()));
    return mav;
  }

  @RequestMapping(value = "/patient-edit", method = RequestMethod.GET)
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
    } catch (IllegalStateException exception) {
      // No deberia pasar
      // TODO: log?
      throw exception;
    } catch (ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException e) {
      LOGGER.error("Change password failed due to user nott existing found");
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
