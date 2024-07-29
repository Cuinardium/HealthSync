package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.annotations.ValidateImage;
import javax.validation.constraints.Size;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

public class ImageForm {

  private static final int MAX_IMAGE_SIZE = 1024 * 1024 * 8; // 8MB

  @ValidateImage(message = "ValidateImage.imageForm.image")
  @FormDataParam("image")
  private FormDataContentDisposition imageDetails;

  @Size(min = 1, max = MAX_IMAGE_SIZE, message = "Size.imageForm.image")
  @FormDataParam("image")
  private byte[] imageData;

  public byte[] getImageData() {
    return imageData;
  }

  public void setImageData(byte[] imageData) {
    this.imageData = imageData;
  }

  public FormDataContentDisposition getImageDetails() {
    return imageDetails;
  }

  public boolean hasFile() {
    return imageDetails != null && imageData != null && imageData.length > 0;
  }

  public void setImageDetails(FormDataContentDisposition imageDetails) {
    this.imageDetails = imageDetails;
  }
}
