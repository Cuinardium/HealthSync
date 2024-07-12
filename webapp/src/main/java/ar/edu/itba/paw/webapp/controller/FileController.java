package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.FileService;
import ar.edu.itba.paw.models.File;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import ar.edu.itba.paw.webapp.exceptions.FileNotFoundException;
import ar.edu.itba.paw.webapp.mediaType.VndType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Path("/appointments/{appointmentId:\\d+}/files")
@Component
public class FileController {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

  private final FileService fileService;

  @Autowired
  public FileController(final FileService fileService) {
    this.fileService = fileService;
  }

  // TODO: Post de archivos aca? validacion que no sean maliciosos?

  // ================= files/{fileId} =================

  // TODO: Servir multiples tipos de archivos
  @GET
  @Path("/{fileId:\\d+}")
  @Produces({"image/jpeg"})
  @PreAuthorize("@authorizationFunctions.isInvolvedInAppointment(authentication, #appointmentId)")
  public Response getFile(
      @PathParam("appointmentId") final Long appointmentId,
      @PathParam("fileId") final Long fileId) {
    LOGGER.debug("Getting file with id: {}", fileId);

    final File file = fileService.getFile(fileId).orElseThrow(FileNotFoundException::new);

    long fileAppointmentId = file.getIndication().getAppointment().getId();

    if (fileAppointmentId != appointmentId) {
      throw new FileNotFoundException();
    }

    final String filename = appointmentId + "-" + fileId + ".jpg";

    return Response.ok(file.getBytes(), "image/jpeg")
        .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
        .build();
  }
}
