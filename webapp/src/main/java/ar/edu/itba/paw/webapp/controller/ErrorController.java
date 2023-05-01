package ar.edu.itba.paw.webapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {

  @RequestMapping("/errors/403")
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ModelAndView error403() {
    return new ModelAndView("/errors/403");
  }

  @RequestMapping("/errors/404")
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ModelAndView error404() {
    return new ModelAndView("/errors/404");
  }

  @RequestMapping("/errors/500")
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ModelAndView error500() {
    return new ModelAndView("/errors/500");
  }
}
