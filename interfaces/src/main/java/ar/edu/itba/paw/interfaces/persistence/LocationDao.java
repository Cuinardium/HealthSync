package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Location;
import java.util.Map;
import java.util.Optional;

public interface LocationDao {

  public long createLocation(int cityCode, String address);

  public Optional<Location> getLocationById(long id);

  // Get all city codes present in the database & qty of appearences
  public Map<Integer, Integer> getUsedCities();
}
