package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.webapp.exceptions.ImageNotFoundException;
import ar.edu.itba.paw.webapp.utils.ResponseUtil;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("images")
@Component
public class ImageController {
  private static final Logger LOGGER = LoggerFactory.getLogger(ImageController.class);

  private final ImageService imageService;

  @Context private UriInfo uriInfo;

  @Autowired
  public ImageController(final ImageService imageService) {
    this.imageService = imageService;
  }

  // ====== images ======

  @GET
  @Path("/{id:\\d+}")
  public Response getImage(@PathParam("id") final int id) {
    final Image image = imageService.getImage(id).orElseThrow(ImageNotFoundException::new);
    Response.ResponseBuilder responseBuilder = ResponseUtil.setImmutable(Response.ok(image.getBytes(), image.getMediaType()));
    LOGGER.debug("Getting image with id {}", image.getImageId());
    return responseBuilder.build();
  }
}
