package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.JwtUtil;
import ar.edu.itba.paw.webapp.auth.PawUserDetailsService;
import ar.edu.itba.paw.webapp.form.AuthForm;
import ar.edu.itba.paw.webapp.responses.AuthErrorResponse;
import ar.edu.itba.paw.webapp.responses.AuthRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/login")//TODO see auth path
@Component
public class SessionController {
    private final UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    public SessionController(final UserService userService) {
        this.userService = userService;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Valid @RequestBody AuthForm authForm) { //TODO check if I can use the same login form
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authForm.getEmail(), authForm.getPassword()));
            String name = auth.getName();
            //TODO: should throw exception? bc user has been already authenticated
            User user = userService.getUserByEmail(name).get();
            String authToken = jwtUtil.generateAccessToken(user);
            return Response.ok(new AuthRes(authToken, authForm.getEmail())).build();//TODO view if AuthRes needs more data
        } catch (BadCredentialsException e) {
            //TODO: check if string needs to be translated
            AuthErrorResponse authErrorResponse = new AuthErrorResponse(HttpStatus.BAD_REQUEST, "Invalid username or password");
            return Response.status(Response.Status.BAD_REQUEST).entity(authErrorResponse).build();
        } catch (Exception e) {
            AuthErrorResponse authErrorResponse = new AuthErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(authErrorResponse).build();
        }
    }
}
