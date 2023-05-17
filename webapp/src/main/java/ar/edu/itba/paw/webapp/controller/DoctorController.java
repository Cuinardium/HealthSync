package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.ReviewService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Review;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DoctorController {

  private static final Logger LOGGER = LoggerFactory.getLogger(DoctorController.class);

  private final DoctorService doctorService;

  private final AppointmentService appointmentService;

  private final ReviewService reviewService;

  private static final int PAGE_SIZE = 10;

  @Autowired
  public DoctorController(
      final DoctorService doctorService,
      final AppointmentService appointmentService,
      final ReviewService reviewService) {
    this.doctorService = doctorService;
    this.appointmentService = appointmentService;
    this.reviewService = reviewService;
  }

  @RequestMapping(value = "/{id:\\d+}/detailed-doctor", method = RequestMethod.GET)
  public ModelAndView detailedDoctor(
      @PathVariable("id") final long doctorId,
      @RequestParam(value = "page", required = false, defaultValue = "1") final int page,
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

    // Get reviews
    Page<Review> reviews = reviewService.getReviewsForDoctor(doctorId, page - 1, PAGE_SIZE);

    System.out.println(reviews.getContent());
    System.out.println(reviews.getTotalPages());

    mav.addObject("form", appointmentForm);
    mav.addObject("canBook", canBook);
    mav.addObject("hoursAvailable", hoursAvailable);
    mav.addObject("reviews", reviews.getContent());
    mav.addObject("currentPage", reviews.getCurrentPage() + 1);
    mav.addObject("totalPages", reviews.getContent().size() == 0 ? 1 : reviews.getTotalPages());
    mav.addObject("showModal", false);

    LOGGER.debug("Detailed doctor page for doctor: {} requested", doctorId);
    return mav;
  }

  @RequestMapping(value = "/{id:\\d+}/detailed-doctor", method = RequestMethod.POST)
  public ModelAndView sendAppointment(
      @PathVariable("id") final long doctorId,
      @RequestParam(value = "page", required = false, defaultValue = "1") final int page,
      @Valid @ModelAttribute("appointmentForm") final AppointmentForm appointmentForm,
      final BindingResult errors) {

    if (errors.hasErrors()) {
      return detailedDoctor(doctorId, page, appointmentForm);
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

    final ModelAndView mav = new ModelAndView("doctor/detailedDoctor");

    Doctor doctor = doctorService.getDoctorById(doctorId).orElseThrow(UserNotFoundException::new);

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
    mav.addObject("showModal", true);

    mav.addObject("form", appointmentForm);
    mav.addObject("canBook", canBook);
    mav.addObject("hoursAvailable", hoursAvailable);
    return mav;
  }

  // ========================== Review ==========================
  @RequestMapping(value = "/{id:\\d+}/review", method = RequestMethod.GET)
  public ModelAndView review(
      @PathVariable("id") final long doctorId,
      @ModelAttribute("reviewForm") final ReviewForm reviewForm) {

    if (!reviewService.canReview(doctorId, PawAuthUserDetails.getCurrentUserId())) {
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

    if (!reviewService.canReview(doctorId, PawAuthUserDetails.getCurrentUserId())) {
      throw new ReviewForbiddenException();
    }

    Review review =
        reviewService.createReview(
            doctorId,
            PawAuthUserDetails.getCurrentUserId(),
            reviewForm.getRating(),
            reviewForm.getDescription());

    LOGGER.info("Created {}", review);

    final ModelAndView mav = new ModelAndView("doctor/review");

    mav.addObject("showModal", true);
    mav.addObject("doctorId", doctorId);
    mav.addObject("selcetdRating", reviewForm.getRating());

    return mav;
  }
}
