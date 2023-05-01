package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.exceptions.ImageNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionController {
  @ExceptionHandler(ImageNotFoundException.class)
  @ResponseStatus(code = HttpStatus.NOT_FOUND)
  public ModelAndView noSuchImage() {
    return new ModelAndView("/errors/404");
  }
}
