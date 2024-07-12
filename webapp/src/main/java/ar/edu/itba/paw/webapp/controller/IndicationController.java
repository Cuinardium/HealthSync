package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.IndicationService;
import ar.edu.itba.paw.interfaces.services.exceptions.AppointmentNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.File;
import ar.edu.itba.paw.models.Indication;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import ar.edu.itba.paw.webapp.dto.IndicationDto;
import ar.edu.itba.paw.webapp.exceptions.IndicationNotFoundException;
import ar.edu.itba.paw.webapp.mediaType.VndType;
import ar.edu.itba.paw.webapp.utils.ResponseUtil;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Path("/appointments/{appointmentId:\\d+}/indications")
@Component
public class IndicationController {
  private static final Logger LOGGER = LoggerFactory.getLogger(IndicationController.class);
  private static final int DEFAULT_PAGE_SIZE = 10;
  private final IndicationService indicationService;
  @Context private UriInfo uriInfo;

  @Autowired
  public IndicationController(final IndicationService indicationService) {
    this.indicationService = indicationService;
  }

  // ================= indications =================

  @GET
  @Produces(VndType.APPLICATION_INDICATION_LIST)
  @PreAuthorize("@authorizationFunctions.isInvolvedInAppointment(authentication, #appointmentId)")
  public Response listIndications(
      @PathParam("appointmentId") final Long appointmentId,
      @QueryParam("page") @DefaultValue("1") final int page)
      throws AppointmentNotFoundException {

    LOGGER.debug(
        "Listing indications for appointment: \nAppointmentId: {}\nPage: {}", appointmentId, page);

    if (page < 1) {
      LOGGER.debug("Invalid page number: {}", page);
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("Invalid page number. Must be greater than 0.")
          .build();
    }

    Page<Indication> indications =
        indicationService.getIndicationsForAppointment(appointmentId, page - 1, DEFAULT_PAGE_SIZE);

    if (indications.getContent().isEmpty()) {
      LOGGER.debug("No indications found for appointment: {}", appointmentId);
      return Response.noContent().build();
    }

    final List<IndicationDto> indicationDtoList =
        indications.getContent().stream()
            .map(i -> IndicationDto.fromIndication(uriInfo, i))
            .collect(Collectors.toList());

    return ResponseUtil.setPaginationLinks(
            Response.ok(new GenericEntity<List<IndicationDto>>(indicationDtoList) {}),
            uriInfo,
            indications)
        .build();
  }

  // TODO: Probablemente mal, subir archivo a otro endpoint?
  @POST
  @Consumes({MediaType.MULTIPART_FORM_DATA, VndType.APPLICATION_INDICATION})
  @PreAuthorize("@authorizationFunctions.isInvolvedInAppointment(authentication, #appointmentId)")
  public Response createIndication(
      @PathParam("appointmentId") final Long appointmentId,
      @FormDataParam("indications") final String indications,
      @FormDataParam("file") final byte[] fileBytes)
      throws AppointmentNotFoundException, UserNotFoundException {

    final long userId = PawAuthUserDetails.getCurrentUserId();

    File file = null;
    if (fileBytes != null && fileBytes.length > 0) {
      file = new File(fileBytes);
    }

    Indication indication =
        indicationService.createIndication(appointmentId, userId, indications, file);

    URI createdIndicationUri =
        uriInfo
            .getBaseUriBuilder()
            .path("appointments")
            .path(String.valueOf(appointmentId))
            .path("indications")
            .path(String.valueOf(indication.getId()))
            .build();

    return Response.created(createdIndicationUri).build();
  }

  // ================ indications/{indicationId} ================

  @GET
  @Path("/{indicationId:\\d+}")
  @Produces(VndType.APPLICATION_INDICATION)
  @PreAuthorize("@authorizationFunctions.isInvolvedInAppointment(authentication, #appointmentId)")
  public Response getIndication(
      @PathParam("appointmentId") final Long appointmentId,
      @PathParam("indicationId") final Long indicationId)
      throws IndicationNotFoundException {

    LOGGER.debug("Getting indication: {}", indicationId);

    final Indication indication =
        indicationService.getIndication(indicationId).orElseThrow(IndicationNotFoundException::new);

    if (!indication.getAppointment().getId().equals(appointmentId)) {
      throw new IndicationNotFoundException();
    }

    final IndicationDto indicationDto = IndicationDto.fromIndication(uriInfo, indication);

    return Response.ok(indicationDto).build();
  }
}
