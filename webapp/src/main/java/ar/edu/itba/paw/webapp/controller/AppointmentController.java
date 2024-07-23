package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.interfaces.services.exceptions.*;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import ar.edu.itba.paw.webapp.dto.AppointmentDto;
import ar.edu.itba.paw.webapp.form.AppointmentForm;
import ar.edu.itba.paw.webapp.form.CancelAppointmentForm;
import ar.edu.itba.paw.webapp.mediaType.VndType;
import ar.edu.itba.paw.webapp.query.AppointmentQuery;
import ar.edu.itba.paw.webapp.query.PageQuery;
import ar.edu.itba.paw.webapp.query.UserQuery;
import ar.edu.itba.paw.webapp.utils.ResponseUtil;
import java.net.URI;
import java.util.List;
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

  @Context private UriInfo uriInfo;

  @Autowired
  public AppointmentController(
      final AppointmentService appointmentService, PatientService patientService) {
    this.appointmentService = appointmentService;
    this.patientService = patientService;
  }

  // ================= appointments ========================

  @GET
  @Produces(VndType.APPLICATION_APPOINTMENT_LIST)
  @PreAuthorize("@authorizationFunctions.isUser(authentication, #userQuery.userId)")
  public Response getAppointments(
      @Valid @BeanParam final UserQuery userQuery,
      @Valid @BeanParam final AppointmentQuery appointmentQuery,
      @Valid @BeanParam final PageQuery pageQuery) {

    LOGGER.debug("Listing appointments, {}, {}, {}", userQuery, appointmentQuery, pageQuery);

    long userId = userQuery.getUserId();

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

    Response.ResponseBuilder responseBuilder =
        Response.ok(new GenericEntity<List<AppointmentDto>>(dtoList) {});

    return ResponseUtil.setPaginationLinks(responseBuilder, uriInfo, appointmentsPage).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createAppointment(@Valid @BeanParam AppointmentForm appointmentForm)
      throws DoctorNotAvailableException, DoctorNotFoundException, PatientNotFoundException {

    LOGGER.debug("Creating appointment: {}", appointmentForm);

    final Appointment appointment =
        appointmentService.createAppointment(
            PawAuthUserDetails.getCurrentUserId(),
            appointmentForm.getDocId(),
            appointmentForm.getDate(),
            appointmentForm.getBlockEnum(),
            appointmentForm.getDescription());

    LOGGER.debug("Appointment created with id {}", appointment.getId());

    URI createdAppointmentUri =
        uriInfo
            .getBaseUriBuilder()
            .path("/appointments")
            .path(String.valueOf(appointment.getId()))
            .build();

    return Response.created(createdAppointmentUri).build();
  }

  // ================= appointments/{id} ========================

  @GET
  @Produces(VndType.APPLICATION_APPOINTMENT)
  @Path("/{appointmentId:\\d+}")
  @PreAuthorize("@authorizationFunctions.isInvolvedInAppointment(authentication, #appointmentId)")
  public Response getAppointment(@PathParam("appointmentId") final long appointmentId)
      throws AppointmentNotFoundException {

    LOGGER.debug("Getting appointment with id {}", appointmentId);

    Appointment appointment =
        appointmentService
            .getAppointmentById(appointmentId)
            .orElseThrow(AppointmentNotFoundException::new);

    LOGGER.debug("returning appointment with id {}", appointmentId);

    return Response.ok(AppointmentDto.fromAppointment(uriInfo, appointment)).build();
  }

  @PATCH
  @Path("/{appointmentId:\\d+}")
  @Consumes(VndType.APPLICATION_APPOINTMENT_CANCEL)
  @Produces(VndType.APPLICATION_APPOINTMENT)
  @PreAuthorize("@authorizationFunctions.isInvolvedInAppointment(authentication, #appointmentId)")
  public Response cancelAppointment(
      @PathParam("appointmentId") final long appointmentId,
      @Valid final CancelAppointmentForm cancelAppointmentForm)
      throws AppointmentNotFoundException, AppointmentInmutableException {

    LOGGER.debug("Cancelling appointment with id {}", appointmentId);

    Appointment appointment =
        appointmentService
            .getAppointmentById(appointmentId)
            .orElseThrow(AppointmentNotFoundException::new);

    long userId = PawAuthUserDetails.getCurrentUserId();

    try {
      appointmentService.cancelAppointment(
          appointmentId, cancelAppointmentForm.getDescription(), userId);
    } catch (CancelForbiddenException e) {
      LOGGER.error(
          "User with id {} should not have been authorized to request cancellation", userId);
      throw new IllegalStateException();
    }

    LOGGER.debug("Appointment with id {} cancelled", appointmentId);

    return Response.ok(AppointmentDto.fromAppointment(uriInfo, appointment)).build();
  }
}
