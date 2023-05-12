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
import java.time.DayOfWeek;
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
  private final ImageService imageService;

  @Autowired
  public ProfileController(
      final DoctorService doctorService,
      final PatientService patientService,
      final UserService userService,
      final ImageService imageService) {
    this.doctorService = doctorService;
    this.patientService = patientService;
    this.userService = userService;
    this.imageService = imageService;
  }

  @RequestMapping(value = "/doctor-edit", method = RequestMethod.POST)
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

    AttendingHours attendingHours =
        new AttendingHours(
            ThirtyMinuteBlock.fromBits(doctorEditForm.getMondayAttendingHours()),
            ThirtyMinuteBlock.fromBits(doctorEditForm.getTuesdayAttendingHours()),
            ThirtyMinuteBlock.fromBits(doctorEditForm.getWednesdayAttendingHours()),
            ThirtyMinuteBlock.fromBits(doctorEditForm.getThursdayAttendingHours()),
            ThirtyMinuteBlock.fromBits(doctorEditForm.getFridayAttendingHours()),
            ThirtyMinuteBlock.fromBits(doctorEditForm.getSaturdayAttendingHours()),
            ThirtyMinuteBlock.fromBits(doctorEditForm.getSundayAttendingHours()));

    doctorService.updateAttendingHours(PawAuthUserDetails.getCurrentUserId(), attendingHours);

    ModelAndView mav = new ModelAndView("components/operationSuccessful");
    mav.addObject("showHeader", true);
    mav.addObject("operationTitle", "profile.editProfileSuccessfulTitle");
    mav.addObject("operationMsg", "profile.editProfileSuccessfulMsg");
    return mav;
  }

  @RequestMapping(value = "/doctor-edit", method = RequestMethod.GET)
  public ModelAndView doctorEdit(
      @ModelAttribute("doctorEditForm") final DoctorEditForm doctorEditForm) {
    Doctor doctor =
        doctorService
            .getDoctorById(PawAuthUserDetails.getCurrentUserId())
            .orElseThrow(UserNotFoundException::new);

    //    Image image =
    //        imageService
    //            .getImage(doctor.getProfilePictureId())
    //            .orElseThrow(ImageNotFoundException::new);
    //  doctorEditForm.setImage(image);
    doctorEditForm.setName(doctor.getFirstName());
    doctorEditForm.setLastname(doctor.getLastName());
    doctorEditForm.setEmail(doctor.getEmail());
    doctorEditForm.setHealthInsuranceCode(doctor.getHealthInsurance().ordinal());
    doctorEditForm.setAddress(doctor.getLocation().getAddress());
    doctorEditForm.setCityCode(doctor.getLocation().getCity().ordinal());
    doctorEditForm.setSpecialtyCode(doctor.getSpecialty().ordinal());

    // Attending hours
    AttendingHours attendingHours = doctor.getAttendingHours();
    doctorEditForm.setMondayAttendingHours(
        ThirtyMinuteBlock.toBits(attendingHours.getAttendingBlocksForDay(DayOfWeek.MONDAY)));
    doctorEditForm.setTuesdayAttendingHours(
        ThirtyMinuteBlock.toBits(attendingHours.getAttendingBlocksForDay(DayOfWeek.TUESDAY)));
    doctorEditForm.setWednesdayAttendingHours(
        ThirtyMinuteBlock.toBits(attendingHours.getAttendingBlocksForDay(DayOfWeek.WEDNESDAY)));
    doctorEditForm.setThursdayAttendingHours(
        ThirtyMinuteBlock.toBits(attendingHours.getAttendingBlocksForDay(DayOfWeek.THURSDAY)));
    doctorEditForm.setFridayAttendingHours(
        ThirtyMinuteBlock.toBits(attendingHours.getAttendingBlocksForDay(DayOfWeek.FRIDAY)));
    doctorEditForm.setSaturdayAttendingHours(
        ThirtyMinuteBlock.toBits(attendingHours.getAttendingBlocksForDay(DayOfWeek.SATURDAY)));
    doctorEditForm.setSundayAttendingHours(
        ThirtyMinuteBlock.toBits(attendingHours.getAttendingBlocksForDay(DayOfWeek.SUNDAY)));

    final ModelAndView mav = new ModelAndView("user/doctorEdit");
    mav.addObject("form", doctorEditForm);
    mav.addObject("cities", Arrays.asList(City.values()));
    mav.addObject("specialties", Arrays.asList(Specialty.values()));
    mav.addObject("healthInsurances", Arrays.asList(HealthInsurance.values()));
    mav.addObject("timeEnumValues", ThirtyMinuteBlock.values());
    return mav;
  }

  @RequestMapping(value = "/patient-edit", method = RequestMethod.POST)
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

    ModelAndView mav = new ModelAndView("components/operationSuccessful");
    mav.addObject("showHeader", true);
    mav.addObject("operationTitle", "profile.editProfileSuccessfulTitle");
    mav.addObject("operationMsg", "profile.editProfileSuccessfulMsg");
    return mav;
  }

  @RequestMapping(value = "/patient-edit", method = RequestMethod.GET)
  public ModelAndView patientEdit(
      @ModelAttribute("patientEditForm") final PatientEditForm patientEditForm) {

    Patient patient =
        patientService
            .getPatientById(PawAuthUserDetails.getCurrentUserId())
            .orElseThrow(UserNotFoundException::new);

    // Image image =
    //  imageService
    //    .getImage(patient.getProfilePictureId())
    //  .orElseThrow(ImageNotFoundException::new);

    patientEditForm.setEmail(patient.getEmail());
    patientEditForm.setName(patient.getFirstName());
    patientEditForm.setLastname(patient.getLastName());
    patientEditForm.setHealthInsuranceCode(patient.getHealthInsurance().ordinal());
    //  patientEditForm.setImage(image);

    final ModelAndView mav = new ModelAndView("user/patientEdit");
    mav.addObject("form", patientEditForm);
    mav.addObject("healthInsurances", Arrays.asList(HealthInsurance.values()));
    return mav;
  }

  @RequestMapping(value = "/change-password", method = RequestMethod.POST)
  public ModelAndView changePasswordSubmit(
      @Valid @ModelAttribute("changePasswordForm") final ChangePasswordForm changePasswordForm,
      final BindingResult errors) {
    if (errors.hasErrors()) {
      return changePassword(changePasswordForm, false);
    }

    try {
      boolean changedPassword =
          userService.changePassword(
              PawAuthUserDetails.getCurrentUserId(),
              changePasswordForm.getOldPassword(),
              changePasswordForm.getPassword());
      if (!changedPassword) {
        return changePassword(changePasswordForm, true);
      }
    } catch (IllegalStateException exception) {
      // No deberia pasar
      // TODO: log?
      throw exception;
    }
    ModelAndView mav = new ModelAndView("components/operationSuccessful");
    mav.addObject("showHeader", true);
    mav.addObject("operationTitle", "profile.changePasswordSuccessfulTitle");
    mav.addObject("operationMsg", "profile.changePasswordSuccessfulMsg");
    return mav;
  }

  @RequestMapping(value = "/change-password", method = RequestMethod.GET)
  public ModelAndView changePassword(
      @ModelAttribute("changePasswordForm") final ChangePasswordForm changePasswordForm,
      Boolean OldPasswordDoesNotMatch) {
    final ModelAndView mav = new ModelAndView("user/changePassword");
    mav.addObject("oldPasswordDoesNotMatch", OldPasswordDoesNotMatch);
    mav.addObject("form", changePasswordForm);
    return mav;
  }
}
