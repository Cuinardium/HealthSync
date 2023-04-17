package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Specialty;
import java.util.Optional;

public interface SpecialtyDao {

  Specialty createSpecialty(String name);

  Optional<Specialty> getSpecialtyById(long id);

  Optional<Specialty> getSpecialtyByName(String name);
}
