package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Image;
import java.util.Optional;

public interface ImageService {

  // =============== Inserts ===============

  long uploadImage(Image image);

  // =============== Updates ===============

  void updateImage(Long pfpId, Image image);

  // =============== Queries ===============

  Optional<Image> getImage(long id);
}
