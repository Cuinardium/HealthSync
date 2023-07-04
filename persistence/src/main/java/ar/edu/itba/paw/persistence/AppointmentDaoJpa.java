package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.AppointmentDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.AppointmentAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.AppointmentNotFoundException;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.persistence.utils.QueryBuilder;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class AppointmentDaoJpa implements AppointmentDao {

  @PersistenceContext private EntityManager em;

  @Override
  public Appointment createAppointment(
      Patient patient,
      Doctor doctor,
      LocalDate date,
      ThirtyMinuteBlock timeBlock,
      String description)
      throws AppointmentAlreadyExistsException {
    final Optional<Appointment> possible_collition =
        getAppointment(doctor.getId(), date, timeBlock);
    if (possible_collition.isPresent()
        && possible_collition.get().getStatus() != AppointmentStatus.CANCELLED) {
      throw new AppointmentAlreadyExistsException();
    }

    // TODO: check that appointment is in future

    final Appointment app =
        new Appointment.Builder(patient, doctor, date, timeBlock, description).build();

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
  public void completeAppointmentsInDateBlock(LocalDate date, ThirtyMinuteBlock timeBlock) {
    // JPA Query Language (JQL) / Hibernate Query Language (HQL)
    final TypedQuery<Appointment> query =
        em.createQuery(
            "from Appointment as app where app.date = :date and app.timeBlock = :timeBlock",
            Appointment.class);
    query.setParameter("date", date);
    query.setParameter("timeBlock", timeBlock);

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
    // JPA Query Language (JQL) / Hibernate Query Language (HQL)
    final TypedQuery<Appointment> query =
        em.createQuery(
            "from Appointment as app where app.doctor.id = :doctorId and app.date = :date and app.timeBlock = :timeBlock",
            Appointment.class);
    query.setParameter("doctorId", doctorId);
    query.setParameter("date", date);
    query.setParameter("timeBlock", timeBlock);

    return query.getResultList().stream().findFirst();
  }

  @Override
  public List<Appointment> getAppointments(long userId, boolean isPatient) {

    StringBuilder queryBuilder =
        new StringBuilder()
            .append("from Appointment as app where app.")
            .append(isPatient ? "patient." : "doctor.")
            .append("id = :userId");

    // JPA Query Language (JQL) / Hibernate Query Language (HQL)
    final TypedQuery<Appointment> query =
        em.createQuery(queryBuilder.toString(), Appointment.class);
    query.setParameter("userId", userId);

    return query.getResultList();
  }

  @Override
  public Page<Appointment> getFilteredAppointments(
      Long userId,
      AppointmentStatus status,
      LocalDate from,
      LocalDate to,
      Integer page,
      Integer pageSize,
      Boolean isPatient) {

    QueryBuilder nativeQueryBuilder =
        new QueryBuilder().select("appointment_id").distinct().from("appointment");

    if (isPatient != null && userId != null) {
      String userField = isPatient ? "appointment.patient_id" : "appointment.doctor_id";
      nativeQueryBuilder.where(userField + "=" + userId);
    }

    if (status != null) {
      nativeQueryBuilder.where("status_code = " + status.ordinal());
    }

    if (from != null) {
      nativeQueryBuilder.where("appointment_date >= '" + Date.valueOf(from) + "'");
    }

    if (to != null) {
      nativeQueryBuilder.where("appointment_date <= '" + Date.valueOf(to) + "'");
    }

    String builtQuery = nativeQueryBuilder.build();

    Query nativeQuery = em.createNativeQuery(builtQuery);
    Query qtyAppointmentsQuery = em.createNativeQuery(builtQuery);

    if (page != null && page >= 0 && pageSize != null && pageSize > 0) {
      nativeQuery.setMaxResults(pageSize);
      nativeQuery.setFirstResult(page * pageSize);
    }

    @SuppressWarnings("unchecked")
    final List<Long> idList =
        (List<Long>)
            nativeQuery
                .getResultList()
                .stream()
                .map(o -> ((Number) o).longValue())
                .collect(Collectors.toList());

    if (idList.isEmpty()) {
      return new Page<>(Collections.emptyList(), page, 0, pageSize);
    }
    // JPA Query Language (JQL) / Hibernate Query Language (HQL)
    final TypedQuery<Appointment> query =
        em.createQuery(
            "from Appointment where id in :idList order by date ASC, timeBlock ASC",
            Appointment.class);
    query.setParameter("idList", idList);

    List<Appointment> content = query.getResultList();
    int qtyAppointments = qtyAppointmentsQuery.getResultList().size();

    return new Page<>(content, page, qtyAppointments, pageSize);
  }

  @Override
  public boolean hasPatientMetDoctor(long patientId, long doctorId) {
    // JPA Query Language (JQL) / Hibernate Query Language (HQL)
    final TypedQuery<Appointment> query =
        em.createQuery(
            "from Appointment as app where app.doctor.id = :doctorId and app.patient.id = :patientId and app.status = :status",
            Appointment.class);
    query.setParameter("doctorId", doctorId);
    query.setParameter("patientId", patientId);
    query.setParameter("status", AppointmentStatus.COMPLETED);

    return query.getResultList().stream().findFirst().isPresent();
  }

  @Override
  public List<Appointment> getAllConfirmedAppointmentsInDateBlock(
      LocalDate date, ThirtyMinuteBlock timeBlock) {
    // JPA Query Language (JQL) / Hibernate Query Language (HQL)
    final TypedQuery<Appointment> query =
        em.createQuery(
            "from Appointment as app where app.date = :date and app.status = :status and app.timeBlock = :timeBlock",
            Appointment.class);
    query.setParameter("date", date);
    query.setParameter("status", AppointmentStatus.CONFIRMED);
    query.setParameter("timeBlock", timeBlock);

    return query.getResultList();
  }
}
