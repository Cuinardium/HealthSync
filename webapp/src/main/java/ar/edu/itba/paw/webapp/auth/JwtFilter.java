package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.utils.AuthUtils;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/** https://www.toptal.com/spring/spring-security-tutorial */
@Component
public class JwtFilter extends OncePerRequestFilter {
  private static final String TOKEN_PREFIX = "Bearer ";
  private static final String TOKEN_HEADER = "X-JWT";

  @Autowired private JwtUtil jwtUtil;

  @Autowired private UserService userService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (header == null || !header.startsWith(TOKEN_PREFIX)) {
      filterChain.doFilter(request, response);
      return;
    }

    final String token;
    try {
      token = header.split(" ")[1].trim();
      if (!jwtUtil.validateAccessToken(token)) {
        filterChain.doFilter(request, response);
        return;
      }
    } catch (Exception e) {
      filterChain.doFilter(request, response);
      return;
    }

    UserDetails userDetails = jwtUtil.parseToken(token);

    if (userDetails == null
        || !userDetails.isEnabled()
        || !userDetails.isAccountNonLocked()
        || SecurityContextHolder.getContext().getAuthentication() != null) {
      filterChain.doFilter(request, response);
      return;
    }

    if (jwtUtil.isRefreshToken(token)) {
      User user = userService.getUserByEmail(userDetails.getUsername()).orElse(null);

      if (user == null) {
        filterChain.doFilter(request, response);
        return;
      }

      String baseURL = AuthUtils.getBaseUrl(request);

      response.setHeader(TOKEN_HEADER, jwtUtil.generateAccessToken(user, baseURL));
    }

    final UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(
            userDetails, userDetails.getPassword(), userDetails.getAuthorities());

    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    filterChain.doFilter(request, response);
  }
}
