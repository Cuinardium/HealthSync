package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.VacationInvalidException;
import ar.edu.itba.paw.interfaces.services.exceptions.VacationNotFoundException;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Vacation;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
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

  @RequestMapping(value = "/doctor-vacations", method = RequestMethod.GET)
  public ModelAndView getVacations() {
    ModelAndView mav = new ModelAndView("doctor-vacations");

    long userId = PawAuthUserDetails.getCurrentUserId();

    Doctor doctor = doctorService.getDoctorById(userId).orElseThrow(UserNotFoundException::new);

    List<Vacation> orderedVacations =
        doctor.getVacations().stream().sorted().collect(Collectors.toList());

    mav.addObject("vacations", orderedVacations);

    return mav;
  }

  @RequestMapping(value = "/doctor-vacation", method = RequestMethod.POST)
  public ModelAndView doctorAddVacations(
      @Valid @ModelAttribute("doctorVacationForm") final DoctorVacationForm doctorVacationForm,
      final BindingResult errors) {

    Doctor doctor =
        doctorService
            .getDoctorById(PawAuthUserDetails.getCurrentUserId())
            .orElseThrow(UserNotFoundException::new);

    if (errors.hasErrors()) {
      LOGGER.warn("Failed to add vacation due to form errors");
      return new ModelAndView("doctor-vacations");
    }

    try {
      doctorService.addVacation(
          doctor.getId(),
          new Vacation(
              doctor.getId(),
              doctorVacationForm.getFromDate(),
              doctorVacationForm.getFromTimeEnum(),
              doctorVacationForm.getToDate(),
              doctorVacationForm.getToTimeEnum()));
    } catch (DoctorNotFoundException e) {
      LOGGER.warn("Failed to add vacation due to doctor not found");
      throw new UserNotFoundException();
    } catch (VacationInvalidException e) {
      LOGGER.warn("Failed to add vacation due to invalid vacation");

      return new ModelAndView("doctor-vacations");
    }

    LOGGER.debug("Doctor added vacation successfully");

    boolean hasAppointmentsInVacation =
        appointmentService.hasAppointmentsInRange(
            doctor.getId(),
            doctorVacationForm.getFromDate(),
            doctorVacationForm.getFromTimeEnum(),
            doctorVacationForm.getToDate(),
            doctorVacationForm.getToTimeEnum());

    if (hasAppointmentsInVacation) {
      // TODO: implement logic
      // this should propt user to cancel appointments
    }

    return new ModelAndView("doctor-vacations");
  }

  @RequestMapping(value = "/delete-vacation", method = RequestMethod.POST)
  public ModelAndView doctorDeleteVacations(
      @Valid @ModelAttribute("doctorVacationForm") final DoctorVacationForm doctorVacationForm,
      final BindingResult errors) {

    long userId = PawAuthUserDetails.getCurrentUserId();

    if (errors.hasErrors()) {
      LOGGER.warn("Failed to delete vacation due to form errors");
      return new ModelAndView("doctor-vacations");
    }

    try {
      doctorService.removeVacation(
          userId,
          new Vacation(
              userId,
              doctorVacationForm.getFromDate(),
              doctorVacationForm.getFromTimeEnum(),
              doctorVacationForm.getToDate(),
              doctorVacationForm.getToTimeEnum()));
    } catch (DoctorNotFoundException e) {
      LOGGER.warn("Failed to delete vacation due to doctor not found");
      throw new UserNotFoundException();
    } catch (VacationNotFoundException e) {
      LOGGER.warn("Failed to delete vacation due to vacation not found");
      throw new UserNotFoundException();
    }

    LOGGER.debug("Doctor deleted vacation successfully");

    return new ModelAndView("doctor-vacations");
  }

  @RequestMapping(value = "/cancel-vacation-appointments", method = RequestMethod.POST)
  public ModelAndView doctorCancelVacationAppointments(
    @Valid @ModelAttribute("cancelVacationAppointmentsForm") final DoctorVacationForm doctorVacationForm,
      final BindingResult errors) {

    long userId = PawAuthUserDetails.getCurrentUserId();

    if (errors.hasErrors()) {
      LOGGER.warn("Failed to cancel vacation appointments due to form errors");
      return new ModelAndView("doctor-vacations");
    }

    try {
      appointmentService.cancelAppointmentsInRange(
          userId,
          doctorVacationForm.getFromDate(),
          doctorVacationForm.getFromTimeEnum(),
          doctorVacationForm.getToDate(),
          doctorVacationForm.getToTimeEnum(),
          doctorVacationForm.getCancelReason());
    } catch (DoctorNotFoundException e) {
      LOGGER.warn("Failed to cancel vacation appointments due to doctor not found");
      throw new UserNotFoundException();
    }

    LOGGER.debug("Doctor canceled vacation appointments successfully");

    return new ModelAndView("doctor-vacations");
  }
}
