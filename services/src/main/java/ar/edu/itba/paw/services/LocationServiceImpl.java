package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.LocationDao;
import ar.edu.itba.paw.interfaces.services.LocationService;
import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Location;
import java.util.HashMap;
import java.util.Map;
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
    return locationDao.createLocation(cityCode, address).getId();
  }

  @Override
  public Optional<Location> getLocationById(long id) {
    return locationDao.getLocationById(id);
  }

  @Override
  public Map<City, Integer> getUsedCities() {

    // Get all city codes present in the database & qty of appearences
    Map<Integer, Integer> cityCodes = locationDao.getUsedCities();

    Map<City, Integer> cityMap = new HashMap<>();
    cityCodes.forEach((key, value) -> cityMap.put(City.getCity(key), value));

    // Create a map of cities with the codes
    return cityMap;
  }
}
