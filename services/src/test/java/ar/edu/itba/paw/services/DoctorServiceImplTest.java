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
  public void testCreateDoctor() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testCreateDoctorAlreadyExists() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testUpdateDoctor() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testUpdateDoctorDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testGetDoctorById() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testGetDoctorByIdDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testGetFilteredDoctors() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testGetDoctors() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testGetUsedSpecialties() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testGetUsedHealthInsurances() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testGetUsedCities() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }
}
