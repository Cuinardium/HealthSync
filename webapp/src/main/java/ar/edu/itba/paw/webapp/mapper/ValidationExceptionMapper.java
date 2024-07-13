package ar.edu.itba.paw.webapp.mapper;

import ar.edu.itba.paw.webapp.dto.ValidationErrorDto;
import ar.edu.itba.paw.webapp.mediaType.VndType;
import ar.edu.itba.paw.webapp.utils.LocaleUtil;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ValidationExceptionMapper.class);

  private static final Response.Status STATUS = Response.Status.BAD_REQUEST;

  @Autowired private MessageSource messageSource;

  @Override
  public Response toResponse(ConstraintViolationException exception) {
    LOGGER.debug("Constraint violation exception", exception);

    Locale locale = LocaleUtil.getCurrentRequestLocale();

    List<ValidationErrorDto> violations =
        exception.getConstraintViolations().stream()
            .map(violation -> ValidationErrorDto.fromViolation(violation, messageSource, locale))
            .collect(Collectors.toList());

    return Response.status(STATUS)
        .type(VndType.APPLICATION_VALIDATION_ERROR_LIST)
        .entity(new GenericEntity<List<ValidationErrorDto>>(violations) {})
        .build();
  }
}
