package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.exceptions.*;
import ar.edu.itba.paw.models.File;
import ar.edu.itba.paw.models.Indication;
import ar.edu.itba.paw.models.Page;

import java.util.Optional;

public interface IndicationService {

    // =============== Inserts ===============

    public Indication createIndication(long appointmentId, long userId, String description, File file)
            throws UserNotFoundException, AppointmentNotFoundException;


    // =============== Queries ===============

    public Page<Indication> getIndicationsForAppointment(long appointmentId, Integer page, Integer pageSize)
            throws AppointmentNotFoundException;

    public Optional<Indication> getIndication(long indicationId);
}
