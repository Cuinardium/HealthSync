package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import ar.edu.itba.paw.webapp.auth.UserRoles;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@Controller
public class DoctorController {
  private final DoctorService doctorService;

  private final AppointmentService appointmentService;

  @Autowired
  public DoctorController(final DoctorService doctorService, final AppointmentService appointmentService) {
    this.doctorService = doctorService;
    this.appointmentService = appointmentService;
  }

  @RequestMapping(value = "/{id:\\d+}/detailed_doctor", method = RequestMethod.GET)
  public ModelAndView detailedDoctor(@PathVariable("id") final int doctorId) {

    Doctor doctor = doctorService.getDoctorById(doctorId).orElseThrow(UserNotFoundException::new);

    final ModelAndView mav = new ModelAndView("doctor/detailedDoctor");

    mav.addObject("doctor", doctor);

    // Only patients can book appointments
    boolean canBook = PawAuthUserDetails.getRole().equals(UserRoles.ROLE_PATIENT);

    //Booking starts from the day after today because of not allowing to book appointments in the past
    LocalDate tomorrow = LocalDate.now().plusDays(1);

    //Patients will only be able to book appointments between tomorrow and a year from now
    List<List<ThirtyMinuteBlock>> hoursAvailable = appointmentService.getAvailableHoursForDoctorOnRange(doctorId, tomorrow, tomorrow.plusMonths(1));

    mav.addObject("canBook", canBook);
    mav.addObject("hoursAvailable", hoursAvailable);
    return mav;
  }
}
