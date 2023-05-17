package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.interfaces.persistence.exceptions.ImageNotFoundException;
import ar.edu.itba.paw.models.Image;
import java.util.Optional;

public interface ImageDao {

  // =============== Inserts ===============

  Image createImage(Image image);

  // =============== Updates ===============

  Image updateImage(Long imageId, Image image) throws ImageNotFoundException;

  // =============== Queries ===============

  Optional<Image> getImage(long id);
}
