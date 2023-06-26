package ar.edu.itba.paw.persistence;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.persistence.TokenDao;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;

@Repository
public class TokenDaoJpa implements TokenDao {

  @PersistenceContext
  private EntityManager em;

  @Override
  public VerificationToken createToken(VerificationToken token) {
    em.persist(token);
    return token;
  }

  @Override
  public Optional<VerificationToken> getUserToken(User user) {
    final TypedQuery<VerificationToken> query = em.createQuery(
        "from VerificationToken as vt where vt.user = :user", VerificationToken.class);
    query.setParameter("user", user);
    return query.getResultList().stream().findFirst();
  }

  @Override
  public void deleteToken(VerificationToken token) {
    em.remove(token);
  }
}
