package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.annotations.ValidateImage;
import ar.edu.itba.paw.webapp.utils.FileUtil;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class ImageValidator implements ConstraintValidator<ValidateImage, MultipartFile> {

  private final Integer MAX_IMG_SIZE = 1024 * 1024 * 8;

  @Override
  public boolean isValid(
      MultipartFile image, ConstraintValidatorContext constraintValidatorContext) {
    if (image == null || image.isEmpty()) {
      return true;
    }
    if (image.getSize() > MAX_IMG_SIZE) {
      return false;
    }
    String extension = FileUtil.getFileExtension(image.getOriginalFilename());
    return isValidExtension(extension);
  }

  private boolean isValidExtension(String extension) {
    return extension != null
        && (extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg"));
  }
}
