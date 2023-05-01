package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
  @ModelAttribute("user")
  public PawAuthUserDetails getCurrentUser() {
    return PawAuthUserDetails.getCurrentUser();
  }
}
