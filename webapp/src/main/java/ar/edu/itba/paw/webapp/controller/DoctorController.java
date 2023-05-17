package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.ReviewService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import ar.edu.itba.paw.webapp.auth.UserRoles;
import ar.edu.itba.paw.webapp.exceptions.ReviewForbiddenException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.AppointmentForm;
import ar.edu.itba.paw.webapp.form.ReviewForm;
import java.time.LocalDate;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DoctorController {

  private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentController.class);

  private final DoctorService doctorService;

  private final AppointmentService appointmentService;

  private final ReviewService reviewService;

  @Autowired
  public DoctorController(
      final DoctorService doctorService,
      final AppointmentService appointmentService,
      final ReviewService reviewService) {
    this.doctorService = doctorService;
    this.appointmentService = appointmentService;
    this.reviewService = reviewService;
  }

  @RequestMapping(value = "/{id:\\d+}/detailed_doctor", method = RequestMethod.GET)
  public ModelAndView detailedDoctor(
      @PathVariable("id") final long doctorId,
      @ModelAttribute("appointmentForm") final AppointmentForm appointmentForm) {

    Doctor doctor = doctorService.getDoctorById(doctorId).orElseThrow(UserNotFoundException::new);

    final ModelAndView mav = new ModelAndView("doctor/detailedDoctor");

    mav.addObject("doctor", doctor);

    // Only patients can book appointments
    boolean canBook = PawAuthUserDetails.getRole().equals(UserRoles.ROLE_PATIENT);

    // Booking starts from the day after today because of not allowing to book appointments in the
    // past
    LocalDate tomorrow = LocalDate.now().plusDays(1);

    // Patients will only be able to book appointments between tomorrow and 3 months from now
    List<List<ThirtyMinuteBlock>> hoursAvailable =
        appointmentService.getAvailableHoursForDoctorOnRange(
            doctorId, tomorrow, tomorrow.plusMonths(3));

    mav.addObject("form", appointmentForm);
    mav.addObject("canBook", canBook);
    mav.addObject("hoursAvailable", hoursAvailable);

    System.out.println(
        "====================================================================================================");
    System.out.println(reviewService.getReviewsForDoctor(doctorId));
    System.out.println(
        "====================================================================================================");

    return mav;
  }

  @RequestMapping(value = "/{id:\\d+}/detailed_doctor", method = RequestMethod.POST)
  public ModelAndView sendAppointment(
      @PathVariable("id") final long doctorId,
      @Valid @ModelAttribute("appointmentForm") final AppointmentForm appointmentForm,
      final BindingResult errors) {

    if (errors.hasErrors()) {
      return detailedDoctor(doctorId, appointmentForm);
    }

    PawAuthUserDetails currentUser =
        (PawAuthUserDetails)
            (SecurityContextHolder.getContext().getAuthentication().getPrincipal());

    try {
      Appointment appointment =
          appointmentService.createAppointment(
              currentUser.getId(),
              doctorId,
              appointmentForm.getDate(),
              appointmentForm.getBlockEnum(),
              appointmentForm.getDescription());

      LOGGER.info("Created {}", appointment);
    } catch (IllegalStateException e) {
      // No deberia pasar
      // TODO: log?
      throw e;
    } catch (RuntimeException e) {
      // TODO: CORRECT exception handling
      LOGGER.error(
          "Failed to create Appointment for patient {}, {}",
          currentUser.getId(),
          appointmentForm,
          new RuntimeException());
    }

    return appointmentSent();
  }

  // ========================== Appointment Sent ==========================
  @RequestMapping(value = "/appointment_sent", method = RequestMethod.GET)
  public ModelAndView appointmentSent() {
    return new ModelAndView("appointment/appointmentSent");
  }

  // ========================== Review ==========================
  @RequestMapping(value = "/{id:\\d+}/review", method = RequestMethod.GET)
  public ModelAndView review(
      @PathVariable("id") final long doctorId,
      @ModelAttribute("reviewForm") final ReviewForm reviewForm) {

     doctorService.getDoctorById(doctorId).orElseThrow(UserNotFoundException::new);

    boolean hasPatientMetDoctor =
        appointmentService.hasPatientMetDoctor(PawAuthUserDetails.getCurrentUserId(), doctorId);

    if (!hasPatientMetDoctor) {
      throw new ReviewForbiddenException();
    }

    final ModelAndView mav = new ModelAndView("doctor/review");
    mav.addObject("showModal", false);
    mav.addObject("doctorId", doctorId);
    mav.addObject("selcetdRating", reviewForm.getRating());

    return mav;
  }

  @RequestMapping(value = "/{id:\\d+}/review", method = RequestMethod.POST)
  public ModelAndView submitReview(
      @PathVariable("id") final long doctorId,
      @Valid @ModelAttribute("reviewForm") final ReviewForm reviewForm,
      final BindingResult errors) {

    if (errors.hasErrors()) {
      return review(doctorId, reviewForm);
    }

    doctorService.getDoctorById(doctorId).orElseThrow(UserNotFoundException::new);

    boolean hasPatientMetDoctor =
        appointmentService.hasPatientMetDoctor(PawAuthUserDetails.getCurrentUserId(), doctorId);

    if (!hasPatientMetDoctor) {
      throw new ReviewForbiddenException();
    }

    reviewService.createReview(
        doctorId,
        PawAuthUserDetails.getCurrentUserId(),
        reviewForm.getRating(),
        reviewForm.getDescription());

    final ModelAndView mav = new ModelAndView("doctor/review");

    mav.addObject("showModal", true);
    mav.addObject("doctorId", doctorId);
    mav.addObject("selcetdRating", reviewForm.getRating());

    return mav;
  }
}
