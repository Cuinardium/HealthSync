package ar.edu.itba.paw.interfaces.services;

import java.util.Optional;
import ar.edu.itba.paw.models.HealthInsurance;

public interface HealthInsuranceService {

  // Create a new health insurance, or return the existing one if it already exists
  public HealthInsurance createHealthInsurance(String name);

  public Optional<HealthInsurance> getHealthInsuranceById(long id);
}
