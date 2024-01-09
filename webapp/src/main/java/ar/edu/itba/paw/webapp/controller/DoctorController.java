package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.dto.DoctorDto;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.DoctorFilterForm;
import ar.edu.itba.paw.webapp.form.DoctorRegisterForm;
import ar.edu.itba.paw.webapp.utils.ResponseUtil;
import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalDate;
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

  private static final int DEFAULT_PAGE_SIZE = 10;

  @Autowired
  public DoctorController(final DoctorService doctorService) {
    this.doctorService = doctorService;
  }

  // TODO: mover el filer form a query params
  @GET
  // @Produces("application/vnd.doctorList.v1+json")
  @Produces(MediaType.APPLICATION_JSON)
  public Response listDoctors(
      @Valid final DoctorFilterForm doctorFilterForm,
      @QueryParam("page") @DefaultValue("1") final int page) {

    // page tiene que ser > 1
    if (page < 1) {
      LOGGER.debug("Bad request page starts a 1");
      return Response.status(Response.Status.BAD_REQUEST).build();
    }

    String name = doctorFilterForm.getName();
    Set<Integer> specialtyCodes = doctorFilterForm.getSpecialtyCodes();
    Set<String> cities = doctorFilterForm.getCities();
    Set<Integer> healthInsuranceCodes = doctorFilterForm.getHealthInsuranceCodes();
    LocalDate date = doctorFilterForm.getDate();
    int fromOrdinal = doctorFilterForm.getFrom();
    int toOrdinal = doctorFilterForm.getTo();
    int minRating = doctorFilterForm.getMinRating();

    ThirtyMinuteBlock fromTime = ThirtyMinuteBlock.values()[fromOrdinal];
    ThirtyMinuteBlock toTime = ThirtyMinuteBlock.values()[toOrdinal];

    Set<Specialty> specialties = new HashSet<>();
    if (specialtyCodes != null) {
      for (Integer specialtyCode : specialtyCodes) {
        if (specialtyCode < 0 || specialtyCode >= Specialty.values().length) {
          continue;
        }
        specialties.add(Specialty.values()[specialtyCode]);
      }
    }

    Set<HealthInsurance> healthInsurances = new HashSet<>();
    if (healthInsuranceCodes != null) {
      for (Integer healthInsuranceCode : healthInsuranceCodes) {
        if (healthInsuranceCode < 0 || healthInsuranceCode >= HealthInsurance.values().length) {
          continue;
        }
        healthInsurances.add(HealthInsurance.values()[healthInsuranceCode]);
      }
    }

    // TODO: allow for variable page size?
    Page<Doctor> doctorsPage =
        doctorService.getFilteredDoctors(
            name,
            date,
            fromTime,
            toTime,
            specialties,
            cities,
            healthInsurances,
            minRating,
            page - 1,
            DEFAULT_PAGE_SIZE);

    List<Doctor> doctorList = doctorsPage.getContent();
    if (doctorList.isEmpty()) {
      LOGGER.debug("No content for page {}", page);
      return Response.noContent().build();
    }

    final List<DoctorDto> dtoList = DoctorDto.fromDoctorList(uriInfo, doctorList);

    LOGGER.debug("doctors for page {}", page);

    Response.ResponseBuilder responseBuilder =
        Response.ok(new GenericEntity<List<DoctorDto>>(dtoList) {});

    return ResponseUtil.setPaginationLinks(responseBuilder, uriInfo, doctorsPage).build();
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
