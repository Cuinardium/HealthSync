package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.AppointmentDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.AppointmentNotFoundException;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class AppointmentDaoJpa implements AppointmentDao {

  @PersistenceContext EntityManager em;

  @Override
  public Appointment createAppointment(
      Patient patient,
      Doctor doctor,
      LocalDate date,
      ThirtyMinuteBlock timeBlock,
      String description) {
    final Appointment app =
        new Appointment(null, patient, doctor, date, timeBlock, null, description, null);

    em.persist(app);

    return app;
  }

  @Override
  public Appointment updateAppointment(
      long appointmentId, AppointmentStatus status, String cancelDescription)
      throws AppointmentNotFoundException {

    Appointment app =
        getAppointmentById(appointmentId).orElseThrow(AppointmentNotFoundException::new);

    app.setStatus(status);
    app.setCancelDesc(cancelDescription);

    em.persist(app);

    return app;
  }

  @Override
  public void completeAppointmentsInDate(LocalDate date) {
    // JPA Query Language (JQL) / Hibernate Query Language (HQL)
    final TypedQuery<Appointment> query =
        em.createQuery("from Appointment as app where app.date = :date", Appointment.class);
    query.setParameter("date", date);

    // get all appointments in date
    List<Appointment> appList = query.getResultList();

    for (Appointment appointment : appList) {
      appointment.setStatus(AppointmentStatus.COMPLETED);
      em.persist(appointment);
    }
  }

  @Override
  public Optional<Appointment> getAppointmentById(long appointmentId) {
    return Optional.ofNullable(em.find(Appointment.class, appointmentId));
  }

  @Override
  public Optional<Appointment> getAppointment(
      long doctorId, LocalDate date, ThirtyMinuteBlock timeBlock) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAppointment'");
  }

  @Override
  public List<Appointment> getAppointments(long userId, boolean isPatient) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAppointments'");
  }

  @Override
  public Page<Appointment> getFilteredAppointments(
      long userId,
      AppointmentStatus status,
      LocalDate from,
      LocalDate to,
      Integer page,
      Integer pageSize,
      boolean isPatient) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getFilteredAppointments'");
  }

  @Override
  public boolean hasPatientMetDoctor(long patientId, long doctorId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'hasPatientMetDoctor'");
  }

  @Override
  public List<Appointment> getAllConfirmedAppointmentsInDate(LocalDate date) {
    // JPA Query Language (JQL) / Hibernate Query Language (HQL)
    final TypedQuery<Appointment> query =
        em.createQuery(
            "from Appointment as app where app.date = :date and app.status = :status",
            Appointment.class);
    query.setParameter("date", date);
    query.setParameter("status", AppointmentStatus.CONFIRMED);

    return query.getResultList();
  }
}
