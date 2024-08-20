package ar.edu.itba.paw.persistence;

import static org.junit.Assert.assertThrows;

import ar.edu.itba.paw.interfaces.persistence.exceptions.ImageNotFoundException;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.persistence.config.TestConfig;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ImageDaoImplTest {

  private static final Long INSERTED_IMAGE_ID = 2L;
  private static final byte[] INSERTED_IMAGE_BYTES = {16};

  private static final Long AUX_IMAGE_ID = 3L;
  private static final byte[] AUX_IMAGE_BYTES = {12, 15, 1, 2};
  private static final String AUX_IMAGE_MEDIA_TYPE = "image/jpeg";

  @PersistenceContext private EntityManager em;

  @Autowired private DataSource ds;

  private JdbcTemplate jdbcTemplate;

  @Autowired private ImageDaoJpa imageDao;

  @Before
  public void setUp() {
    jdbcTemplate = new JdbcTemplate(ds);
  }

  @Test
  public void testGetImage() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Optional<Image> image = imageDao.getImage(INSERTED_IMAGE_ID);
    // 3. Meanignful assertions
    Assert.assertTrue(image.isPresent());
    Assert.assertEquals(INSERTED_IMAGE_ID, image.get().getImageId());
    Assert.assertArrayEquals(INSERTED_IMAGE_BYTES, image.get().getBytes());
  }

  @Test
  public void testGetImageDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Optional<Image> image = imageDao.getImage(AUX_IMAGE_ID);
    // 3. Meanignful assertions
    Assert.assertFalse(image.isPresent());
  }

  @Test
  public void testCreateImage() {
    // 1. Precondiciones (script testImage.sql)
    // 2. Ejercitar la class under test
    Image image =
        imageDao.createImage(new Image.Builder(AUX_IMAGE_BYTES, AUX_IMAGE_MEDIA_TYPE).build());

    em.flush();

    // 3. Meanignful assertions
    Assert.assertArrayEquals(AUX_IMAGE_BYTES, image.getBytes());

    Assert.assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "profile_picture"));
  }

  @Test
  public void testUpdateImage() throws ImageNotFoundException {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Image image =
        imageDao.updateImage(
            new Image.Builder(AUX_IMAGE_BYTES, AUX_IMAGE_MEDIA_TYPE).id(INSERTED_IMAGE_ID).build());
    // 3. Meanignful assertions
    Assert.assertEquals(INSERTED_IMAGE_ID, image.getImageId());
    Assert.assertArrayEquals(AUX_IMAGE_BYTES, image.getBytes());
  }

  @Test
  public void testUpdateImageDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    assertThrows(
        ImageNotFoundException.class,
        () ->
            imageDao.updateImage(
                new Image.Builder(AUX_IMAGE_BYTES, AUX_IMAGE_MEDIA_TYPE).id(AUX_IMAGE_ID).build()));
    // 3. Meanignful assertions
  }
}
