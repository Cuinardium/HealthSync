package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.AppointmentDao;
import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.MailService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

  MailService mailService;
  DoctorService doctorService;
  PatientService patientService;

  AppointmentDao appointmentDao;

  @Autowired
  public AppointmentServiceImpl(
      MailService mailService,
      DoctorService doctorService,
      PatientService patientService,
      AppointmentDao appointmentDao) {
    this.mailService = mailService;
    this.doctorService = doctorService;
    this.patientService = patientService;
    this.appointmentDao = appointmentDao;
  }

  @Transactional
  @Override
  public Appointment createAppointment(
      long patientId,
      long doctorId,
      LocalDate date,
      ThirtyMinuteBlock timeBlock,
      String description) {

    // TODO: error handling
    Doctor doctor = doctorService.getDoctorById(doctorId).orElseThrow(RuntimeException::new);
    Patient patient = patientService.getPatientById(patientId).orElseThrow(RuntimeException::new);

    Optional<Appointment> possibleAppointment =
        appointmentDao.getAppointment(doctorId, date, timeBlock);

    if (possibleAppointment.isPresent()
        && (possibleAppointment.get().getStatus() == AppointmentStatus.COMPLETED
            || possibleAppointment.get().getStatus() == AppointmentStatus.ACCEPTED
            || possibleAppointment.get().getStatus() == AppointmentStatus.PENDING)) {
      throw new RuntimeException();
    }

    Appointment appointment =
        appointmentDao.createAppointment(patientId, doctorId, date, timeBlock, description);

    // TODO: locale should be determined by the patient's language
    mailService.sendAppointmentRequestMail(
        appointment, doctor, patient, LocaleContextHolder.getLocale());
    mailService.sendAppointmentReminderMail(
        appointment, doctor, patient, LocaleContextHolder.getLocale());

    return appointment;
  }

  @Override
  public Optional<Appointment> getAppointmentById(long appointmentId) {
    return appointmentDao.getAppointmentById(appointmentId);
  }

  @Override
  public List<Appointment> getAppointmentsForPatient(long patientId) {
    return appointmentDao.getAppointmentsForPatient(patientId);
  }

  @Override
  public List<Appointment> getAppointmentsForDoctor(long doctorId) {
    return appointmentDao.getAppointmentsForDoctor(doctorId);
  }

  @Transactional
  @Override
  public void updateAppointmentStatus(
      long appointmentId, AppointmentStatus status, long requesterId) {

    // Get appointment
    // TODO: error handling
    Appointment appointment = getAppointmentById(appointmentId).orElseThrow(RuntimeException::new);

    // If requester is the patient, he can only cancel the appointment
    if (requesterId == appointment.getPatientId()) {
      if (status != AppointmentStatus.CANCELLED) {
        throw new RuntimeException();
      }
    }

    // If requester is nor the patient nor the doctor, he can't update the appointment
    if (requesterId != appointment.getPatientId() && requesterId != appointment.getDoctorId()) {
      throw new RuntimeException();
    }

    appointmentDao.updateAppointmentStatus(appointmentId, status);

    // TODO: error handling
    Doctor doctor =
        doctorService.getDoctorById(appointment.getDoctorId()).orElseThrow(RuntimeException::new);
    Patient patient =
        patientService
            .getPatientById(appointment.getPatientId())
            .orElseThrow(RuntimeException::new);
    Locale locale = LocaleContextHolder.getLocale();

    switch (status) {
      case ACCEPTED:
        mailService.sendAppointmentConfirmedMail(appointment, doctor, patient, locale);
        break;
      case CANCELLED:
        if (requesterId == appointment.getPatientId()) {
          mailService.sendAppointmentCancelledByPatientMail(appointment, doctor, patient, locale);
        } else {
          mailService.sendAppointmentCancelledByDoctorMail(appointment, doctor, patient, locale);
        }
        break;
      case REJECTED:
        mailService.sendAppointmentRejectedMail(appointment, doctor, patient, locale);
        break;
      default:
    }
  }

  @Override
  public List<ThirtyMinuteBlock> getAvailableHoursForDoctorOnDate(long doctorId, LocalDate date) {

    // Get doctor appointments
    List<Appointment> appointments = getAppointmentsForDoctor(doctorId);

    // TODO: error handling
    Doctor doctor = doctorService.getDoctorById(doctorId).orElseThrow(RuntimeException::new);

    // Get doctor available hours for date
    List<ThirtyMinuteBlock> availableHours = new ArrayList<>();
    for (ThirtyMinuteBlock block : doctor.getAttendingHours().getAttendingBlocksForDate(date)) {
      availableHours.add(block);
    }

    for (Appointment appointment : appointments) {
      if (appointment.getDate().equals(date)
          && (appointment.getStatus() == AppointmentStatus.ACCEPTED
              || appointment.getStatus() == AppointmentStatus.PENDING
              || appointment.getStatus() == AppointmentStatus.COMPLETED)) {
        availableHours.remove(appointment.getTimeBlock());
      }
    }

    return availableHours;
  }

  @Override
  public List<Appointment> getFilteredAppointmentsForDoctor(
      long doctorId, AppointmentStatus status, LocalDate from, LocalDate to) {
    return appointmentDao.getFilteredAppointmentsForDoctor(doctorId, status, from, to);
  }

  @Override
  public List<Appointment> getFilteredAppointmentsForPatient(
      long patientId, AppointmentStatus status, LocalDate from, LocalDate to) {
    return appointmentDao.getFilteredAppointmentsForPatient(patientId, status, from, to);
  }
}
