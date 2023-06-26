package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.exceptions.TokenNotFoundException;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;
import java.util.Optional;

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
