package ar.edu.itba.paw.interfaces.services;

import java.util.Optional;

import ar.edu.itba.paw.models.Location;

public interface LocationService {

  public long createLocation(int cityCode, String address);

  public Optional<Location> getLocationById(long id);
}
