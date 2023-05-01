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
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

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

    Appointment appointment =
        appointmentDao.createAppointment(patientId, doctorId, date, timeBlock, description);

    // TODO: locale should be determined by the patient's language
    mailService.sendAppointmentRequestMail(appointment, doctor, patient, LocaleContextHolder.getLocale());
    mailService.sendAppointmentReminderMail(appointment, doctor, patient, LocaleContextHolder.getLocale());

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

  @Override
  public void updateAppointmentStatus(long appointmentId, AppointmentStatus status) {
    appointmentDao.updateAppointmentStatus(appointmentId, status);
  }
}