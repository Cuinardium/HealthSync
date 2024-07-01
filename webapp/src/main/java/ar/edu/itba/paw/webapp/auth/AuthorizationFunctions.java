package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.ReviewService;
import ar.edu.itba.paw.models.Appointment;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationFunctions {

  @Autowired private ReviewService reviewService;

  @Autowired private AppointmentService appointmentService;

  // ================= Reviews =================

  public boolean canReview(Authentication authentication, long doctorId) {

    if (authentication == null
        || !authentication.isAuthenticated()
        || authentication.getPrincipal() == null
        || doctorId < 0) {
      return false;
    }

    PawAuthUserDetails user = (PawAuthUserDetails) authentication.getPrincipal();

    return reviewService.canReview(doctorId, user.getId());
  }

  // ================= Appointment =================

  public boolean isInvolvedInAppointment(Authentication authentication, long appointmentId) {
    if (authentication == null
        || !authentication.isAuthenticated()
        || authentication.getPrincipal() == null
        || appointmentId < 0) {
      return false;
    }

    PawAuthUserDetails user = (PawAuthUserDetails) authentication.getPrincipal();

    Optional<Appointment> appointment = appointmentService.getAppointmentById(appointmentId);

    return appointment
        .filter(
            value ->
                value.getDoctor().getId() == user.getId()
                    || value.getPatient().getId() == user.getId())
        .isPresent();
  }
}
