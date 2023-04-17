package ar.edu.itba.paw.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.interfaces.persistence.SpecialtyDao;
import ar.edu.itba.paw.interfaces.services.SpecialtyService;
import ar.edu.itba.paw.models.Specialty;

@Service
public class SpecialtyServiceImpl implements SpecialtyService {

  private final SpecialtyDao specialtyDao;

  @Autowired
  public SpecialtyServiceImpl(SpecialtyDao specialtyDao) {
    this.specialtyDao = specialtyDao;
  }

  @Override
  public Specialty createSpecialty(String name) {
    
    // If it exists, return it
    Optional<Specialty> specialty = specialtyDao.getSpecialtyByName(name);

    if (specialty.isPresent()) {
      return specialty.get();
    }

    return specialtyDao.createSpecialty(name);
  }

  @Override
  public Optional<Specialty> getSpecialtyById(long id) {
    return specialtyDao.getSpecialtyById(id);
  }

  @Override
  public Optional<Specialty> getSpecialtyByName(String name) {
    return specialtyDao.getSpecialtyByName(name);
  }
}
