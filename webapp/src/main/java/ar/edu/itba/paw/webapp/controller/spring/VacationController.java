package ar.edu.itba.paw.webapp.controller.spring;

import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.VacationInvalidException;
import ar.edu.itba.paw.interfaces.services.exceptions.VacationNotFoundException;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.models.Vacation;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.DoctorDeleteVacationForm;
import ar.edu.itba.paw.webapp.form.DoctorVacationForm;
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
public class VacationController {

  private final DoctorService doctorService;
  private final AppointmentService appointmentService;

  private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);

  @Autowired
  public VacationController(DoctorService doctorService, AppointmentService appointmentService) {
    this.doctorService = doctorService;
    this.appointmentService = appointmentService;
  }

  @RequestMapping(value = "/doctor-vacation", method = RequestMethod.GET)
  public ModelAndView getVacations(
      @ModelAttribute("doctorVacationForm") final DoctorVacationForm doctorVacationForm,
      @ModelAttribute("deleteVacationForm") final DoctorDeleteVacationForm deleteVacationForm) {
    return getVacationsModelAndView(false, false, false, false);
  }

  @RequestMapping(value = "/doctor-vacation", method = RequestMethod.POST)
  public ModelAndView doctorAddVacations(
      @Valid @ModelAttribute("doctorVacationForm") final DoctorVacationForm doctorVacationForm,
      @ModelAttribute("deleteVacationForm") final DoctorDeleteVacationForm deleteVacationForm,
      final BindingResult errors) {

    long userId = PawAuthUserDetails.getCurrentUserId();

    if (errors.hasErrors()) {
      LOGGER.warn("Failed to add vacation due to form errors");
      return getVacationsModelAndView(true, false, false, false);
    }

    Vacation newVacation =
        new Vacation(
            userId,
            doctorVacationForm.getFromDate(),
            doctorVacationForm.getFromTimeEnum(),
            doctorVacationForm.getToDate(),
            doctorVacationForm.getToTimeEnum());

    try {
      doctorService.addVacation(userId, newVacation);

      if (doctorVacationForm.getCancelAppointmentsInVacation()) {
        appointmentService.cancelAppointmentsInRange(
            userId,
            newVacation.getFromDate(),
            newVacation.getFromTime(),
            newVacation.getToDate(),
            newVacation.getToTime(),
            doctorVacationForm.getCancelReason());
      }

    } catch (DoctorNotFoundException e) {
      LOGGER.warn("Failed to add vacation due to doctor not found");
      throw new UserNotFoundException();
    } catch (VacationInvalidException e) {
      LOGGER.warn("Failed to add vacation due to invalid vacation");

      return getVacationsModelAndView(true, true, false, false);
    }

    LOGGER.debug("Doctor added vacation successfully");

    return getVacationsModelAndView(false, false, true, false);
  }

  @RequestMapping(value = "/delete-vacation", method = RequestMethod.POST)
  public ModelAndView doctorDeleteVacations(
      @ModelAttribute("deleteVacationForm") final DoctorDeleteVacationForm deleteVacationForm,
      @ModelAttribute("doctorVacationForm") final DoctorVacationForm doctorVacationForm) {

    long userId = PawAuthUserDetails.getCurrentUserId();

    try {
      doctorService.removeVacation(
          userId,
          new Vacation(
              userId,
              deleteVacationForm.getFromDate(),
              deleteVacationForm.getFromTimeEnum(),
              deleteVacationForm.getToDate(),
              deleteVacationForm.getToTimeEnum()));
    } catch (DoctorNotFoundException e) {
      LOGGER.warn("Failed to delete vacation due to doctor not found");
      throw new UserNotFoundException();
    } catch (VacationNotFoundException e) {
      LOGGER.warn("Failed to delete vacation due to vacation not found");
      throw new UserNotFoundException();
    }

    LOGGER.debug("Doctor deleted vacation successfully");

    return getVacationsModelAndView(false, false, false, true);
  }

  // ============== Private methods ==============

  private ModelAndView getVacationsModelAndView(
      boolean showAddVacationModal,
      boolean isVacationInvalid,
      boolean showVacationSuccessModal,
      boolean showVacationDeleteSuccessModal) {
    ModelAndView mav = new ModelAndView("vacation/doctorVacation");

    long userId = PawAuthUserDetails.getCurrentUserId();

    Doctor doctor = doctorService.getDoctorById(userId).orElseThrow(UserNotFoundException::new);

    List<Vacation> orderedVacations =
        doctor.getVacations().stream().sorted().collect(Collectors.toList());

    mav.addObject("vacations", orderedVacations);
    mav.addObject("timeEnumValues", ThirtyMinuteBlock.values());
    mav.addObject("isVacationInvalid", isVacationInvalid);
    mav.addObject("showAddVacationModal", showAddVacationModal);
    mav.addObject("showVacationSuccessModal", showVacationSuccessModal);
    mav.addObject("showVacationDeleteSuccessModal", showVacationDeleteSuccessModal);

    return mav;
  }
}