package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.annotations.ValidateFile;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileValidator implements ConstraintValidator<ValidateFile, MultipartFile> {

    @Override
    public boolean isValid(
            MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        if (file == null || file.isEmpty()) {
            return true;
        }
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        return isValidExtension(extension);
    }

    private boolean isValidExtension(String extension) {
        return extension != null
                && (extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg") || extension.equals("pdf"));
    }
}
