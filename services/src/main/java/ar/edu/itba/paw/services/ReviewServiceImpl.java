package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.interfaces.services.ReviewService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.PatientNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.ReviewForbiddenException;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Review;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewServiceImpl implements ReviewService {

  private final DoctorService doctorService;
  private final PatientService patientService;
  private final AppointmentService appointmentService;

  @Autowired
  public ReviewServiceImpl(
      DoctorService doctorService,
      PatientService patientService,
      AppointmentService appointmentService) {
    this.doctorService = doctorService;
    this.patientService = patientService;
    this.appointmentService = appointmentService;
  }

  // =============== Inserts ===============

  @Transactional
  @Override
  public Review createReview(long doctorId, long patientId, int rating, String description) throws DoctorNotFoundException, PatientNotFoundException, ReviewForbiddenException {
    if (!doctorService.getDoctorById(doctorId).isPresent()) {
      throw new DoctorNotFoundException();
    }

    if (!patientService.getPatientById(patientId).isPresent()) {
      throw new PatientNotFoundException();
    }

    if (!appointmentService.hasPatientMetDoctor(patientId, doctorId)) {
      throw new ReviewForbiddenException();
    }

    Doctor doctor = doctorService.getDoctorById(doctorId).get();
    Patient patient = patientService.getPatientById(patientId).get();

    return doctorService.addReview(doctor.getId(), new Review(null, doctor, patient, LocalDate.now(), description, (short) rating));
  }

  // =============== Queries ===============

  @Override
  public boolean canReview(long doctorId, long patientId) {

    boolean doctorExists = doctorService.getDoctorById(doctorId).isPresent();
    if (!doctorExists) {
      return false;
    }

    boolean patientExists = patientService.getPatientById(patientId).isPresent();
    if (!patientExists) {
      return false;
    }

    return appointmentService.hasPatientMetDoctor(patientId, doctorId);
  }

  @Transactional
  @Override
  public Page<Review> getReviewsForDoctor(long doctorId, Integer page, Integer pageSize) {
    return doctorService.getReviewsForDoctor(doctorId, page, pageSize);
  }
}
