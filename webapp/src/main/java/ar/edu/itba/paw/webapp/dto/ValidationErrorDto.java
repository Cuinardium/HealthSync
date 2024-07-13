package ar.edu.itba.paw.webapp.dto;

import java.util.Locale;
import javax.validation.ConstraintViolation;
import org.springframework.context.MessageSource;

public class ValidationErrorDto {

  private String message;
  private String attribute;
  private String invalidValue;

  public static ValidationErrorDto fromViolation(
      ConstraintViolation<?> violation, MessageSource messageSource, Locale locale) {

    ValidationErrorDto dto = new ValidationErrorDto();
    dto.message = messageSource.getMessage(violation.getMessage(), null, locale);

    // Obtener el último segmento del propertyPath
    String propertyPath = violation.getPropertyPath().toString();

    dto.attribute =
        propertyPath.contains(".")
            ? propertyPath.substring(propertyPath.lastIndexOf(".") + 1)
            : propertyPath;

    Object invalidValue = violation.getInvalidValue();
    dto.invalidValue = invalidValue == null ? null : invalidValue.toString();

    return dto;
  }

  public String getAttribute() {
    return attribute;
  }

  public void setAttribute(String attribute) {
    this.attribute = attribute;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getInvalidValue() {
    return invalidValue;
  }

  public void setInvalidValue(String invalidValue) {
    this.invalidValue = invalidValue;
  }
}
