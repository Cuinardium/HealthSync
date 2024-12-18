package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ImageDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.ImageNotFoundException;
import ar.edu.itba.paw.models.Image;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class ImageDaoJpa implements ImageDao {

  @PersistenceContext private EntityManager em;

  @Override
  public Image createImage(Image image) {
    image.setImageId(null);
    em.persist(image);
    return image;
  }

  @Override
  public Image updateImage(Image image) throws ImageNotFoundException {
    if (image.getImageId() == null) {
      throw new ImageNotFoundException();
    }
    final Image oldImage = getImage(image.getImageId()).orElseThrow(ImageNotFoundException::new);
    oldImage.setBytes(image.getBytes());
    em.persist(oldImage);
    return oldImage;
  }

  @Override
  public Optional<Image> getImage(long id) {
    return Optional.ofNullable(em.find(Image.class, id));
  }
}
