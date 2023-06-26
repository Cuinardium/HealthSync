package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.exceptions.*;
import ar.edu.itba.paw.models.Indication;
import ar.edu.itba.paw.models.Page;

public interface IndicationService {

    // =============== Inserts ===============

    public Indication createIndication(long appointmentId, long userId, String description)
            throws UserNotFoundException, AppointmentNotFoundException;

    // =============== Queries ===============

    public Page<Indication> getIndicationsForAppointment(long appointmentId, Integer page, Integer pageSize)
            throws AppointmentNotFoundException;
}
