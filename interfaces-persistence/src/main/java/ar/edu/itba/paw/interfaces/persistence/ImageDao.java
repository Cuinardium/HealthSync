package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.interfaces.persistence.exceptions.ImageNotFoundException;
import ar.edu.itba.paw.models.Image;
import java.util.Optional;

public interface ImageDao {

  // =============== Inserts ===============

  public Image createImage(Image image);

  // =============== Updates ===============

  public Image updateImage(Image image) throws ImageNotFoundException;

  // =============== Queries ===============

  public Optional<Image> getImage(long id);
}
