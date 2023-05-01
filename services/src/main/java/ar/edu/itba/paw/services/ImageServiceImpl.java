package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ImageDao;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.models.Image;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {

  private ImageDao imageDao;

  @Autowired
  public ImageServiceImpl(ImageDao imageDao) {
    this.imageDao = imageDao;
  }

  @Override
  public Optional<Image> getImage(long id) {
    return imageDao.getImage(id);
  }
}