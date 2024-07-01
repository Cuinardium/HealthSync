package ar.edu.itba.paw.webapp.auth.handlers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class ForbiddenHandler implements AccessDeniedHandler {
  @Override
  public void handle(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      AccessDeniedException e)
      throws IOException, ServletException {

    httpServletResponse.setStatus(Response.Status.FORBIDDEN.getStatusCode());
    httpServletResponse.setContentType(MediaType.APPLICATION_JSON);
    httpServletResponse.setHeader("X-Jwt", null);
    httpServletResponse.setHeader("X-Refresh", null);
    httpServletResponse.getWriter().write(String.format("{\"error\": \"forbidden: %s\"}", e.getMessage()));
  }
}
