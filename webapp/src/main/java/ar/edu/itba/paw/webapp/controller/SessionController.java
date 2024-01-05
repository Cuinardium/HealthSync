package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.JwtUtil;
import ar.edu.itba.paw.webapp.form.AuthForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")//TODO see auth path
@Component
public class SessionController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Valid @BeanParam AuthForm authForm){ //TODO check if I can use the same login form
        Authentication auth= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authForm.getEmail(), authForm.getPassword()));
        User user= (User) auth.getPrincipal();
        String authToken= jwtUtil.generateAccessToken(user);
        return Response.ok( )//TODO tema response con el token
    }
}
