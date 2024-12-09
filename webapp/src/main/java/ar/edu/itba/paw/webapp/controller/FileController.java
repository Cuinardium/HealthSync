package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.FileService;
import ar.edu.itba.paw.models.File;
import ar.edu.itba.paw.webapp.exceptions.FileNotFoundException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import ar.edu.itba.paw.webapp.utils.FileUtil;
import ar.edu.itba.paw.webapp.utils.ResponseUtil;

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

  // ================= files/{fileId} =================

  @GET
  @Path("/{fileId:\\d+}")
  @Produces({"image/jpeg", "image/png", "application/pdf", "application/octet-stream"})
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

    String contentType = getFileContentType(file.getName());

    return ResponseUtil.setImmutable(Response.ok(file.getBytes(), contentType))
        .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
        .build();
  }

  private String getFileContentType(String fileName) {
    // jpeg, jpg, png, pdf
    String extension = FileUtil.getFileExtension(fileName);

    switch (extension) {
      case "jpeg":
      case "jpg":
        return "image/jpeg";
      case "png":
        return "image/png";
      case "pdf":
        return "application/pdf";
      default:
        return "application/octet-stream";
    }
  }
}
