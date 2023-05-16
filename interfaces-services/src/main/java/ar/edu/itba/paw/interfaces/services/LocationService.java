package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Location;
import java.util.Map;
import java.util.Optional;

public interface LocationService {

  public long createLocation(int cityCode, String address);

  public Optional<Location> getLocationById(long id);

  // Gets all cities used by doctors & qty of appearences
  public Map<City, Integer> getUsedCities();
}
