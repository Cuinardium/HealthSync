package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Image;
import java.util.Optional;

public interface ImageService {
  Optional<Image> getImage(long id);

  void uploadImage(Image image);

  void updateImage(long id, Image image);
}
