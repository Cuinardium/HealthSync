package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.webapp.dto.AppointmentDto;
import ar.edu.itba.paw.webapp.exceptions.AppointmentNotFoundException;
import java.util.List;
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

  // TODO: should only return appointments user is involved in
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAppointments() {
    // TODO: both should be taken from jwt?
    long userId = 1;
    boolean isPatient = true;
    List<Appointment> appointmentList = appointmentService.getAppointments(userId, isPatient);
    List<AppointmentDto> dtoList = AppointmentDto.fromAppointmentList(uriInfo, appointmentList);
    return Response.ok(new GenericEntity<List<AppointmentDto>>(dtoList) {}).build();
  }

  // TODO: should be authorized? - like only return if user is involved
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  public Response getAppointment(@PathParam("id") final long id)
      throws AppointmentNotFoundException {
    Appointment appointment =
        appointmentService.getAppointmentById(id).orElseThrow(AppointmentNotFoundException::new);
    LOGGER.debug("returning appointment with id {}", id);
    return Response.ok(AppointmentDto.fromAppointment(uriInfo, appointment)).build();
  }
}
