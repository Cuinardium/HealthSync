package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ImageDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.ImageNotFoundException;
import ar.edu.itba.paw.models.Image;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

// Le permite a Mockito tomar control de JUnit y permite anotaciones que sino no estarian
// disponibles
@RunWith(MockitoJUnitRunner.class)
public class ImageServiceImplTest {

  private static final long ID = 0;
  private static final byte[] BYTES = new byte[] {0, 1, 5, 2};

  private static final Image IMAGE = new Image.Builder(BYTES).id(ID).build();

  @Mock private ImageDao imageDao;

  @InjectMocks private ImageServiceImpl is;

  @Test
  public void testUploadImage() {
    // 1. Precondiciones
    Mockito.when(imageDao.createImage(IMAGE)).thenReturn(IMAGE);
    // 2. Ejercitar la class under test
    Image image = is.uploadImage(IMAGE);
    // 3. Meaningful assertions
    Assert.assertEquals(IMAGE, image);
  }

  // TODO: no lo puedo testear, si update image retorna void
  @Test
  public void testUpdateImage() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  // TODO: cng excep
  @Test(expected = RuntimeException.class)
  public void testUpdateImageDoesNotExist() throws ImageNotFoundException {
    // 1. Precondiciones
    Mockito.when(imageDao.updateImage(IMAGE)).thenThrow(ImageNotFoundException.class);
    // 2. Ejercitar la class under test
    is.updateImage(IMAGE);
    // 3. Meaningful assertions
  }

  @Test
  public void testGetImage() {
    // 1. Precondiciones
    Mockito.when(imageDao.getImage(ID)).thenReturn(Optional.of(IMAGE));
    // 2. Ejercitar la class under test
    Optional<Image> image = is.getImage(ID);
    // 3. Meaningful assertions
    Assert.assertTrue(image.isPresent());
    Assert.assertEquals(IMAGE, image.get());
  }

  @Test
  public void testGetImageNotFound() {
    // 1. Precondiciones
    Mockito.when(imageDao.getImage(ID)).thenReturn(Optional.empty());
    // 2. Ejercitar la class under test
    Optional<Image> image = is.getImage(ID);
    // 3. Meaningful assertions
    Assert.assertFalse(image.isPresent());
  }
}
