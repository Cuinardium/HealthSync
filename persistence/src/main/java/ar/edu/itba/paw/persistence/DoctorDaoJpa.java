package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.DoctorDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorNotFoundException;
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

  @Override
  public Doctor createDoctor(Doctor doctor)
      throws DoctorAlreadyExistsException, IllegalStateException {
    if (doctor.getId() != null && getDoctorById(doctor.getId()).isPresent()) {
      throw new DoctorAlreadyExistsException();
    }
    mapAttendingHours(doctor);
    em.persist(doctor);
    return doctor;
  }

  @Override
  public Review addReview(long doctorId, Review review) throws DoctorNotFoundException {
    Doctor doctor = getDoctorById(doctorId).orElseThrow(DoctorNotFoundException::new);
    doctor.getReviews().add(review);
    em.persist(doctor);
    return review;
  }

  @Override
  public Doctor updateDoctorInfo(
      long doctorId,
      Specialty specialty,
      City city,
      String address,
      List<HealthInsurance> healthInsurances,
      Set<AttendingHours> attendingHours,
      List<Review> reviews)
      throws DoctorNotFoundException {
    Doctor doctor = getDoctorById(doctorId).orElseThrow(DoctorNotFoundException::new);
    doctor.setSpecialty(specialty);
    doctor.setLocation(new Location(doctorId, city, address));
    doctor.setHealthInsurances(healthInsurances);
    doctor.setReviews(reviews);
    doctor.setAttendingHours(attendingHours);
    mapAttendingHours(doctor);
    em.persist(doctor);
    return doctor;
  }

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
      City city,
      HealthInsurance healthInsurance,
      Integer page,
      Integer pageSize) {

    int specialtyCode = specialty != null ? specialty.ordinal() : -1;
    int cityCode = city != null ? city.ordinal() : -1;
    int healthInsuranceCode = healthInsurance != null ? healthInsurance.ordinal() : -1;

    // Start building the query
    QueryBuilder nativeQueryBuilder =
        new QueryBuilder().select("doctor.doctor_id").from("doctor").groupBy("doctor.doctor_id");

    // Add the filters to the query, if it is the first filter, don't add AND
    if (name != null && !name.isEmpty()) {
      nativeQueryBuilder.where(
          "CONCAT(first_name, ' ', last_name) ILIKE CONCAT('" + name + "', '%')");
    }

    if (specialtyCode >= 0) {
      nativeQueryBuilder.where("specialty_code = " + specialtyCode);
    }

    if (cityCode >= 0) {
      nativeQueryBuilder.where("city_code = " + cityCode);
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

    if (date != null) {
      // Query de horarios disponibles (attending hours) y luego hago el horario que me pide NOT IN
      // appintments en el rang y fecha

      QueryBuilder appointmentQuery =
          new QueryBuilder()
              .select("appointment.appointment_time")
              .from("appointment")
              .where("appointment.doctor_id = doctor.doctor_id")
              .where("appointment.appointment_date = '" + Date.valueOf(date) + "'");

      QueryBuilder attendingHoursQuery =
          new QueryBuilder()
              .select("attending_hours.doctor_id")
              .from("attending_hours")
              .where("attending_hours.doctor_id = doctor.doctor_id")
              .where("attending_hours.day = " + date.getDayOfWeek().ordinal())
              .where(
                  "attending_hours.hour_block BETWEEN "
                      + fromTime.ordinal()
                      + " AND "
                      + toTime.ordinal())
              .where("attending_hours.hour_block NOT IN (" + appointmentQuery.build() + ")");

      nativeQueryBuilder.where("doctor.doctor_id IN (" + attendingHoursQuery.build() + ")");
    }

    Query nativeQuery = em.createNativeQuery(nativeQueryBuilder.build());

    if (page != null && page >= 0 && pageSize != null && pageSize > 0) {
      nativeQuery.setMaxResults(pageSize);
      nativeQuery.setFirstResult((page - 1) * pageSize);
    }

    final List<Long> idList =
        (List<Long>)
            nativeQuery
                .getResultList()
                .stream()
                .map(o -> ((Number) o).longValue())
                .collect(Collectors.toList());

    if(idList.isEmpty())
      return new Page<>(new ArrayList<>(), page, 0, pageSize);

    final TypedQuery<Doctor> query =
        em.createQuery("from Doctor where id in :idList", Doctor.class);
    query.setParameter("idList", idList);

    return new Page<>(query.getResultList(), page, query.getResultList().size(), pageSize);
  }

  @Override
  public List<Doctor> getDoctors() {
    return em.createQuery("from Doctor", Doctor.class).getResultList();
  }

  @Override
  public Map<HealthInsurance, Integer> getUsedHealthInsurances() {
    List<List<HealthInsurance>> hList =
        em.createQuery("from Doctor", Doctor.class)
            .getResultList()
            .stream()
            .map(Doctor::getHealthInsurances)
            .collect(Collectors.toCollection(ArrayList::new));

    Map<HealthInsurance, Integer> map = new HashMap<>();

    for (List<HealthInsurance> list : hList) {
      for (HealthInsurance h : list) {
        map.putIfAbsent(h, 0);
        map.put(h, map.get(h) + 1);
      }
    }
    return map;
  }

  @Override
  public Map<Specialty, Integer> getUsedSpecialties() {
    List<Specialty> sList =
        em.createQuery("from Doctor", Doctor.class)
            .getResultList()
            .stream()
            .map(Doctor::getSpecialty)
            .collect(Collectors.toCollection(ArrayList::new));

    Map<Specialty, Integer> map = new HashMap<>();

    for (Specialty s : sList) {
      map.putIfAbsent(s, 0);
      map.put(s, map.get(s) + 1);
    }

    return map;
  }

  @Override
  public Map<City, Integer> getUsedCities() {
    List<Location> lList =
        em.createQuery("from Doctor", Doctor.class)
            .getResultList()
            .stream()
            .map(Doctor::getLocation)
            .collect(Collectors.toCollection(ArrayList::new));

    Map<City, Integer> map = new HashMap<>();

    for (Location l : lList) {
      City c = l.getCity();
      map.putIfAbsent(c, 0);
      map.put(c, map.get(c) + 1);
    }

    return map;
  }

  @Override
  public Page<Review> getReviewsForDoctor(long doctorId, Integer page, Integer pageSize) {
    Query nativeQuery = em.createNativeQuery("SELECT review_id FROM review WHERE doctor_id = " + doctorId);

    if (page != null && page >= 0 && pageSize != null && pageSize > 0) {
      nativeQuery.setMaxResults(pageSize);
      nativeQuery.setFirstResult((page - 1) * pageSize);
    }

    final List<Long> idList =
            (List<Long>)
                    nativeQuery
                            .getResultList()
                            .stream()
                            .map(o -> ((Number) o).longValue())
                            .collect(Collectors.toList());

    if(idList.isEmpty())
      return new Page<>(new ArrayList<>(), page, 0, pageSize);

    final TypedQuery<Review> query =
            em.createQuery("from Review where id in :idList", Review.class);
    query.setParameter("idList", idList);

    return new Page<>(query.getResultList(), page, query.getResultList().size(), pageSize);
  }

  private void mapAttendingHours(Doctor doctor) {
    for (AttendingHours att : doctor.getAttendingHours()) {
      att.setDoctor(doctor);
    }
  }
}
