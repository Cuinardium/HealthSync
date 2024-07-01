package ar.edu.itba.paw.webapp.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationErrorException extends AuthenticationException {
    public AuthenticationErrorException(String msg, Throwable t) {
        super(msg, t);
    }

    public AuthenticationErrorException(String msg) {
        super(msg);
    }
}
