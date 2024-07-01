package ar.edu.itba.paw.webapp.controller.spring;

import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.webapp.exceptions.ImageNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ImageController {

  private ImageService imageService;
  private static final Logger LOGGER = LoggerFactory.getLogger(ImageController.class);

  @Autowired
  public ImageController(ImageService imageService) {
    this.imageService = imageService;
  }

  @RequestMapping(
      value = "/img/{id:\\d+}",
      method = RequestMethod.GET,
      produces = MediaType.IMAGE_JPEG_VALUE)
  @ResponseBody
  public ResponseEntity<byte[]> getImage(@PathVariable("id") final long id) {
    try {
      Image image = imageService.getImage(id).orElseThrow(ImageNotFoundException::new);
      LOGGER.info("Returning Image with id: {}", id);
      return ResponseEntity.ok()
          .contentLength(image.getBytes().length)
          .contentType(MediaType.IMAGE_JPEG)
          .body(image.getBytes());
    } catch (ImageNotFoundException e) {
      LOGGER.error("Image with id: {} not found!", id);
      throw e; // TODO: return empty image
    }
  }
}
