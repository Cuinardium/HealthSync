package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter  extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.resolveToken(request);
        if (accessToken == null ) {
            filterChain.doFilter(request, response);
            return;
        }

        Claims claims = jwtUtil.resolveClaims(request);

        if(claims != null && jwtUtil.validateClaims(claims)){
            String email = claims.getSubject();
            User user= userService.getUserByEmail(email).orElse(null);
            if(jwtUtil.isTokenRefresh(accessToken) && user!=null){
                response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtUtil.generateAccessToken(user));
            } else if (user!=null) {
                response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
            }
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user,"",new ArrayList<>());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

}
