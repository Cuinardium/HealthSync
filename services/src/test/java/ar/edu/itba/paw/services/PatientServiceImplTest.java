package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.PatientDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

// Le permite a Mockito tomar control de JUnit y permite anotaciones que sino no estarian
// disponibles
@RunWith(MockitoJUnitRunner.class)
public class PatientServiceImplTest {

  @Mock private PatientDao patientDao;

  @InjectMocks private PatientServiceImpl ps;

  @Test
  public void testCreatePatient() {}

  @Test
  public void testCreatePatientAlreadyExists() {}

  @Test
  public void testUpdatePatient() {}

  @Test
  public void testUpdatePatientDoesNotExist() {}

  @Test
  public void testGetPatientById() {}

  @Test
  public void testGetPatientByIdDoesNotExist() {}
}
