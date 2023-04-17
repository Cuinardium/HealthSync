package ar.edu.itba.paw.interfaces.services;

import java.util.Optional;

import ar.edu.itba.paw.models.Location;

public interface LocationService {

  public Location createLocation(String city, String address);

  public Optional<Location> getLocationById(long id);
}
