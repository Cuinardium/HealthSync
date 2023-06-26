package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.annotations.ValidateImage;
import org.springframework.web.multipart.MultipartFile;

public class ImageForm {

  @ValidateImage private MultipartFile image ;

  public MultipartFile getImage() {
    return image;
  }

  public void setImage(MultipartFile image) {
    this.image = image;
  }
}
