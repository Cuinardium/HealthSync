package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.FileService;
import ar.edu.itba.paw.models.File;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

@Path("/appointments/{appointmentId:\\d+}/files")
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
  @Produces("image/jpeg")
  @PreAuthorize("@authorizationFunctions.isInvolvedInAppointment(authentication, #appointmentId)")
  public Response getFile(
      @PathParam("appointmentId") final Long appointmentId,
      @PathParam("fileId") final Long fileId) {
    LOGGER.debug("Getting file with id: {}", fileId);

    final File file = fileService.getFile(fileId).orElse(null);

    if (file == null) {
      LOGGER.debug("File not found: {}", fileId);
      return Response.status(Response.Status.NOT_FOUND).entity("File not found.").build();
    }

    final String filename = appointmentId + "-" + fileId + ".jpg";

    return Response.ok(file.getBytes(), "image/jpeg")
        .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
        .build();
  }
}
