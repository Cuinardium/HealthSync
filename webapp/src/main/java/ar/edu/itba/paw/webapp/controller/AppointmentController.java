package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.webapp.dto.AppointmentDto;
import java.util.Optional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("appointments")
@Component
public class AppointmentController {

  private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentController.class);

  private final AppointmentService appointmentService;

  @Context private UriInfo uriInfo;

  @Autowired
  public AppointmentController(final AppointmentService appointmentService) {
    this.appointmentService = appointmentService;
  }

  // TODO: Add authentication so the appointment is only visible to the users involved
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  public Response getAppointment(@PathParam("id") final long id) {

    Optional<Appointment> possibleAppointment = appointmentService.getAppointmentById(id);

    if (!possibleAppointment.isPresent()) {
      LOGGER.debug("appointment with id {} not found", id);
      return Response.status(Response.Status.NOT_FOUND).build();
    }

    Appointment appointment = possibleAppointment.get();

    LOGGER.debug("returning appointment with id {}", id);
    return Response.ok(AppointmentDto.fromAppointment(uriInfo, appointment)).build();
  }
}
