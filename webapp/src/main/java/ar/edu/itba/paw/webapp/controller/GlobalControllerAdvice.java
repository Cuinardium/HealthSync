package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.NotificationService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import ar.edu.itba.paw.webapp.auth.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

  private final UserService userService;

  private final NotificationService notificationService;

  @Autowired
  public GlobalControllerAdvice(final UserService userService, NotificationService notificationService) {
    this.userService = userService;
    this.notificationService = notificationService;
  }

  @ModelAttribute("user")
  public User getCurrentUser() {
    long userId = PawAuthUserDetails.getCurrentUserId();
    return userService.getUserById(userId).orElse(null);
  }

  @ModelAttribute("isDoctor")
  public boolean isDoctor() {
    return UserRole.ROLE_DOCTOR.equals(PawAuthUserDetails.getRole());
  }

  @ModelAttribute("isPatient")
  public boolean isPatient() {
    return UserRole.ROLE_PATIENT.equals(PawAuthUserDetails.getRole());
  }

  @ModelAttribute("hasNotifications")
  public boolean hasNotifications(){
    long userId = PawAuthUserDetails.getCurrentUserId();
    return !(notificationService.getUserNotifications(userId).isEmpty());
  }
}
