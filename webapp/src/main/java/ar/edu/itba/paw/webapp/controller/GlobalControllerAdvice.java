package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import ar.edu.itba.paw.webapp.auth.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

  private final UserService userService;

  @Autowired
  public GlobalControllerAdvice(final UserService userService) {
    this.userService = userService;
  }

  @ModelAttribute("user")
  public User getCurrentUser() {
    long userId = PawAuthUserDetails.getCurrentUserId();
    return userService.getUserById(userId).orElse(null);
  }

  @ModelAttribute("isDoctor")
  public boolean isDoctor() {
    return UserRoles.ROLE_DOCTOR.equals(PawAuthUserDetails.getRole());
  }

  @ModelAttribute("isPatient")
  public boolean isPatient() {
    return UserRoles.ROLE_PATIENT.equals(PawAuthUserDetails.getRole());
  }
}
