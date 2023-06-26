package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.IndicationDao;
import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.IndicationService;
import ar.edu.itba.paw.interfaces.services.NotificationService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.exceptions.AppointmentNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Indication;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.User;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IndicationServiceImpl implements IndicationService {

  private final UserService userService;

  private final AppointmentService appointmentService;
  private final IndicationDao indicationDao;

  private final NotificationService notificationService;

  @Autowired
  public IndicationServiceImpl(
      UserService userService,
      AppointmentService appointmentService,
      IndicationDao indicationDao,
      NotificationService notificationService) {
    this.userService = userService;
    this.appointmentService = appointmentService;
    this.indicationDao = indicationDao;
    this.notificationService = notificationService;
  }

  @Transactional
  @Override
  public Indication createIndication(long appointmentId, long userId, String description)
      throws UserNotFoundException, AppointmentNotFoundException {

    Optional<Appointment> appointmentOptional =
        appointmentService.getAppointmentById(appointmentId);
    if (!appointmentOptional.isPresent()) {
      throw new AppointmentNotFoundException();
    }

    Optional<User> userOptional = userService.getUserById(userId);
    if (!userOptional.isPresent()) {
      throw new UserNotFoundException();
    }

    Appointment appointment = appointmentOptional.get();
    User user = userOptional.get();

    // Notify other user
    User otherUser;
    if (appointment.getDoctorId() == user.getId()) {
      otherUser = appointment.getPatient();
    } else {
      otherUser = appointment.getDoctor();
    }

    if (!notificationService
        .getUserAppointmentNotification(otherUser.getId(), appointmentId)
        .isPresent()) {
      notificationService.createNotification(otherUser.getId(), appointmentId);
    }

    Indication indication =
        new Indication.Builder(appointment, user, LocalDate.now(), description).build();
    return indicationDao.createIndication(indication);
  }

  @Transactional(readOnly = true)
  @Override
  public Page<Indication> getIndicationsForAppointment(
      long appointmentId, Integer page, Integer pageSize) throws AppointmentNotFoundException {
    return indicationDao.getIndicationsForAppointment(appointmentId, page, pageSize);
  }
}
