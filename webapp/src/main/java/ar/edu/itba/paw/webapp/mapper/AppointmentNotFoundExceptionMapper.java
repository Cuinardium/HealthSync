package ar.edu.itba.paw.webapp.mapper;

import ar.edu.itba.paw.webapp.exceptions.AppointmentNotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AppointmentNotFoundExceptionMapper
    implements ExceptionMapper<AppointmentNotFoundException> {
  @Override
  public Response toResponse(AppointmentNotFoundException exception) {
    return Response.status(Response.Status.NOT_FOUND).build();
  }
}
