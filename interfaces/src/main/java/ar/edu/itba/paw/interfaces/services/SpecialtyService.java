package ar.edu.itba.paw.interfaces.services;

import java.util.Optional;

import ar.edu.itba.paw.models.Specialty;

public interface SpecialtyService {

  // Create a new specialty, or return the existing one if it already exists
  public Specialty createSpecialty(String name);

  public Optional<Specialty> getSpecialtyById(long id);

  public Optional<Specialty> getSpecialtyByName(String name);
}
