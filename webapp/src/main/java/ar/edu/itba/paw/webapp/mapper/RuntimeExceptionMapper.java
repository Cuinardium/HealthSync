package ar.edu.itba.paw.webapp.mapper;

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
import javax.xml.bind.UnmarshalException;
import java.util.Locale;

@Component
@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeExceptionMapper.class);

    private static final String MESSAGE_ID = "error.illegalState";
    private static final Response.Status Status = Response.Status.INTERNAL_SERVER_ERROR;

    @Autowired
    private MessageSource messageSource;

    @Override
    public Response toResponse(RuntimeException e) {

        LOGGER.error("Unexpected runtime exception", e);

        Response.Status status = e.getCause() instanceof UnmarshalException ? Response.Status.BAD_REQUEST : Status;
        String messageId = e.getCause() instanceof UnmarshalException ? "error.badJson" : MESSAGE_ID;
        String message = messageSource.getMessage(messageId, null, LocaleUtil.getCurrentRequestLocale());


        return Response.status(status)
                .type(VndType.APPLICATION_ERROR)
                .entity(ErrorDto.fromMessage(message))
                .build();
    }
}
