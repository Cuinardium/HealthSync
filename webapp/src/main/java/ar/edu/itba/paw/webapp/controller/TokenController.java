package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.TokenService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.exceptions.TokenInvalidException;
import ar.edu.itba.paw.interfaces.services.exceptions.TokenNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.JwtUtil;
import ar.edu.itba.paw.webapp.form.EmailForm;
import ar.edu.itba.paw.webapp.mediaType.VndType;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("tokens")
@Component
public class TokenController {

  private static final Logger LOGGER = LoggerFactory.getLogger(TokenController.class);

  private final UserService userService;
  private final TokenService tokenService;

  private final JwtUtil jwtUtil;

  @Autowired
  public TokenController(
      final UserService userService, final TokenService tokenService, final JwtUtil jwtUtil) {
    this.userService = userService;
    this.tokenService = tokenService;
    this.jwtUtil = jwtUtil;
  }

  // ================== verification ===============

  @POST
  @Path("/verification")
  @Consumes(VndType.APPLICATION_EMAIL)
  public Response resendVerificationToken(@Valid final EmailForm emailForm)
      throws UserNotFoundException, TokenNotFoundException {

    LOGGER.debug("Resending verification token to user with email: {}", emailForm.getEmail());

    User user =
        userService.getUserByEmail(emailForm.getEmail()).orElseThrow(UserNotFoundException::new);

    tokenService.renewUserToken(user);

    return Response.accepted().build();
  }

  @PATCH
  @Path("/verification/{token}")
  @Consumes(VndType.APPLICATION_EMAIL)
  public Response verifyUser(
      @PathParam("token") final String token, @Valid final EmailForm emailForm)
      throws TokenNotFoundException, UserNotFoundException, TokenInvalidException {

    LOGGER.debug("Verifying user with email: {}", emailForm.getEmail());

    User user =
        userService.getUserByEmail(emailForm.getEmail()).orElseThrow(UserNotFoundException::new);
    userService.confirmUser(user.getId(), token);

    String accessToken = jwtUtil.generateAccessToken(user);
    String refreshToken = jwtUtil.generateRefreshToken(user);

    return Response.noContent()
        .header(JwtUtil.REFRESH_TOKEN_HEADER, refreshToken)
        .header(JwtUtil.TOKEN_HEADER, accessToken)
        .build();
  }
}
