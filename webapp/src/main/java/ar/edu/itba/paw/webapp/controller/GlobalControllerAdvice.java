package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
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
    return userService.findById(userId).orElse(null);
  }
}
