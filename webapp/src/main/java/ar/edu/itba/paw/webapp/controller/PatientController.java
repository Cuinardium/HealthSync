package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.interfaces.services.exceptions.PatientNotFoundException;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.webapp.dto.PatientDto;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.PatientEditForm;
import ar.edu.itba.paw.webapp.form.PatientRegisterForm;
import java.io.IOException;
import java.net.URI;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
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

  @POST
  public Response createPatient(@Valid final PatientRegisterForm patientRegisterForm) {
    if (patientRegisterForm == null) {
      return Response.status(Response.Status.BAD_REQUEST).entity("Form cannot be empty").build();
    }
    HealthInsurance healthInsurance =
        HealthInsurance.values()[patientRegisterForm.getHealthInsuranceCode()];

    // TODO: include locale in payload??
    try {
      final Patient patient =
          patientService.createPatient(
              patientRegisterForm.getEmail(),
              patientRegisterForm.getPassword(),
              patientRegisterForm.getName(),
              patientRegisterForm.getLastname(),
              healthInsurance,
              LocaleContextHolder.getLocale());

      LOGGER.info("Registered {}", patient);
      URI createdPatientUri =
          uriInfo
              .getBaseUriBuilder()
              .path("/patients")
              .path(String.valueOf(patient.getId()))
              .build();
      return Response.created(createdPatientUri).build();
    } catch (EmailInUseException e) {
      LOGGER.warn("Failed to register patient due to email unique constraint");
      return Response.status(Response.Status.CONFLICT).build();
    }
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPatient(@PathParam("id") final long id) throws UserNotFoundException {
    Patient patient = patientService.getPatientById(id).orElseThrow(UserNotFoundException::new);
    LOGGER.debug("returning patient with id {}", id);
    return Response.ok(PatientDto.fromPatient(uriInfo, patient)).build();
  }

  @PUT
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response updatePatient(
      @PathParam("id") final long id, @Valid final PatientEditForm patientEditForm) {
    try {
      HealthInsurance healthInsurance =
          HealthInsurance.values()[patientEditForm.getHealthInsuranceCode()];
      Image image = null;
      if (!patientEditForm.getImage().isEmpty()) {
        image = new Image.Builder(patientEditForm.getImage().getBytes()).build();
      }
      Patient patient =
          patientService.updatePatient(
              id,
              patientEditForm.getEmail(),
              patientEditForm.getName(),
              patientEditForm.getLastname(),
              healthInsurance,
              image,
              patientEditForm.getLocale());

      LOGGER.debug("updated patient {}", patient);
      return Response.noContent().build();
    } catch (IOException e) {
      // TODO: handle
      return Response.status(Response.Status.CONFLICT).build();
    } catch (PatientNotFoundException e) {
      LOGGER.warn("Failed to find patient");
      return Response.status(Response.Status.CONFLICT).build();
    } catch (EmailInUseException e) {
      LOGGER.warn("Failed to modify patient´s email due to email unique constraint");
      return Response.status(Response.Status.CONFLICT).build();
    }
  }
}
