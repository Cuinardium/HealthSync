package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.IndicationDao;
import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.IndicationService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.exceptions.AppointmentNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Indication;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class IndicationServiceImpl implements IndicationService {

    private final UserService userService;

    private final AppointmentService appointmentService;
    private final IndicationDao indicationDao;

    @Autowired
    public IndicationServiceImpl(UserService userService, AppointmentService appointmentService, IndicationDao indicationDao) {
        this.userService = userService;
        this.appointmentService = appointmentService;
        this.indicationDao = indicationDao;
    }


    @Override
    public Indication createIndication(long appointmentId, long userId, String description)
            throws UserNotFoundException, AppointmentNotFoundException {
        if (!appointmentService.getAppointmentById(appointmentId).isPresent()) {
            throw new AppointmentNotFoundException();
        }
        if (!userService.getUserById(userId).isPresent()) {
            throw new UserNotFoundException();
        }

        Appointment appointment= appointmentService.getAppointmentById(appointmentId).get();
        User user= userService.getUserById(userId).get();

        Indication indication=
                new Indication.Builder(appointment, user, LocalDate.now(), description).build();
        return indicationDao.createIndication(indication);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Indication> getIndicationsForAppointment(long appointmentId, Integer page, Integer pageSize) throws AppointmentNotFoundException {
        return indicationDao.getIndicationsForAppointment(appointmentId, page, pageSize);
    }
}
