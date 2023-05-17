package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ImageDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

// Le permite a Mockito tomar control de JUnit y permite anotaciones que sino no estarian
// disponibles
@RunWith(MockitoJUnitRunner.class)
public class ImageServiceImplTest {

  @Mock private ImageDao imageDao;

  @InjectMocks private ImageServiceImpl is;

  @Test
  public void testUploadImage() {}

  @Test
  public void testUpdateImage() {}

  @Test
  public void testUpdateImageDoesNotExist() {}

  @Test
  public void testGetImage() {}

  @Test
  public void testGetImageNotFound() {}
}
