package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.webapp.dto.ErrorDto;
import ar.edu.itba.paw.webapp.dto.HealthInsuranceDto;
import ar.edu.itba.paw.webapp.mediaType.VndType;
import ar.edu.itba.paw.webapp.utils.LocaleUtil;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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

  private final MessageSource messageSource;

  @Context private UriInfo uriInfo;

  @Autowired
  public HealthInsuranceController(final MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @GET
  @Produces(VndType.APPLICATION_HEALTH_INSURANCE_LIST)
  public Response listHealthInsurances() {
    List<HealthInsurance> healthInsuranceList = Arrays.asList(HealthInsurance.values());

    final List<HealthInsuranceDto> dtoList =
        healthInsuranceList.stream()
            .map(
                healthInsurance ->
                    HealthInsuranceDto.fromHealthInsurance(uriInfo, messageSource, healthInsurance))
            .collect(Collectors.toList());

    return Response.ok(new GenericEntity<List<HealthInsuranceDto>>(dtoList) {}).build();
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

    LOGGER.debug("returning healthInsurance with id {}", id);

    return Response.ok(
            HealthInsuranceDto.fromHealthInsurance(uriInfo, messageSource, healthInsurance))
        .build();
  }
}
