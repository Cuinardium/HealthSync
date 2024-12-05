package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.webapp.dto.ErrorDto;
import ar.edu.itba.paw.webapp.dto.HealthInsuranceDto;
import ar.edu.itba.paw.webapp.mediaType.VndType;
import ar.edu.itba.paw.webapp.query.HealthInsuranceQuery;
import ar.edu.itba.paw.webapp.utils.LocaleUtil;
import ar.edu.itba.paw.webapp.utils.ResponseUtil;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Path("healthinsurances")
@Component
public class HealthInsuranceController {
  private static final Logger LOGGER = LoggerFactory.getLogger(HealthInsuranceController.class);

  private final DoctorService doctorService;

  private final MessageSource messageSource;

  @Context private UriInfo uriInfo;

  @Autowired
  public HealthInsuranceController(
      final DoctorService doctorService, final MessageSource messageSource) {
    this.doctorService = doctorService;
    this.messageSource = messageSource;
  }

  @GET
  @Produces(VndType.APPLICATION_HEALTH_INSURANCE_LIST)
  public Response listHealthInsurances(
      @Valid @BeanParam HealthInsuranceQuery healthInsuranceQuery) {

    LOGGER.debug("Listing health insurances, page: {}", healthInsuranceQuery.getPage());

    Map<HealthInsurance, Integer> healthInsurancePopularity =
        doctorService.getUsedHealthInsurances(
            healthInsuranceQuery.getName(),
            healthInsuranceQuery.getLocalDate(),
            healthInsuranceQuery.getFromTimeEnum(),
            healthInsuranceQuery.getToTimeEnum(),
            healthInsuranceQuery.getSpecialtiesEnum(),
            healthInsuranceQuery.getCities(),
            healthInsuranceQuery.getHealthInsurancesEnum(),
            healthInsuranceQuery.getMinRating(),
            healthInsuranceQuery.sortByPopularity(),
            healthInsuranceQuery.reversed());

    final List<HealthInsuranceDto> dtoList =
        healthInsurancePopularity.entrySet().stream()
            .map(
                entry ->
                    HealthInsuranceDto.fromHealthInsurance(
                        uriInfo, messageSource, entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());

    Page<HealthInsuranceDto> healthInsurancePage =
        new Page<>(dtoList, healthInsuranceQuery.getPage(), healthInsuranceQuery.getPageSize());

    if (healthInsurancePage.getContent().isEmpty()) {
      return Response.noContent().build();
    }

    return ResponseUtil.setPaginationLinks(
            Response.ok(
                new GenericEntity<List<HealthInsuranceDto>>(healthInsurancePage.getContent()) {}),
            uriInfo,
            healthInsurancePage)
        .build();
  }

  @GET
  @Produces(VndType.APPLICATION_HEALTH_INSURANCE)
  @Path("/{id:\\d+}")
  public Response getHealthInsurance(@PathParam("id") final int id) {

    HealthInsurance[] healthInsurances = HealthInsurance.values();
    if (id < 0 || id >= healthInsurances.length) {
      String message =
          messageSource.getMessage(
              "error.healthInsuranceNotFound", null, LocaleUtil.getCurrentRequestLocale());

      return Response.status(Response.Status.NOT_FOUND)
          .entity(ErrorDto.fromMessage(message))
          .build();
    }

    HealthInsurance healthInsurance = healthInsurances[id];
    int popularity =
        doctorService
            .getUsedHealthInsurances(null, null, null, null, null, null, null, null, null, null)
            .getOrDefault(healthInsurance, 0);

    LOGGER.debug("returning healthInsurance with id {}", id);

    return Response.ok(
            HealthInsuranceDto.fromHealthInsurance(
                uriInfo, messageSource, healthInsurance, popularity))
        .build();
  }
}
