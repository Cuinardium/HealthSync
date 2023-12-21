package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.dto.DoctorDto;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.DoctorRegisterForm;
import java.net.URI;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Path("doctors")
@Component
public class DoctorController {
  private static final Logger LOGGER = LoggerFactory.getLogger(DoctorController.class);

  private final DoctorService doctorService;

  @Context private UriInfo uriInfo;

  @Autowired
  public DoctorController(final DoctorService doctorService) {
    this.doctorService = doctorService;
  }

  @GET
  // @Produces("application/vnd.doctorList.v1+json")
  @Produces(MediaType.APPLICATION_JSON)
  public Response listDoctors(@QueryParam("page") @DefaultValue("1") final int page) {

    // page tiene que ser > 1
    if (page < 1) {
      LOGGER.debug("Bad request page starts a 1");
      return Response.status(Response.Status.BAD_REQUEST).build();
    }

    // TODO: filter by page
    List<Doctor> doctorList = doctorService.getDoctors();
    if (doctorList.isEmpty()) {
      LOGGER.debug("No content for page {}", page);
      return Response.noContent().build();
    }

    final List<DoctorDto> dtoList = DoctorDto.fromDoctorList(uriInfo, doctorList);

    // TODO: fill links
    LOGGER.debug("doctors for page {}", page);
    // TODO: hay q verificar exitencia para first tambien?
    String first = uriInfo.getRequestUriBuilder().replaceQueryParam("page", 1).toString();
    // TODO: cambiar por una consulta para saber cual es el last
    String last = uriInfo.getRequestUriBuilder().replaceQueryParam("page", 1).toString();
    // TODO: verificar exitencia de page + 1
    String next = uriInfo.getRequestUriBuilder().replaceQueryParam("page", page + 1).toString();
    // TODO: verificar exitencia de page - 1
    String prev = uriInfo.getRequestUriBuilder().replaceQueryParam("page", page - 1).toString();
    return Response.ok(new GenericEntity<List<DoctorDto>>(dtoList) {})
        .link(next, "next")
        .link(prev, "prev")
        .link(first, "first")
        .link(last, "last")
        .build();
  }

  @POST
  public Response createDoctor(@Valid final DoctorRegisterForm doctorRegisterForm) {

    // TODO: form errors?

    Specialty specialty = Specialty.values()[doctorRegisterForm.getSpecialtyCode()];
    Set<HealthInsurance> healthInsurances =
        doctorRegisterForm
            .getHealthInsuranceCodes()
            .stream()
            .map(code -> HealthInsurance.values()[code])
            .collect(Collectors.toSet());

    ThirtyMinuteBlock[] values = ThirtyMinuteBlock.values();
    Set<AttendingHours> attendingHours = new HashSet<>();
    for (Map.Entry<DayOfWeek, List<Integer>> aux :
        doctorRegisterForm.getAttendingHours().entrySet()) {
      for (Integer ordinal : aux.getValue()) {
        attendingHours.add(new AttendingHours(null, aux.getKey(), values[ordinal]));
      }
    }
    try {
      final Doctor doctor =
          doctorService.createDoctor(
              doctorRegisterForm.getEmail(),
              doctorRegisterForm.getPassword(),
              doctorRegisterForm.getName(),
              doctorRegisterForm.getLastname(),
              specialty,
              doctorRegisterForm.getCity(),
              doctorRegisterForm.getAddress(),
              healthInsurances,
              attendingHours,
              LocaleContextHolder.getLocale());
      LOGGER.info("Registered {}", doctor);
      URI createdDoctorUri =
          uriInfo.getBaseUriBuilder().path("/doctors").path(String.valueOf(doctor.getId())).build();
      return Response.created(createdDoctorUri).build();
    } catch (EmailInUseException e) {
      LOGGER.warn("Failed to register doctor due to email unique constraint");
      return Response.status(Response.Status.CONFLICT).build();
    }
  }

  @GET
  @Path("/{id}")
  public Response getDoctor(@PathParam("id") final long id) throws UserNotFoundException {
    Doctor doctor = doctorService.getDoctorById(id).orElseThrow(UserNotFoundException::new);
    // TODO: links for attributes?
    LOGGER.debug("returning doctor with id {}", id);
    return Response.ok(DoctorDto.fromDoctor(uriInfo, doctor)).build();
  }
}
