package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.TokenService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.exceptions.TokenNotFoundException;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;
import ar.edu.itba.paw.webapp.auth.utils.AuthUtils;
import ar.edu.itba.paw.webapp.exceptions.AuthenticationErrorException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
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
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class BasicAuthFilter extends OncePerRequestFilter {
  @Autowired private JwtUtil jwtUtil;

  @Autowired private AuthenticationEntryPoint authenticationEntryPoint;

  @Autowired private UserService userService;

  @Autowired private TokenService tokenService;

  @Autowired private PawUserDetailsService userDetailsService;

  @Autowired private AuthenticationManager authenticationManager;

  private static final int EMAIL_INDEX = 0;
  private static final int PASSWORD_INDEX = 1;
  private static final int VERIFICATION_TOKEN_INDEX = 1;

  private static final String TOKEN_HEADER = "X-JWT";
  private static final String REFRESH_TOKEN_HEADER = "X-Refresh";
  private static final String BASIC_PREFIX = "Basic ";

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
      Optional<VerificationToken> token = tokenService.getUserToken(user);
      if (token.isPresent()
          && token.get().getToken().equals(credentials[VERIFICATION_TOKEN_INDEX])) {
        if (token.get().isExpired()) throw new NonceExpiredException("Token expired");

        response.addHeader(
            TOKEN_HEADER, jwtUtil.generateAccessToken(user, AuthUtils.getBaseUrl(request)));
        response.addHeader(REFRESH_TOKEN_HEADER, jwtUtil.generateRefreshToken(user));
        authentication = getAuthWithVerifToken(user);
      } else {
        authentication =
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    credentials[EMAIL_INDEX], credentials[PASSWORD_INDEX]));

        response.addHeader(
            TOKEN_HEADER, jwtUtil.generateAccessToken(user, AuthUtils.getBaseUrl(request)));
        response.addHeader(REFRESH_TOKEN_HEADER, jwtUtil.generateRefreshToken(user));
      }
    } catch (AuthenticationException e) {
      SecurityContextHolder.clearContext();
      authenticationEntryPoint.commence(request, response, e);
      return;
    }

    SecurityContextHolder.getContext().setAuthentication(authentication);
    filterChain.doFilter(request, response);
  }

  private Authentication getAuthWithVerifToken(User user) throws DisabledException {
    try {
      tokenService.deleteUserToken(user);
    } catch (TokenNotFoundException e) {
      throw new DisabledException("Token not found");
    }

    PawAuthUserDetails userDetails =
        (PawAuthUserDetails) userDetailsService.loadUserByUsername(user.getEmail());
    return new UsernamePasswordAuthenticationToken(
        userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
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
