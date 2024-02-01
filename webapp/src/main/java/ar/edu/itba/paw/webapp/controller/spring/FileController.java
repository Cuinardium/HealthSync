package ar.edu.itba.paw.webapp.controller.spring;

import ar.edu.itba.paw.interfaces.services.FileService;
import ar.edu.itba.paw.models.File;
import ar.edu.itba.paw.webapp.exceptions.FileNotFoundException;
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
public class FileController {

  private final FileService fileService;
  private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

  @Autowired
  public FileController(FileService fileService) {
    this.fileService = fileService;
  }

  @RequestMapping(
      value = "/file/{id:\\d+}",
      method = RequestMethod.GET,
      produces = MediaType.IMAGE_JPEG_VALUE)
  @ResponseBody
  public ResponseEntity<byte[]> getFile(@PathVariable("id") final long id) {
    try {
      File file = fileService.getFile(id).orElseThrow(FileNotFoundException::new);
      LOGGER.info("Returning file with id: {}", id);
      return ResponseEntity.ok()
          .contentLength(file.getBytes().length)
          .contentType(MediaType.IMAGE_JPEG)
          .body(file.getBytes());
    } catch (FileNotFoundException e) {
      LOGGER.error("File with id: {} not found!", id);
      throw e;
    }
  }
}
