package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.annotations.ValidateImage;
import ar.edu.itba.paw.webapp.utils.FileUtil;
import java.util.Arrays;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

public class ImageValidator
    implements ConstraintValidator<ValidateImage, FormDataContentDisposition> {

  private static final List<String> SUPPORTED_TYPES = Arrays.asList("png", "jpg", "jpeg");

  @Override
  public boolean isValid(
      FormDataContentDisposition imageDetails,
      ConstraintValidatorContext constraintValidatorContext) {
    if (imageDetails == null) {
      return true;
    }

    String extension = FileUtil.getFileExtension(imageDetails.getFileName());
    return extension != null && SUPPORTED_TYPES.contains(extension);
  }
}
