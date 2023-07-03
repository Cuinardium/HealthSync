package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;
import java.util.Optional;

public interface TokenDao {

  // ============= CREATE =============

  public VerificationToken createToken(VerificationToken token);

  // ============= QUERY =============

  public Optional<VerificationToken> getUserToken(User user);

  // ============= DELETE =============

  public void deleteToken(VerificationToken token);
}
