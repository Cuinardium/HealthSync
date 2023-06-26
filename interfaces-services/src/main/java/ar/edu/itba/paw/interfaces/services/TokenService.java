package ar.edu.itba.paw.interfaces.services;

import java.util.Optional;

import ar.edu.itba.paw.interfaces.services.exceptions.TokenNotFoundException;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;

public interface TokenService {
  
  // ============= CREATE =============

  VerificationToken createToken(User user);

  // ============= QUERY =============

  Optional<VerificationToken> getUserToken(User user);

  // ============= DELETE =============

  void deleteUserToken(User user) throws TokenNotFoundException;

  // ============ UPDATE ============

  void renewUserToken(User user) throws TokenNotFoundException;
}
