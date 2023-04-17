package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Location;
import java.util.Optional;

public interface LocationDao {

  public Location createLocation(String city, String address);

  public Optional<Location> getLocationById(long id);
}
