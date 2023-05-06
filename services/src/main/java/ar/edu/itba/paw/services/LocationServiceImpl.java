package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.LocationDao;
import ar.edu.itba.paw.interfaces.services.LocationService;
import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Location;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocationServiceImpl implements LocationService {

  private final LocationDao locationDao;

  @Autowired
  public LocationServiceImpl(LocationDao locationDao) {
    this.locationDao = locationDao;
  }

  @Transactional
  @Override
  public long createLocation(int cityCode, String address) {
    return locationDao.createLocation(cityCode, address);
  }

  @Override
  public Optional<Location> getLocationById(long id) {
    return locationDao.getLocationById(id);
  }

  @Override
  public List<City> getUsedCities() {

    // Get all city codes present in the database
    List<Integer> cityCodes = locationDao.getUsedCities();

    // Create a list of cities with the codes
    return City.getCities(cityCodes);
  }
}
