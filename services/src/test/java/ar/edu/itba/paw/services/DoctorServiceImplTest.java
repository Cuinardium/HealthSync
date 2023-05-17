package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.DoctorDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

// Le permite a Mockito tomar control de JUnit y permite anotaciones que sino no estarian
// disponibles
@RunWith(MockitoJUnitRunner.class)
public class DoctorServiceImplTest {

  @Mock private DoctorDao doctorDao;

  @InjectMocks private DoctorServiceImpl ds;

  @Test
  public void testCreateDoctor() {}

  @Test
  public void testCreateDoctorAlreadyExists() {}

  @Test
  public void testUpdateDoctor() {}

  @Test
  public void testUpdateDoctorDoesNotExist() {}

  @Test
  public void testGetDoctorById() {}

  @Test
  public void testGetDoctorByIdDoesNotExist() {}

  @Test
  public void testGetFilteredDoctors() {}

  @Test
  public void testGetDoctors() {}

  @Test
  public void testGetUsedSpecialties() {}

  @Test
  public void testGetUsedHealthInsurances() {}

  @Test
  public void testGetUsedCities() {}
}
