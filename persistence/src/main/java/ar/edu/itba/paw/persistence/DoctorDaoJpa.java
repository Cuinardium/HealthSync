package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.DoctorDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.models.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class DoctorDaoJpa implements DoctorDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Doctor createDoctor(Doctor doctor) throws DoctorAlreadyExistsException, IllegalStateException {
        em.persist(doctor);
        return doctor;
    }

    @Override
    public Doctor updateDoctorInfo(long doctorId, Specialty specialty, City city, String address, List<HealthInsurance> healthInsurances, Set<AttendingHours> attendingHours) throws DoctorNotFoundException {
        Doctor doctor = getDoctorById(doctorId).orElseThrow(DoctorNotFoundException::new);
        doctor.setSpecialty(specialty);
        doctor.setLocation(new Location(doctorId, city, address));
        doctor.setHealthInsurances(healthInsurances);
        doctor.setAttendingHours(attendingHours);
        em.persist(doctor);
        return doctor;
    }

    @Override
    public Optional<Doctor> getDoctorById(long id) {
        return Optional.ofNullable(em.find(Doctor.class, id));
    }

    @Override
    public Page<Doctor> getFilteredDoctors(String name, LocalDate date, ThirtyMinuteBlock fromTime, ThirtyMinuteBlock toTime, Specialty specialty, City city, HealthInsurance healthInsurance, Integer page, Integer pageSize) {
        return null;
    }

    @Override
    public List<Doctor> getDoctors() {
        return em.createQuery("from Doctor", Doctor.class).getResultList();
    }

    @Override
    public Map<HealthInsurance, Integer> getUsedHealthInsurances() {
        List<List<HealthInsurance>> hList = em.createQuery("from Doctor", Doctor.class).getResultList().stream().map(Doctor::getHealthInsurances).collect(Collectors.toCollection(ArrayList::new));

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
        List<Specialty> sList = em.createQuery("from Doctor", Doctor.class).getResultList().stream().map(Doctor::getSpecialty).collect(Collectors.toCollection(ArrayList::new));

        Map<Specialty, Integer> map = new HashMap<>();

        for (Specialty s : sList) {
            map.putIfAbsent(s, 0);
            map.put(s, map.get(s) + 1);
        }

        return map;
    }

    @Override
    public Map<City, Integer> getUsedCities() {
        List<Location> lList = em.createQuery("from Doctor", Doctor.class).getResultList().stream().map(Doctor::getLocation).collect(Collectors.toCollection(ArrayList::new));

        Map<City, Integer> map = new HashMap<>();

        for (Location l : lList) {
            City c = l.getCity();
            map.putIfAbsent(c, 0);
            map.put(c, map.get(c) + 1);
        }

        return map;
    }

}
