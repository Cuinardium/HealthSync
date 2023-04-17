package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.HealthInsurance;
import java.util.Optional;

public interface HealthInsuranceDao {

  HealthInsurance createHealthInsurance(String name);

  Optional<HealthInsurance> getHealthInsuranceById(long id);

  Optional<HealthInsurance> getHealthInsuranceByName(String name);
}
