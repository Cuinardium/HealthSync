package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.annotations.HasAllDays;
import ar.edu.itba.paw.webapp.dto.AttendingHoursDto;
import ar.edu.itba.paw.webapp.dto.DoctorDto;
import ar.edu.itba.paw.webapp.form.AttendingHoursForm;
import ar.edu.itba.paw.webapp.form.DoctorEditForm;
import ar.edu.itba.paw.webapp.form.DoctorFilterForm;
import ar.edu.itba.paw.webapp.form.DoctorRegisterForm;
import ar.edu.itba.paw.webapp.mediaType.VndType;
import ar.edu.itba.paw.webapp.utils.ResponseUtil;
import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Path("doctors")
@Component
public class DoctorController {
  private static final Logger LOGGER = LoggerFactory.getLogger(DoctorController.class);
  private static final int DEFAULT_PAGE_SIZE = 10;
  private static final String DATE_FORMAT = "dd-MM-yyyy";
  private final DoctorService doctorService;
  @Context private UriInfo uriInfo;

  @Autowired
  public DoctorController(final DoctorService doctorService) {
    this.doctorService = doctorService;
  }

  // ================= doctors ========================

  // TODO: mover el filer form a query params
  @GET
  @Produces(VndType.APPLICATION_DOCTOR_LIST)
  public Response listDoctors(
      @Valid final DoctorFilterForm doctorFilterForm,
      @QueryParam("page") @DefaultValue("1") final int page,
      @QueryParam("name") final String name,
      @QueryParam("fromTime") final String fromTimeString,
      @QueryParam("toTime") final String toTimeString,
      @QueryParam("minRating") final Integer minRating,
      @QueryParam("date") final String dateString,
      @QueryParam("specialty") final Set<String> specialtiesStringSet,
      @QueryParam("city") final Set<String> citiesStringSet,
      @QueryParam("healthInsurance") final Set<String> healthInsurancesStringSet) {

    LOGGER.debug(
        "\n\n\nValues are:\n"
            + "page {}\n"
            + "name {}\n"
            + "fromTime {}\n"
            + "toTime {}\n"
            + "minRating {}\n"
            + "date {}\n"
            + "specialty {}\n"
            + "city {}\n"
            + "healthInsurance {}\n"
            + "\n\n\n",
        page,
        name,
        fromTimeString,
        toTimeString,
        minRating,
        dateString,
        specialtiesStringSet,
        citiesStringSet,
        healthInsurancesStringSet);

    // page tiene que ser > 1
    if (page < 1) {
      LOGGER.debug("Bad request page starts a 1");
      return Response.status(Response.Status.BAD_REQUEST).build();
    }

    LocalDate date = null;
    if (dateString != null) {
      date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    ThirtyMinuteBlock fromTime = null;
    if (fromTimeString != null) {
      fromTime = ThirtyMinuteBlock.valueOf(fromTimeString);
    }
    ThirtyMinuteBlock toTime = null;
    if (toTimeString != null) {
      toTime = ThirtyMinuteBlock.valueOf(toTimeString);
    }
    // TODO: hace falta esto? (hay otro mapping mas adelante) quizas habria que quedarnos con 1 solo
    // !!!
    Set<Specialty> specialties =
        specialtiesStringSet.stream().map(Specialty::valueOf).collect(Collectors.toSet());
    Set<HealthInsurance> healthInsurances =
        healthInsurancesStringSet.stream()
            .map(HealthInsurance::valueOf)
            .collect(Collectors.toSet());

    // TODO: CHECK ERRORS for all types
    // TODO: allow for variable page size?date
    Page<Doctor> doctorsPage =
        doctorService.getFilteredDoctors(
            name,
            date,
            fromTime,
            toTime,
            specialties,
            citiesStringSet,
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
  @Consumes(VndType.APPLICATION_DOCTOR)
  public Response createDoctor(@Valid final DoctorRegisterForm doctorRegisterForm)
      throws EmailInUseException {

    final Doctor doctor =
        doctorService.createDoctor(
            doctorRegisterForm.getEmail(),
            doctorRegisterForm.getPassword(),
            doctorRegisterForm.getName(),
            doctorRegisterForm.getLastname(),
            doctorRegisterForm.getSpecialtyEnum(),
            doctorRegisterForm.getCity(),
            doctorRegisterForm.getAddress(),
            doctorRegisterForm.getHealthInsurancesEnum(),
            DoctorRegisterForm.getDefaultAttendingHours(),
            LocaleContextHolder.getLocale());

    LOGGER.info("Registered {}", doctor);

    URI createdDoctorUri =
        uriInfo.getBaseUriBuilder().path("/doctors")
                 .path(String.valueOf(doctor.getId()))
      .build();

    return Response.created(createdDoctorUri).build();
  }

  // ================= doctors/{id} ========================

  @GET
  @Path("/{doctorId:\\d+}")
  @Produces(VndType.APPLICATION_DOCTOR)
  public Response getDoctor(@PathParam("doctorId") final long doctorId)
      throws DoctorNotFoundException {

    Doctor doctor = doctorService.getDoctorById(doctorId).orElseThrow(DoctorNotFoundException::new);

    LOGGER.debug("returning doctor with id {}", doctorId);

    return Response.ok(DoctorDto.fromDoctor(uriInfo, doctor)).build();
  }

  @PUT
  @Path("/{doctorId:\\d+}")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @PreAuthorize("@authorizationFunctions.isUser(authentication, #doctorId)")
  public Response updateDoctor(
      @PathParam("doctorId") final long doctorId,
      @Valid @BeanParam final DoctorEditForm doctorEditForm)
      throws DoctorNotFoundException, EmailInUseException {

    LOGGER.debug("Updating doctor with id {}", doctorId);

    Doctor doctor = doctorService.getDoctorById(doctorId).orElseThrow(DoctorNotFoundException::new);

    Image image = null;
    if (doctorEditForm.hasFile()) {
      image = new Image.Builder(doctorEditForm.getImageData()).build();
    }

    // TODO: delete attending hours from method signature
    doctor =
        doctorService.updateDoctor(
            doctorId,
            doctorEditForm.getEmail(),
            doctorEditForm.getName(),
            doctorEditForm.getLastname(),
            doctorEditForm.getSpecialtyEnum(),
            doctorEditForm.getCity(),
            doctorEditForm.getAddress(),
            doctorEditForm.getHealthInsurancesEnum(),
            doctor.getAttendingHours(),
            image,
            doctorEditForm.getLocale());

    LOGGER.debug("Updated doctor {}", doctor);

    return Response.noContent().build();
  }

  // ================= doctors/{id}/attendinghours ========================

  @GET
  @Path("/{doctorId:\\d+}/attendinghours")
  @Produces(VndType.APPLICATION_ATTENDING_HOURS_LIST)
  public Response getAttendingHours(@PathParam("doctorId") final long doctorId)
      throws DoctorNotFoundException {

    Doctor doctor = doctorService.getDoctorById(doctorId).orElseThrow(DoctorNotFoundException::new);

    Set<AttendingHours> attendingHours = doctor.getAttendingHours();

    List<AttendingHoursDto> attendingHoursDtoList =
        AttendingHoursDto.fromAttendingHours(attendingHours);

    return Response.ok(new GenericEntity<List<AttendingHoursDto>>(attendingHoursDtoList) {})
        .build();
  }

  @PUT
  @Path("/{doctorId:\\d+}/attendinghours")
  @Consumes(VndType.APPLICATION_ATTENDING_HOURS_LIST)
  @PreAuthorize("@authorizationFunctions.isUser(authentication, #doctorId)")
  public Response updateAttendingHours(
      @PathParam("doctorId") final long doctorId,
      @Valid @HasAllDays(message = "HasAllDays.attendingHoursForm")
          final List<@Valid AttendingHoursForm> attendingHourForms)
      throws DoctorNotFoundException, EmailInUseException {

    LOGGER.debug("Updating attending hours for doctor with id {}", doctorId);

    Doctor doctor = doctorService.getDoctorById(doctorId).orElseThrow(DoctorNotFoundException::new);

    // Unwrap (day, List<ThirtyMinuteBlock>) to Set<AttendingHours>
    Set<AttendingHours> attendingHours =
        attendingHourForms.stream()
            .flatMap(
                dto ->
                    dto.getHours().stream()
                        .map(
                            hour ->
                                new AttendingHours(
                                    doctorId,
                                    DayOfWeek.valueOf(dto.getDay()),
                                    ThirtyMinuteBlock.fromBeginning(hour))))
            .collect(Collectors.toSet());

    // TODO: create a method in doctorService to update attending hours
    doctorService.updateDoctor(
        doctorId,
        doctor.getEmail(),
        doctor.getFirstName(),
        doctor.getLastName(),
        doctor.getSpecialty(),
        doctor.getCity(),
        doctor.getAddress(),
        doctor.getHealthInsurances(),
        attendingHours,
        doctor.getImage(),
        doctor.getLocale());

    LOGGER.debug("Updated attending hours for doctor with id {}", doctorId);

    return Response.noContent().build();
  }
}
