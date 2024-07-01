package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationFunctions {


    @Autowired
    private ReviewService reviewService;



    public boolean canReview(Authentication authentication, long doctorId) {

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null || doctorId < 0) {
            return false;
        }

        PawAuthUserDetails user = (PawAuthUserDetails) authentication.getPrincipal();


        return reviewService.canReview(user.getId(), doctorId);
    }
}
