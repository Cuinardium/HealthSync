package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.TokenService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.exceptions.TokenNotFoundException;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.exceptions.AuthenticationErrorException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class BasicAuthFilter extends OncePerRequestFilter {
  private static final int EMAIL_INDEX = 0;
  private static final int PASSWORD_INDEX = 1;

  private static final String BASIC_PREFIX = "Basic ";
  @Autowired private JwtUtil jwtUtil;
  @Autowired private AuthenticationEntryPoint authenticationEntryPoint;
  @Autowired private UserService userService;
  @Autowired private TokenService tokenService;
  @Autowired private PawUserDetailsService userDetailsService;
  @Autowired private AuthenticationManager authenticationManager;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (header == null || !header.startsWith(BASIC_PREFIX)) {
      filterChain.doFilter(request, response);
      return;
    }

    Authentication authentication;
    try {
      String[] credentials = getDecodedHeader(header);

      User user =
          userService
              .getUserByEmail(credentials[EMAIL_INDEX])
              .orElseThrow(
                  () ->
                      new UsernameNotFoundException(
                          "No user for email " + credentials[EMAIL_INDEX]));

      authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  credentials[EMAIL_INDEX], credentials[PASSWORD_INDEX]));

      response.addHeader(JwtUtil.TOKEN_HEADER, jwtUtil.generateAccessToken(user));
      response.addHeader(JwtUtil.REFRESH_TOKEN_HEADER, jwtUtil.generateRefreshToken(user));

    } catch (AuthenticationException e) {
      SecurityContextHolder.clearContext();
      authenticationEntryPoint.commence(request, response, e);
      return;
    }

    SecurityContextHolder.getContext().setAuthentication(authentication);
    filterChain.doFilter(request, response);
  }

  private String[] getDecodedHeader(String header) {
    // Encoded token is in Base64
    byte[] encodedToken = header.split(" ")[1].trim().getBytes(StandardCharsets.UTF_8);
    byte[] decodedToken;
    try {
      decodedToken = Base64.getDecoder().decode(encodedToken);
    } catch (IllegalArgumentException e) {
      throw new AuthenticationErrorException("Could not decode the header");
    }

    String token = new String(decodedToken, StandardCharsets.UTF_8);
    int separator = token.indexOf(":");

    if (separator == -1) {
      throw new AuthenticationErrorException("Invalid basic token");
    }

    return new String[] {token.substring(0, separator), token.substring(separator + 1)};
  }
}
