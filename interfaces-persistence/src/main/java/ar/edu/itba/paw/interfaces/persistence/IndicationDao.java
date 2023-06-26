package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Indication;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Review;

public interface IndicationDao {

    // =============== Inserts ===============

    public Indication createIndication(Indication indication);

    // =============== Queries ===============

    public Page<Indication> getIndicationsForAppointment(long appointmentId, Integer page, Integer pageSize);
}
