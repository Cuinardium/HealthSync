package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.annotations.ValidateImage;
import ar.edu.itba.paw.webapp.utils.FileUtil;
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

  public String getImageMediaType() {
    // jpeg, jpg, png
    String extension = FileUtil.getFileExtension(imageDetails.getFileName());

    switch (extension) {
      case "png":
        return "image/png";
      case "jpeg":
      case "jpg":
      default:
        return "image/jpeg";
    }
  }

  public boolean hasFile() {
    return imageDetails != null && imageData != null && imageData.length > 0;
  }

  public void setImageDetails(FormDataContentDisposition imageDetails) {
    this.imageDetails = imageDetails;
  }
}
