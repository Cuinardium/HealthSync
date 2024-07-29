package ar.edu.itba.paw.webapp.mapper;

import ar.edu.itba.paw.interfaces.services.exceptions.PatientNotFoundException;
import ar.edu.itba.paw.webapp.dto.ErrorDto;
import ar.edu.itba.paw.webapp.mediaType.VndType;
import ar.edu.itba.paw.webapp.utils.LocaleUtil;
import java.util.Locale;
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
public class PatientNotFoundExceptionMapper implements ExceptionMapper<PatientNotFoundException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientNotFoundExceptionMapper.class);

    private static final String MESSAGE_ID = "error.patientNotFound";
    private static final Response.Status Status = Response.Status.NOT_FOUND;

    @Autowired private MessageSource messageSource;

    @Override
    public Response toResponse(PatientNotFoundException e) {

        LOGGER.debug("Patient not found", e);

        Locale locale = LocaleUtil.getCurrentRequestLocale();
        String message = messageSource.getMessage(MESSAGE_ID, null, locale);

        return Response.status(Status)
                .type(VndType.APPLICATION_ERROR)
                .entity(ErrorDto.fromMessage(message))
                .build();
    }
}
