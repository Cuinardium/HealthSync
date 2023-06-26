package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.TokenDao;
import ar.edu.itba.paw.interfaces.services.TokenService;
import ar.edu.itba.paw.interfaces.services.exceptions.TokenNotFoundException;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenServiceImpl implements TokenService {

  private TokenDao verificationTokenDao;

  @Autowired
  public TokenServiceImpl(TokenDao verificationTokenDao) {
    this.verificationTokenDao = verificationTokenDao;
  }

  // ============= CREATE =============

  @Override
  @Transactional
  public VerificationToken createToken(User user) {
    String token = UUID.randomUUID().toString();

    VerificationToken verificationToken = new VerificationToken(user, token);

    return verificationTokenDao.createToken(verificationToken);
  }

  // ============= QUERY =============

  @Override
  @Transactional(readOnly = true)
  public Optional<VerificationToken> getUserToken(User user) {
    return verificationTokenDao.getUserToken(user);
  }

  // ============= DELETE =============

  @Override
  @Transactional
  public void deleteUserToken(User user) throws TokenNotFoundException {
    VerificationToken verificationToken =
        verificationTokenDao.getUserToken(user).orElseThrow(TokenNotFoundException::new);

    verificationTokenDao.deleteToken(verificationToken);
  }

  // ============ UPDATE ============

  @Override
  @Transactional
  public void renewUserToken(User user) throws TokenNotFoundException {
    VerificationToken verificationToken =
        verificationTokenDao.getUserToken(user).orElseThrow(TokenNotFoundException::new);

    verificationToken.renewExpiryDateTime();
  }
}
