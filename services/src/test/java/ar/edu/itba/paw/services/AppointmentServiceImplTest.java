package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.AppointmentDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

// Le permite a Mockito tomar control de JUnit y permite anotaciones que sino no estarian
// disponibles
@RunWith(MockitoJUnitRunner.class)
public class AppointmentServiceImplTest {

  @Mock private AppointmentDao appointmentDao;

  @InjectMocks private AppointmentServiceImpl as;

  @Test
  public void testCreateAppointment() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testCreateAppointmentAlreadyExists() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testUpdateAppointment() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testUpdateAppointmentDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testGetAppointmentById() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testGetAppointmentByIdDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testGetAppointmentsForPatient() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testGetAppointmentsForPatientDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testGetAppointmentsForDoctor() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testGetAppointmentsForDoctorDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testGetFilteredAppointmentsForPatient() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testGetFilteredAppointmentsForPatientDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testGetFilteredAppointmentsForDoctor() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testGetFilteredAppointmentsForDoctorDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testGetAvailableHoursForDoctorOnDate() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testGetAvailableHoursForDoctorOnRange() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }
}
