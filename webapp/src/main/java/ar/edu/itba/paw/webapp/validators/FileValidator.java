package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.annotations.ValidateFile;
import ar.edu.itba.paw.webapp.utils.FileUtil;
import java.util.Arrays;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

public class FileValidator
    implements ConstraintValidator<ValidateFile, FormDataContentDisposition> {

  private static final List<String> SUPPORTED_TYPES = Arrays.asList("png", "jpg", "jpeg", "pdf");

  @Override
  public boolean isValid(
      FormDataContentDisposition fileDetails,
      ConstraintValidatorContext constraintValidatorContext) {

    if (fileDetails == null) {
      return true;
    }

    String extension = FileUtil.getFileExtension(fileDetails.getFileName());

    return extension != null && SUPPORTED_TYPES.contains(extension);
  }
}
