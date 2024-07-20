package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.interfaces.services.exceptions.AppointmentNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.CancelForbiddenException;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import ar.edu.itba.paw.webapp.dto.AppointmentDto;
import ar.edu.itba.paw.webapp.exceptions.AppointmentAlreadyCancelledException;
import ar.edu.itba.paw.webapp.form.CancelAppointmentForm;
import ar.edu.itba.paw.webapp.query.AppointmentQuery;
import ar.edu.itba.paw.webapp.query.PageQuery;
import ar.edu.itba.paw.webapp.query.UserQuery;
import ar.edu.itba.paw.webapp.utils.ResponseUtil;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Path("appointments")
@Component
public class AppointmentController {

  private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentController.class);

  private final AppointmentService appointmentService;
  private final PatientService patientService;
  private static final int DEFAULT_PAGE_SIZE = 10;

  @Context private UriInfo uriInfo;

  @Autowired
  public AppointmentController(
      final AppointmentService appointmentService, PatientService patientService) {
    this.appointmentService = appointmentService;
    this.patientService = patientService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @PreAuthorize("@authorizationFunctions.isUser(authentication, #userQuery.userId)")
  public Response getAppointments(
      @Valid @BeanParam final UserQuery userQuery,
      @Valid @BeanParam final AppointmentQuery appointmentQuery,
      @Valid @BeanParam final PageQuery pageQuery) {
    long userId = userQuery.getUserId();
    LOGGER.debug(
        "\n\n\nValues are:\nuserId {}\nstatus {}\npage {}\npageSize {}\n\n\n",
        userId,
        appointmentQuery.getAppointmentStatus(),
        pageQuery.getPage(),
        pageQuery.getPageSize());

    Page<Appointment> appointmentsPage =
        appointmentService.getFilteredAppointments(
            userId,
            appointmentQuery.getAppointmentStatus(),
            pageQuery.getPage(),
            pageQuery.getPageSize(),
            patientService
                .getPatientById(userId)
                .isPresent() // TODO: remove argument, appointment service should handle it
            );

    List<Appointment> appointmentList = appointmentsPage.getContent();
    if (appointmentList.isEmpty()) {
      LOGGER.debug("No content for page {}", pageQuery.getPage());
      return Response.noContent().build();
    }

    final List<AppointmentDto> dtoList =
        AppointmentDto.fromAppointmentList(uriInfo, appointmentList);

    LOGGER.debug("Appointments for page {}", pageQuery.getPage());

    Response.ResponseBuilder responseBuilder =
        Response.ok(new GenericEntity<List<AppointmentDto>>(dtoList) {});

    return ResponseUtil.setPaginationLinks(responseBuilder, uriInfo, appointmentsPage).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id:\\d+}")
  @PreAuthorize("@authorizationFunctions.isInvolvedInAppointment(authentication, #id)")
  public Response getAppointment(@PathParam("id") final long id)
      throws AppointmentNotFoundException {

    Optional<Appointment> possibleAppointment = appointmentService.getAppointmentById(id);

    if (!possibleAppointment.isPresent()) {
      LOGGER.debug("appointment with id {} not found", id);
      throw new AppointmentNotFoundException();
    }

    Appointment appointment = possibleAppointment.get();

    LOGGER.debug("returning appointment with id {}", id);
    return Response.ok(AppointmentDto.fromAppointment(uriInfo, appointment)).build();
  }

  @PATCH
  @Path("/{id:\\d+}")
  @PreAuthorize("@authorizationFunctions.isInvolvedInAppointment(authentication, #id)")
  public Response cancelAppointment(
      @PathParam("id") final long id, @Valid final CancelAppointmentForm cancelAppointmentForm)
      throws AppointmentNotFoundException, AppointmentAlreadyCancelledException {
    Optional<Appointment> possibleAppointment = appointmentService.getAppointmentById(id);

    if (!possibleAppointment.isPresent()) {
      LOGGER.debug("appointment with id {} not found", id);
      throw new AppointmentNotFoundException();
    }

    Appointment appointment = possibleAppointment.get();

    if (appointment.getStatus().equals(AppointmentStatus.CANCELLED)) {
      LOGGER.debug("appointment with id {} was already cancelled", id);
      throw new AppointmentAlreadyCancelledException();
    }

    try {
      appointmentService.cancelAppointment(
          id, cancelAppointmentForm.getDescription(), PawAuthUserDetails.getCurrentUserId());
    } catch (CancelForbiddenException e) {
      LOGGER.debug("User should have been preauthorized");
      throw new IllegalStateException();
    }

    return Response.ok(AppointmentDto.fromAppointment(uriInfo, appointment)).build();
  }
}
