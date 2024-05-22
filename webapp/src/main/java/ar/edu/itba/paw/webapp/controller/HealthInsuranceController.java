package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.webapp.dto.HealthInsuranceDto;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Path("healthinsurances")
@Component
public class HealthInsuranceController {
  private static final Logger LOGGER = LoggerFactory.getLogger(HealthInsuranceController.class);

  @Context private UriInfo uriInfo;

  // TODO: paginate?
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response listHealthInsurances() {
    List<HealthInsurance> healthInsuranceList = Arrays.asList(HealthInsurance.values());

    final List<HealthInsuranceDto> dtoList =
        HealthInsuranceDto.fromHealthInsuranceList(uriInfo, healthInsuranceList);
    return Response.ok(new GenericEntity<List<HealthInsuranceDto>>(dtoList) {}).build();
  }

  @GET
  @Path("/{id}")
  public Response getHealthInsurance(@PathParam("id") final int id) throws UserNotFoundException {
    HealthInsurance[] healthInsurances = HealthInsurance.values();
    if (id < 0 || id >= healthInsurances.length) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    HealthInsurance healthInsurance = healthInsurances[id];
    LOGGER.debug("returning healthInsurance with id {}", id);
    return Response.ok(HealthInsuranceDto.fromHealthInsurance(uriInfo, healthInsurance)).build();
  }
}
