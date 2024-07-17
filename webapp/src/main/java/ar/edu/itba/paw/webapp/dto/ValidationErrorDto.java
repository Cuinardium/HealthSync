package ar.edu.itba.paw.webapp.dto;

import java.util.Locale;
import javax.validation.ConstraintViolation;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
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
    String[] propertyPath = violation.getPropertyPath().toString().split("\\.");

    // Solo especifico el path si el error es de un field
    // Si es del bean entero no
    if (propertyPath.length > 2) {
      dto.attribute = propertyPath[propertyPath.length - 1];
    }

    dto.invalidValue = getInvalidValueString(violation.getInvalidValue());

    return dto;
  }

  private static String getInvalidValueString(Object invalidValue) {

    if (invalidValue == null) {
      return null;
    }

    // Edge cases, esto es un code smell
    // Pero no se me ocurre una forma más elegante de hacerlo

    // Si es un array, devolver null para no mostrar referencias de memoria
    if (invalidValue.getClass().isArray()) {
      return null;
    }

    // Si es un FormDataContentDisposition, devolver el nombre del archivo
    if (invalidValue instanceof FormDataContentDisposition) {
      return ((FormDataContentDisposition) invalidValue).getFileName();
    }

    // Si es otro objeto, devolver su representación en string
    return invalidValue.toString();
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
