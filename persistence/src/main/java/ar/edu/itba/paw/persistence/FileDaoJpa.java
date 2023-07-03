package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.FileDao;
import ar.edu.itba.paw.models.File;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class FileDaoJpa implements FileDao {

  @PersistenceContext private EntityManager em;

  @Override
  public File uploadFile(File file) {
    em.persist(file);
    return file;
  }

  @Override
  public Optional<File> getFile(long id) {
    return Optional.ofNullable(em.find(File.class, id));
  }
}
