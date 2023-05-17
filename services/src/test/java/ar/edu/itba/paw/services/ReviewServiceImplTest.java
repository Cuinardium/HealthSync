package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ReviewDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

// Le permite a Mockito tomar control de JUnit y permite anotaciones que sino no estarian
// disponibles
@RunWith(MockitoJUnitRunner.class)
public class ReviewServiceImplTest {

  @Mock private ReviewDao reviewDao;

  @InjectMocks private ReviewServiceImpl rs;

  @Test
  public void testCreateReview() {}

  @Test
  public void testCreateReviewCanNotReview() {}

  @Test
  public void testGetReviewsForDoctor() {}

  @Test
  public void testCanReviewTrue() {}

  @Test
  public void testCanReviewFalse() {}
}
