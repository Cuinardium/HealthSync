package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.webapp.exceptions.ImageNotFoundException;
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

  @Autowired
  public ImageController(ImageService imageService) {
    this.imageService = imageService;
  }

  @RequestMapping(
    value = "/image/{id:\\d+}",
    method = RequestMethod.GET,
    produces = MediaType.IMAGE_JPEG_VALUE
  )
  @ResponseBody
  public ResponseEntity<byte[]> getImage(@PathVariable("id") final long id) {
    Image image = imageService.getImage(id).orElseThrow(ImageNotFoundException::new);
    return ResponseEntity.ok()
        .contentLength(image.getImage().length)
        .contentType(MediaType.IMAGE_JPEG)
        .body(image.getImage());
  }
}
