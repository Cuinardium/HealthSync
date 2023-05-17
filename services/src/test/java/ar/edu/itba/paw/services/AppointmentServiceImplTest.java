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
  public void testCreateAppointment() {}

  @Test
  public void testCreateAppointmentAlreadyExists() {}

  @Test
  public void testUpdateAppointment() {}

  @Test
  public void testUpdateAppointmentDoesNotExist() {}

  @Test
  public void testGetAppointmentById() {}

  @Test
  public void testGetAppointmentByIdDoesNotExist() {}

  @Test
  public void testGetAppointmentsForPatient() {}

  @Test
  public void testGetAppointmentsForPatientDoesNotExist() {}

  @Test
  public void testGetAppointmentsForDoctor() {}

  @Test
  public void testGetAppointmentsForDoctorDoesNotExist() {}

  @Test
  public void testGetFilteredAppointmentsForPatient() {}

  @Test
  public void testGetFilteredAppointmentsForPatientDoesNotExist() {}

  @Test
  public void testGetFilteredAppointmentsForDoctor() {}

  @Test
  public void testGetFilteredAppointmentsForDoctorDoesNotExist() {}

  @Test
  public void testGetAvailableHoursForDoctorOnDate() {}

  @Test
  public void testGetAvailableHoursForDoctorOnRange() {}
}
