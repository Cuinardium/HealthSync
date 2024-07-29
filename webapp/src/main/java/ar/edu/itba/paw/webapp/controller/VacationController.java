package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.VacationInvalidException;
import ar.edu.itba.paw.interfaces.services.exceptions.VacationNotFoundException;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Vacation;
import ar.edu.itba.paw.webapp.dto.VacationDto;
import ar.edu.itba.paw.webapp.form.DoctorVacationForm;
import ar.edu.itba.paw.webapp.mediaType.VndType;
import ar.edu.itba.paw.webapp.query.PageQuery;
import ar.edu.itba.paw.webapp.utils.ResponseUtil;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Path("/doctors/{doctorId:\\d+}/vacations")
@Component
public class VacationController {

  private static final Logger LOGGER = LoggerFactory.getLogger(VacationController.class);

  private final DoctorService doctorService;
  private final AppointmentService appointmentService;

  @Context private UriInfo uriInfo;

  @Autowired
  public VacationController(
      final DoctorService doctorService, final AppointmentService appointmentService) {
    this.doctorService = doctorService;
    this.appointmentService = appointmentService;
  }

  // =============== vacations ==============

  @GET
  @Produces(VndType.APPLICATION_VACATION_LIST)
  @PreAuthorize("@authorizationFunctions.isUser(authentication, #doctorId)")
  public Response listVacations(
      @PathParam("doctorId") final Long doctorId, @BeanParam final PageQuery pageQuery)
      throws DoctorNotFoundException {

    LOGGER.debug("Listing vacations for doctor: {}", doctorId);

    final Doctor doctor =
        doctorService.getDoctorById(doctorId).orElseThrow(DoctorNotFoundException::new);

    Collection<Vacation> vacations = doctor.getVacations();

    if (vacations.isEmpty()) {
      LOGGER.debug("No vacations found for doctor: {}", doctorId);
      return Response.noContent().build();
    }

    List<VacationDto> vacationDtos =
        vacations.stream()
            .sorted()
            .map(vacation -> VacationDto.fromVacation(uriInfo, vacation))
            .collect(Collectors.toList());

    Page<VacationDto> vacationPage =
        new Page<>(vacationDtos, pageQuery.getPage(), pageQuery.getPageSize());

    if (vacationPage.getContent().isEmpty()) {
      return Response.noContent().build();
    }

    return ResponseUtil.setPaginationLinks(
            Response.ok(new GenericEntity<List<VacationDto>>(vacationPage.getContent()) {}),
            uriInfo,
            vacationPage)
        .build();
  }

  @POST
  @Consumes(VndType.APPLICATION_VACATION_FORM)
  @PreAuthorize("@authorizationFunctions.isUser(authentication, #doctorId)")
  public Response createVacation(
      @PathParam("doctorId") final Long doctorId,
      @Valid final DoctorVacationForm doctorVacationForm)
      throws DoctorNotFoundException, VacationInvalidException {

    LOGGER.debug("Creating vacation for doctor: {}", doctorId);

    Vacation vacation =
        new Vacation.Builder(
                doctorVacationForm.getFromDate(),
                doctorVacationForm.getFromTimeEnum(),
                doctorVacationForm.getToDate(),
                doctorVacationForm.getToTimeEnum())
            .build();

    vacation = doctorService.addVacation(doctorId, vacation);

    final boolean cancelAppointments = doctorVacationForm.getCancelAppointments();
    String cancelReason = doctorVacationForm.getCancelReason();

    if (cancelAppointments) {
      LOGGER.debug(
          "Canceling appointments in range, vacation: {}, reason: {}", vacation, cancelReason);

      appointmentService.cancelAppointmentsInRange(
          doctorId,
          vacation.getFromDate(),
          vacation.getFromTime(),
          vacation.getToDate(),
          vacation.getToTime(),
          cancelReason);
    }

    final URI createdVacationUri =
        uriInfo
            .getBaseUriBuilder()
            .path("doctors")
            .path(doctorId.toString())
            .path("vacations")
            .path(vacation.getId().toString())
            .build();

    return Response.created(createdVacationUri).build();
  }

  // ============== vacations/{vacationId} =============

  @GET
  @Path("/{vacationId:\\d+}")
  @Produces(VndType.APPLICATION_VACATION)
  @PreAuthorize("@authorizationFunctions.isUser(authentication, #doctorId)")
  public Response getVacation(
      @PathParam("doctorId") final Long doctorId, @PathParam("vacationId") final Long vacationId)
      throws DoctorNotFoundException, VacationNotFoundException {

    LOGGER.debug("Getting vacation: {}", vacationId);

    final Doctor doctor =
        doctorService.getDoctorById(doctorId).orElseThrow(DoctorNotFoundException::new);

    final Vacation vacation =
        doctor.getVacations().stream()
            .filter(v -> v.getId().equals(vacationId))
            .findAny()
            .orElseThrow(VacationNotFoundException::new);

    VacationDto vacationDto = VacationDto.fromVacation(uriInfo, vacation);
    return Response.ok(vacationDto).build();
  }

  @DELETE
  @Path("/{vacationId:\\d+}")
  @PreAuthorize("@authorizationFunctions.isUser(authentication, #doctorId)")
  public Response deleteVacation(
      @PathParam("doctorId") final Long doctorId, @PathParam("vacationId") final Long vacationId) {

    LOGGER.debug("Deleting vacation: {}", vacationId);

    try {
      final Doctor doctor =
          doctorService.getDoctorById(doctorId).orElseThrow(DoctorNotFoundException::new);

      final Vacation vacation =
          doctor.getVacations().stream()
              .filter(v -> v.getId().equals(vacationId))
              .findAny()
              .orElseThrow(VacationNotFoundException::new);

      doctorService.removeVacation(doctorId, vacation);
    } catch (DoctorNotFoundException e) {
      LOGGER.debug("Doctor not found: {}", doctorId);
    } catch (VacationNotFoundException e) {
      LOGGER.debug("Vacation not found, vacation: {}", vacationId);
    }

    return Response.noContent().build();
  }
}
