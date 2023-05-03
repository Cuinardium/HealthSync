package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.exceptions.AppointmentForbiddenException;
import ar.edu.itba.paw.webapp.exceptions.AppointmentNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.ImageNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionController {
  @ExceptionHandler(ImageNotFoundException.class)
  @ResponseStatus(code = HttpStatus.NOT_FOUND)
  public ModelAndView noSuchImage(ImageNotFoundException e) {
    ModelAndView mav = new ModelAndView("/errors/404");
    mav.addObject("message", e.getMessage());
    return mav;
  }

  @ExceptionHandler(UserNotFoundException.class)
  @ResponseStatus(code = HttpStatus.NOT_FOUND)
  public ModelAndView noSuchUser(UserNotFoundException e) {
    ModelAndView mav = new ModelAndView("/errors/404");
    mav.addObject("message", e.getMessage());
    return mav;
  }

  @ExceptionHandler(AppointmentNotFoundException.class)
  @ResponseStatus(code = HttpStatus.NOT_FOUND)
  public ModelAndView noSuchAppointment(AppointmentNotFoundException e) {
    ModelAndView mav = new ModelAndView("/errors/404");
    mav.addObject("message", e.getMessage());

    return mav;
  }

  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
  public ModelAndView internalServerError() {
    return new ModelAndView("/errors/500");
  }

  @ExceptionHandler(AppointmentForbiddenException.class)
  @ResponseStatus(code = HttpStatus.FORBIDDEN)
  public ModelAndView forbidden(AppointmentForbiddenException e) {
    ModelAndView mav = new ModelAndView("/errors/403");
    mav.addObject("message", e.getMessage());

    return mav;
  }
}
