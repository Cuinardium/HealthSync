package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.DoctorDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.VacationCollisionException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.VacationNotFoundException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.utils.QueryBuilder;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class DoctorDaoJpa implements DoctorDao {

  @PersistenceContext private EntityManager em;

  // =============== Inserts ===============

  @Override
  public Doctor createDoctor(Doctor doctor) throws DoctorAlreadyExistsException {

    if (doctor.getId() != null && getDoctorById(doctor.getId()).isPresent()) {
      throw new DoctorAlreadyExistsException();
    }

    mapAttendingHours(doctor);
    em.persist(doctor);
    return doctor;
  }

  // ================= Updates ==============

  @Override
  public Doctor updateDoctorInfo(
      long doctorId,
      Specialty specialty,
      String city,
      String address,
      Set<HealthInsurance> healthInsurances,
      Set<AttendingHours> attendingHours)
      throws DoctorNotFoundException {
    Doctor doctor = getDoctorById(doctorId).orElseThrow(DoctorNotFoundException::new);
    doctor.setSpecialty(specialty);
    doctor.setCity(city);
    doctor.setAddress(address);
    doctor.setHealthInsurances(healthInsurances);
    doctor.setAttendingHours(attendingHours);
    mapAttendingHours(doctor);
    em.persist(doctor);
    return doctor;
  }

  @Override
  public Doctor addVacation(long doctorId, Vacation vacation)
      throws DoctorNotFoundException, VacationCollisionException {
    Doctor doctor = getDoctorById(doctorId).orElseThrow(DoctorNotFoundException::new);

    // Check vacation does not collide with other vacations
    Set<Vacation> vacations = doctor.getVacations();

    for (Vacation doctorVacation : vacations) {
      boolean vacationFromAfterDoctorVacationFrom =
          vacation.getFromDate().isAfter(doctorVacation.getFromDate())
              || (vacation.getFromDate().equals(doctorVacation.getFromDate())
                  && vacation.getFromTime().ordinal() >= doctorVacation.getFromTime().ordinal());
      boolean vacationFromBeforeDoctorVacationTo =
          vacation.getFromDate().isBefore(doctorVacation.getToDate())
              || (vacation.getFromDate().equals(doctorVacation.getToDate())
                  && vacation.getFromTime().ordinal() <= doctorVacation.getToTime().ordinal());
      boolean vacationToAfterDoctorVacationFrom =
          vacation.getToDate().isAfter(doctorVacation.getFromDate())
              || (vacation.getToDate().equals(doctorVacation.getFromDate())
                  && vacation.getToTime().ordinal() >= doctorVacation.getFromTime().ordinal());
      boolean vacationToBeforeDoctorVacationTo =
          vacation.getToDate().isBefore(doctorVacation.getToDate())
              || (vacation.getToDate().equals(doctorVacation.getToDate())
                  && vacation.getToTime().ordinal() <= doctorVacation.getToTime().ordinal());

      boolean vacationCollidesWithDoctorVacation =
          (vacationFromAfterDoctorVacationFrom && vacationFromBeforeDoctorVacationTo)
              || (vacationToAfterDoctorVacationFrom && vacationToBeforeDoctorVacationTo)
              || (vacationFromBeforeDoctorVacationTo && vacationToAfterDoctorVacationFrom)
              || (vacationFromAfterDoctorVacationFrom && vacationToBeforeDoctorVacationTo);

      if (vacationCollidesWithDoctorVacation) {
        throw new VacationCollisionException();
      }
    }

    vacation.setDoctor(doctor);

    doctor.addVacation(vacation);
    em.persist(doctor);
    return doctor;
  }

  @Override
  public Doctor removeVacation(long doctorId, Vacation vacation)
      throws DoctorNotFoundException, VacationNotFoundException {
    Doctor doctor = getDoctorById(doctorId).orElseThrow(DoctorNotFoundException::new);

    if (!doctor.getVacations().contains(vacation)) {
      throw new VacationNotFoundException();
    }

    doctor.removeVacation(vacation);
    em.persist(doctor);
    return doctor;
  }

  // =============== Queries ===============

  @Override
  public Optional<Doctor> getDoctorById(long id) {
    return Optional.ofNullable(em.find(Doctor.class, id));
  }

  @Override
  public Page<Doctor> getFilteredDoctors(
      String name,
      LocalDate date,
      ThirtyMinuteBlock fromTime,
      ThirtyMinuteBlock toTime,
      Specialty specialty,
      String city,
      HealthInsurance healthInsurance,
      Integer minRating,
      Integer page,
      Integer pageSize) {

    int specialtyCode = specialty != null ? specialty.ordinal() : -1;
    String cityS = city;
    int healthInsuranceCode = healthInsurance != null ? healthInsurance.ordinal() : -1;

    // Start building the query
    QueryBuilder nativeQueryBuilder =
        new QueryBuilder().select("doctor.doctor_id").from("doctor").groupBy("doctor.doctor_id");

    // Add the filters to the query, if it is the first filter, don't add AND
    if (name != null && !name.isEmpty()) {
      String nameQuery =
          new QueryBuilder()
              .select("user_id as doctor_id")
              .from("users")
              .where("CONCAT(first_name, ' ', last_name) ILIKE CONCAT('%', :name, '%')")
              .build();

      nativeQueryBuilder.where("doctor.doctor_id IN (" + nameQuery + ")");
    }

    if (specialtyCode >= 0) {
      nativeQueryBuilder.where("specialty_code = " + specialtyCode);
    }

    if (cityS != null && !cityS.isEmpty()) {
      nativeQueryBuilder.where("city = :city");
    }

    if (healthInsuranceCode >= 0) {
      String healthInsuranceQuery =
          new QueryBuilder()
              .select("doctor_id")
              .from("health_insurance_accepted_by_doctor")
              .where("health_insurance_code = " + healthInsuranceCode)
              .build();

      nativeQueryBuilder.where("doctor.doctor_id IN (" + healthInsuranceQuery + ")");
    }

    if (date != null && fromTime != null && toTime != null) {
      // Query de horarios disponibles (attending hours) y luego hago el horario que me pide NOT IN
      // appintments en el rang y fecha

      String dateSQLString = "'" + Date.valueOf(date) + "'";

      String appointmentQuery =
          new QueryBuilder()
              .select("appointment.appointment_time")
              .from("appointment")
              .where("appointment.doctor_id = doctor.doctor_id")
              .where("appointment.appointment_date = " + dateSQLString)
              .build();

      String vacationQuery =
          new QueryBuilder()
              .select("doctor_id")
              .from("doctor_vacation")
              .where(
                  "(("
                      + dateSQLString
                      + " > doctor_vacation.from_date"
                      + " AND "
                      + dateSQLString
                      + " < doctor_vacation.to_date)"
                      + " OR ("
                      + dateSQLString
                      + " = doctor_vacation.from_date AND "
                      + fromTime.ordinal()
                      + " >= doctor_vacation.from_time)"
                      + " OR ("
                      + dateSQLString
                      + " = doctor_vacation.to_date AND "
                      + toTime.ordinal()
                      + " <= doctor_vacation.to_time))")
              .build();

      String attendingHoursQuery =
          new QueryBuilder()
              .select("doctor_attending_hours.doctor_id")
              .from("doctor_attending_hours")
              .where("doctor_attending_hours.doctor_id = doctor.doctor_id")
              .where("doctor_attending_hours.day = " + date.getDayOfWeek().ordinal())
              .where(
                  "doctor_attending_hours.hour_block BETWEEN "
                      + fromTime.ordinal()
                      + " AND "
                      + toTime.ordinal())
              .where("doctor_attending_hours.hour_block NOT IN (" + appointmentQuery + ")")
              .build();

      nativeQueryBuilder.where("doctor.doctor_id IN (" + attendingHoursQuery + ")");
      nativeQueryBuilder.where("doctor.doctor_id NOT IN (" + vacationQuery + ")");
    }

    if (minRating != null && minRating >= 0 && minRating <= 5) {
      String ratingQuery =
          new QueryBuilder()
              .select("doctor_id")
              .from("review")
              .groupBy("doctor_id")
              .having("AVG(rating) >= " + minRating)
              .having("COUNT(rating) > 0")
              .build();

      nativeQueryBuilder.where("doctor.doctor_id IN (" + ratingQuery + ")");
    }

    String builtQuery = nativeQueryBuilder.build();

    Query nativeQuery = em.createNativeQuery(builtQuery);
    Query qtyDoctorsQuery = em.createNativeQuery(builtQuery);

    if (name != null && !name.isEmpty()) {
      nativeQuery.setParameter("name", name);
      qtyDoctorsQuery.setParameter("name", name);
    }

    if (cityS != null && !cityS.isEmpty()) {
      nativeQuery.setParameter("city", cityS);
      qtyDoctorsQuery.setParameter("city", cityS);
    }

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
      return new Page<>(new ArrayList<>(), page, 0, pageSize);
    }

    final TypedQuery<Doctor> query =
        em.createQuery("from Doctor where id in :idList", Doctor.class);
    query.setParameter("idList", idList);

    List<Doctor> content = query.getResultList();
    int qtyDoctors = qtyDoctorsQuery.getResultList().size();

    return new Page<>(content, page, qtyDoctors, pageSize);
  }

  @Override
  public List<Doctor> getDoctors() {
    return em.createQuery("from Doctor", Doctor.class).getResultList();
  }

  @Override
  public Map<HealthInsurance, Integer> getUsedHealthInsurances() {
    List<Set<HealthInsurance>> hList =
        em.createQuery("from Doctor", Doctor.class)
            .getResultList()
            .stream()
            .map(Doctor::getHealthInsurances)
            .collect(Collectors.toCollection(ArrayList::new));

    Map<HealthInsurance, Integer> map = new HashMap<>();

    for (Set<HealthInsurance> set : hList) {
      for (HealthInsurance h : set) {
        map.put(h, map.getOrDefault(h, 0) + 1);
      }
    }
    return map;
  }

  @Override
  public Map<Specialty, Integer> getUsedSpecialties() {
    List<Specialty> sList =
        em.createQuery("select specialty from Doctor", Specialty.class).getResultList();

    Map<Specialty, Integer> map = new HashMap<>();

    for (Specialty s : sList) {
      map.put(s, map.getOrDefault(s, 0) + 1);
    }

    return map;
  }

  @Override
  public Map<String, Integer> getUsedCities() {
    List<String> lList = em.createQuery("select city from Doctor", String.class).getResultList();

    Map<String, Integer> map = new HashMap<>();

    for (String c : lList) {
      map.put(c, map.getOrDefault(c, 0) + 1);
    }

    return map;
  }

  private void mapAttendingHours(Doctor doctor) {
    for (AttendingHours att : doctor.getAttendingHours()) {
      att.setDoctor(doctor);
    }
  }

  @Override
  public List<Specialty> getPopularSpecialties() {
    final TypedQuery<Specialty> query =
        em.createQuery(
            "select doc.specialty from Doctor as doc group by doc.specialty order by count(*) desc",
            Specialty.class);
    query.setMaxResults(7);
    List<Specialty> sList = query.getResultList();

    return sList;
  }
}
