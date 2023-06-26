package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.FileDao;
import ar.edu.itba.paw.models.File;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class FileDaoJpa implements FileDao {

    @PersistenceContext
    EntityManager em;

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
