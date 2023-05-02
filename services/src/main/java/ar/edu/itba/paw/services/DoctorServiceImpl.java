package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.DoctorDao;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.LocationService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.AttendingHours;
import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.models.User;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorDao doctorDao;

    private final UserService userService;
    private final LocationService locationService;

    @Autowired
    public DoctorServiceImpl(
            DoctorDao doctorDao, UserService userService, LocationService locationService) {

        this.doctorDao = doctorDao;

        this.userService = userService;
        this.locationService = locationService;
    }

    @Override
    public Doctor createDoctor(
            String email,
            String password,
            String firstName,
            String lastName,
            int healthInsuranceCode,
            int specialtyCode,
            int cityCode,
            String address,
            AttendingHours attendingHours) {

        // Create user
        User user = userService.createUser(email, password, firstName, lastName);
        long userId = user.getId();
        long pfpId = user.getProfilePictureId();

        // Create Location
        long locationId = locationService.createLocation(cityCode, address);

        // Create doctor
        long doctorId = doctorDao.createDoctor(userId, specialtyCode, attendingHours);

        // Add location to doctor
        doctorDao.addLocation(doctorId, locationId);

        // Add health insurance to doctor
        doctorDao.addHealthInsurance(doctorId, healthInsuranceCode);

        // Enums
        HealthInsurance healthInsurance = HealthInsurance.values()[healthInsuranceCode];
        Specialty specialty = Specialty.values()[specialtyCode];
        Location location = new Location(locationId, City.values()[cityCode], address);

        return new Doctor(
                doctorId,
                email,
                password,
                firstName,
                lastName,
                pfpId,
                healthInsurance,
                specialty,
                location,
                attendingHours);
    }

    @Override
    public void updateInformation(long doctorId, String email, String firstName, String lastName, int healthInsuranceCode, int specialtyCode, int cityCode, String address) {
        userService.editUser(doctorId, email, firstName, lastName);
        doctorDao.updateInformation(doctorId, healthInsuranceCode, specialtyCode, cityCode, address);
    }

    @Override
    public void updateAttendingHours(long doctorId, AttendingHours attendingHours) {
        doctorDao.updateAttendingHours(doctorId, attendingHours);
    }

    @Override
    public Optional<Doctor> getDoctorById(long id) {
        return doctorDao.getDoctorById(id);
    }

    @Override
    public List<Doctor> getFilteredDoctors(
            String name, int specialtyCode, int cityCode, int healthInsuranceCode) {
        return doctorDao.getFilteredDoctors(name, specialtyCode, cityCode, healthInsuranceCode);
    }

    @Override
    public List<Doctor> getDoctors() {
        return doctorDao.getDoctors();
    }
}
