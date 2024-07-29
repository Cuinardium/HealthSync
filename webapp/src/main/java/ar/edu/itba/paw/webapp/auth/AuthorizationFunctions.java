package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.NotificationService;
import ar.edu.itba.paw.interfaces.services.ReviewService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationFunctions {

  @Autowired private AppointmentService appointmentService;

  @Autowired private NotificationService notificationService;

  // ================= User ====================

  public boolean isUser(Authentication authentication, long userId) {
    if (isInvalid(authentication) || userId < 0) {
      return false;
    }

    PawAuthUserDetails user = (PawAuthUserDetails) authentication.getPrincipal();
    if (user == null) {
      return false;
    }

    return user.getId() == userId;
  }

  // ================= Patient =====================

  public boolean hasAppointmentWithPatient(Authentication authentication, long patientId) {
    if (isInvalid(authentication) || patientId < 0) {
      return false;
    }

    UserRole role = PawAuthUserDetails.getRole();

    // If it's a patient
    if (role == UserRole.ROLE_PATIENT) {
      return false;
    }

    PawAuthUserDetails doctor = (PawAuthUserDetails) authentication.getPrincipal();
    if (doctor == null) {
      return false;
    }

    // If it's the same user, should not happen though
    if (doctor.getId() == patientId) {
      return false; // Other functions will handle this
    }

    return appointmentService.hasPatientMetDoctor(patientId, doctor.getId());
  }

  // ================= Appointment =================

  public boolean isInvolvedInAppointment(Authentication authentication, long appointmentId) {
    if (isInvalid(authentication) || appointmentId < 0) {
      return false;
    }

    PawAuthUserDetails user = (PawAuthUserDetails) authentication.getPrincipal();
    if (user == null) {
      return false;
    }

    Appointment appointment = appointmentService.getAppointmentById(appointmentId).orElse(null);
    if (appointment == null) {
      return false;
    }

    return appointment.getDoctor().getId() == user.getId()
        || appointment.getPatient().getId() == user.getId();
  }

  // ================== Notification ==================

  public boolean isNotificationRecipient(Authentication authentication, long notificationId) {

    if (isInvalid(authentication) || notificationId < 0) {
      return false;
    }

    PawAuthUserDetails user = (PawAuthUserDetails) authentication.getPrincipal();
    if (user == null) {
      return false;
    }

    Notification notification = notificationService.getNotification(notificationId).orElse(null);
    if (notification == null) {
      // So we can return 404
      return true;
    }

    return notification.getUser().getId() == user.getId();
  }

  // ================== Private =======================

  private boolean isInvalid(Authentication authentication) {
    return authentication == null
        || !authentication.isAuthenticated()
        || authentication.getPrincipal() == null;
  }
}
