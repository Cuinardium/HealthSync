package ar.edu.itba.paw.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.interfaces.persistence.LocationDao;
import ar.edu.itba.paw.interfaces.services.LocationService;
import ar.edu.itba.paw.models.Location;

@Service
public class LocationServiceImpl implements LocationService {

  private final LocationDao locationDao;

  @Autowired
  public LocationServiceImpl(LocationDao locationDao) {
    this.locationDao = locationDao;
  }

  @Override
  public Location createLocation(String city, String address) {
    return locationDao.createLocation(city, address);
  }

  @Override
  public Optional<Location> getLocationById(long id) {
    return locationDao.getLocationById(id);
  }
}
