package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Image;
import java.util.Optional;

public interface ImageService {

  // =============== Inserts ===============

  Image uploadImage(Image image);

  // =============== Updates ===============

  Image updateImage(Image image);

  // =============== Queries ===============

  Optional<Image> getImage(long id);
}
