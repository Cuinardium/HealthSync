package ar.edu.itba.paw.webapp.mapper;

import ar.edu.itba.paw.interfaces.services.exceptions.IndicationForbiddenException;
import ar.edu.itba.paw.webapp.dto.ErrorDto;
import ar.edu.itba.paw.webapp.mediaType.VndType;
import ar.edu.itba.paw.webapp.utils.LocaleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Locale;

@Component
@Provider
public class IndicationForbiddenExceptionMapper implements ExceptionMapper<IndicationForbiddenException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndicationForbiddenExceptionMapper.class);

    private static final String MESSAGE_ID = "error.indicationForbidden";
    private static final Response.Status Status = Response.Status.FORBIDDEN;

    @Autowired private MessageSource messageSource;

    @Override
    public Response toResponse(IndicationForbiddenException e) {

        LOGGER.debug("Indication forbidden", e);

        Locale locale = LocaleUtil.getCurrentRequestLocale();
        String message = messageSource.getMessage(MESSAGE_ID, null, locale);

        return Response.status(Status)
            .type(VndType.APPLICATION_ERROR)
            .entity(ErrorDto.fromMessage(message))
            .build();
    }
}
