package ar.edu.itba.paw.webapp.auth.handlers;

import ar.edu.itba.paw.webapp.mediaType.VndType;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class ForbiddenHandler implements AccessDeniedHandler {

  @Autowired
  MessageSource messageSource;

  @Override
  public void handle(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      AccessDeniedException e)
      throws IOException, ServletException {

    httpServletResponse.setStatus(Response.Status.FORBIDDEN.getStatusCode());
    httpServletResponse.setContentType(VndType.APPLICATION_ERROR);
    httpServletResponse.setHeader("X-Jwt", null);
    httpServletResponse.setHeader("X-Refresh", null);

    // Cors
    httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
    httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");

    httpServletResponse.getWriter().write("{\"message\": \"forbidden, access denied\"}");
  }
}
