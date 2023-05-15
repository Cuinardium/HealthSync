package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Location;
import java.util.Optional;

public interface LocationService {

  public long createLocation(int cityCode, String address);

  public Optional<Location> getLocationById(long id);
}
