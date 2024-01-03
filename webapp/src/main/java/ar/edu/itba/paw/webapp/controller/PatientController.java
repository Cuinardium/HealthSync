package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.webapp.dto.PatientDto;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("patients")
@Component
public class PatientController {
  private static final Logger LOGGER = LoggerFactory.getLogger(PatientController.class);

  private final PatientService patientService;

  @Context private UriInfo uriInfo;

  @Autowired
  public PatientController(final PatientService patientService) {
    this.patientService = patientService;
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPatient(@PathParam("id") final long id) throws UserNotFoundException {
    Patient patient = patientService.getPatientById(id).orElseThrow(UserNotFoundException::new);
    LOGGER.debug("returning patient with id {}", id);
    return Response.ok(PatientDto.fromPatient(uriInfo, patient)).build();
  }
}
