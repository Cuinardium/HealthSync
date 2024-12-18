package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ImageDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.ImageNotFoundException;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.models.Image;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImageServiceImpl implements ImageService {

  private ImageDao imageDao;

  @Autowired
  public ImageServiceImpl(ImageDao imageDao) {
    this.imageDao = imageDao;
  }

  // =============== Inserts ===============

  @Transactional
  @Override
  public Image uploadImage(Image image) {
    return imageDao.createImage(image);
  }

  // =============== Updates ===============

  @Transactional
  @Override
  public Image updateImage(Image image) {
    try {
      return imageDao.updateImage(image);
    } catch (ImageNotFoundException e) {
      throw new RuntimeException();
    }
  }

  // =============== Queries ===============

  @Transactional(readOnly = true)
  @Override
  public Optional<Image> getImage(long id) {
    return imageDao.getImage(id);
  }
}
