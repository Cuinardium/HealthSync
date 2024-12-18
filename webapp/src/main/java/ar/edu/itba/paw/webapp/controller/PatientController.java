package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.interfaces.services.exceptions.PatientNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import ar.edu.itba.paw.webapp.dto.PatientDto;
import ar.edu.itba.paw.webapp.exceptions.InvalidPasswordException;
import ar.edu.itba.paw.webapp.form.ChangePasswordForm;
import ar.edu.itba.paw.webapp.form.PatientEditForm;
import ar.edu.itba.paw.webapp.form.PatientRegisterForm;
import ar.edu.itba.paw.webapp.mediaType.VndType;
import ar.edu.itba.paw.webapp.utils.LocaleUtil;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Path("patients")
@Component
public class PatientController {
  private static final Logger LOGGER = LoggerFactory.getLogger(PatientController.class);

  private final PatientService patientService;
  private final UserService userService;

  @Context private UriInfo uriInfo;

  @Autowired
  public PatientController(final PatientService patientService, final UserService userService) {
    this.patientService = patientService;
    this.userService = userService;
  }

  @POST
  @Consumes(VndType.APPLICATION_PATIENT)
  public Response createPatient(@Valid final PatientRegisterForm patientRegisterForm)
      throws EmailInUseException {

    final Patient patient =
        patientService.createPatient(
            patientRegisterForm.getEmail(),
            patientRegisterForm.getPassword(),
            patientRegisterForm.getName(),
            patientRegisterForm.getLastname(),
            patientRegisterForm.getHealthInsuranceEnum(),
            LocaleUtil.getCurrentRequestLocale());

    LOGGER.debug("Registered {}", patient);

    URI createdPatientUri =
        uriInfo.getBaseUriBuilder().path("/patients").path(String.valueOf(patient.getId())).build();

    return Response.created(createdPatientUri).build();
  }

  // ================ patients/{patientId} ================

  @GET
  @Path("/{patientId:\\d+}")
  @Produces(VndType.APPLICATION_PATIENT)
  @PreAuthorize(
      "@authorizationFunctions.isUser(authentication, #patientId) || "
          + "@authorizationFunctions.hasAppointmentWithPatient(authentication, #patientId)")
  public Response getPatient(@PathParam("patientId") final long patientId)
      throws PatientNotFoundException {

    Patient patient =
        patientService.getPatientById(patientId).orElseThrow(PatientNotFoundException::new);

    LOGGER.debug("returning patient with id {}", patientId);

    PatientDto patientDto = PatientDto.fromPatient(uriInfo, patient);
    long currentUserId = PawAuthUserDetails.getCurrentUserId();

    if (currentUserId == patientId) {
      patientDto.addPrivateLinks(uriInfo);
    }

    return Response.ok(patientDto).build();
  }

  @PUT
  @Path("/{patientId:\\d+}")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @PreAuthorize("@authorizationFunctions.isUser(authentication, #patientId)")
  public Response updatePatient(
      @PathParam("patientId") final long patientId,
      @Valid @BeanParam final PatientEditForm patientEditForm)
      throws PatientNotFoundException, EmailInUseException {

    Image image = null;
    if (patientEditForm.hasFile()) {
      image =
          new Image.Builder(patientEditForm.getImageData(), patientEditForm.getImageMediaType())
              .build();
    }

    Patient patient =
        patientService.getPatientById(patientId).orElseThrow(PatientNotFoundException::new);

    patientService.updatePatient(
        patientId,
        patient.getEmail(),
        patientEditForm.getName(),
        patientEditForm.getLastname(),
        patientEditForm.getHealthInsuranceEnum(),
        image,
        patientEditForm.getLocale());

    LOGGER.debug("updated patient {}", patient);

    return Response.noContent().build();
  }

  @PATCH
  @Path("{patientId:\\d+}")
  @Consumes(VndType.APPLICATION_PASSWORD)
  @PreAuthorize("@authorizationFunctions.isUser(authentication, #patientId)")
  public Response updatePassword(
      @PathParam("patientId") final long patientId, @Valid final ChangePasswordForm passwordForm)
      throws UserNotFoundException, InvalidPasswordException {

    boolean updated =
        userService.updatePassword(
            patientId, passwordForm.getOldPassword(), passwordForm.getPassword());

    if (!updated) {
      throw new InvalidPasswordException();
    }

    LOGGER.debug("updated password for patient {}", patientId);

    return Response.noContent().build();
  }
}
