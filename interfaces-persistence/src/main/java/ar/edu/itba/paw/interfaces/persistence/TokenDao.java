package ar.edu.itba.paw.interfaces.persistence;

import java.util.Optional;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;

public interface TokenDao {

  // ============= CREATE =============

  VerificationToken createToken(VerificationToken token);

  // ============= QUERY =============

  Optional<VerificationToken> getUserToken(User user);

  // ============= DELETE =============

  void deleteToken(VerificationToken token);
}
