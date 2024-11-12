package ar.edu.itba.paw.webapp.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

public class StaticCacheFilter extends OncePerRequestFilter {
  private static final int STATIC_MAX_AGE = 31536000;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (HttpMethod.GET.matches(request.getMethod())) {
      response.setHeader(
          HttpHeaders.CACHE_CONTROL,
          String.format("public, max-age=%d, immutable", STATIC_MAX_AGE));
    }

    filterChain.doFilter(request, response);
  }
}
