package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter  extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.resolveToken(request);
        if (accessToken == null ) {
            filterChain.doFilter(request, response);
            return;
        }

        Claims claims = jwtUtil.resolveClaims(request);

        if(claims != null & jwtUtil.validateClaims(claims)){
            String email = claims.getSubject();
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(email,"",new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

}
