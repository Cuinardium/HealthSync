package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Image;
import java.util.Optional;

public interface ImageService {

  // =============== Inserts ===============

  public Image uploadImage(Image image);

  // =============== Updates ===============

  public Image updateImage(Image image);

  // =============== Queries ===============

  public Optional<Image> getImage(long id);
}
