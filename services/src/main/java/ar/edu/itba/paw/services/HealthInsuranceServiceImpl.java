package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.HealthInsurance;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.interfaces.persistence.HealthInsuranceDao;
import ar.edu.itba.paw.interfaces.services.HealthInsuranceService;

@Service
public class HealthInsuranceServiceImpl implements HealthInsuranceService {

  private final HealthInsuranceDao healthInsuranceDao;

  @Autowired
  public HealthInsuranceServiceImpl(HealthInsuranceDao healthInsuranceDao) {
    this.healthInsuranceDao = healthInsuranceDao;
  }

  @Override
  public HealthInsurance createHealthInsurance(String name) {

    // If the health insurance already exists, return it
    Optional<HealthInsurance> healthInsurance = healthInsuranceDao.getHealthInsuranceByName(name);
    if (healthInsurance.isPresent()) {
      return healthInsurance.get();
    }

    return healthInsuranceDao.createHealthInsurance(name);
  }

  @Override
  public Optional<HealthInsurance> getHealthInsuranceById(long id) {
    return healthInsuranceDao.getHealthInsuranceById(id);
  }

}
