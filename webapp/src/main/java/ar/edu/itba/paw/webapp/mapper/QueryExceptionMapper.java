package ar.edu.itba.paw.webapp.mapper;

import ar.edu.itba.paw.webapp.dto.ErrorDto;
import ar.edu.itba.paw.webapp.mediaType.VndType;
import ar.edu.itba.paw.webapp.utils.LocaleUtil;
import org.eclipse.persistence.exceptions.QueryException;
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
public class QueryExceptionMapper implements ExceptionMapper<QueryException> {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryExceptionMapper.class);

    private static final String MESSAGE_ID = "error.badJson";
    private static final Response.Status Status = Response.Status.BAD_REQUEST;

    @Autowired
    private MessageSource messageSource;

    @Override
    public Response toResponse(QueryException e) {

        LOGGER.error("Exception while parsing request body", e);

        Locale locale = LocaleUtil.getCurrentRequestLocale();
        String message = messageSource.getMessage(MESSAGE_ID, null, locale);

        return Response.status(Status)
                .type(VndType.APPLICATION_ERROR)
                .entity(ErrorDto.fromMessage(message))
                .build();
    }
}
