package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.webapp.dto.CityDto;
import ar.edu.itba.paw.webapp.mediaType.VndType;
import ar.edu.itba.paw.webapp.query.CityQuery;
import ar.edu.itba.paw.webapp.utils.ResponseUtil;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("cities")
@Component
public class CityController {
  private static final Logger LOGGER = LoggerFactory.getLogger(CityController.class);

  private final DoctorService doctorService;

  @Context private UriInfo uriInfo;

  @Autowired
  public CityController(final DoctorService doctorService) {
    this.doctorService = doctorService;
  }

  @GET
  @Produces(VndType.APPLICATION_CITY_LIST)
  public Response listCities(@Valid @BeanParam final CityQuery cityQuery) {
    LOGGER.debug("Listing cities");

    Map<String, Integer> citiesPopularity =
        doctorService.getUsedCities(
            cityQuery.getName(),
            cityQuery.getLocalDate(),
            cityQuery.getFromTimeEnum(),
            cityQuery.getToTimeEnum(),
            cityQuery.getSpecialtiesEnum(),
            cityQuery.getCities(),
            cityQuery.getHealthInsurancesEnum(),
            cityQuery.getMinRating());

    // Compare by alphabetical or by popularity
    Comparator<CityDto> comparator =
        cityQuery.sortByPopularity()
            ? Comparator.comparingInt(CityDto::getPopularity)
            : Comparator.comparing(CityDto::getName);

    if (cityQuery.reversed()) {
      comparator = comparator.reversed();
    }

    List<CityDto> cities =
        citiesPopularity.entrySet().stream()
            .map(entry -> CityDto.fromCity(entry.getKey(), entry.getValue()))
            .sorted(comparator)
            .collect(Collectors.toList());

    Page<CityDto> cityDtoPage = new Page<>(cities, cityQuery.getPage(), cityQuery.getPageSize());

    if (cityDtoPage.getContent().isEmpty()) {
      return Response.noContent().build();
    }

    return ResponseUtil.setPaginationLinks(
            Response.ok(new GenericEntity<List<CityDto>>(cityDtoPage.getContent()) {}),
            uriInfo,
            cityDtoPage)
        .build();
  }
}
