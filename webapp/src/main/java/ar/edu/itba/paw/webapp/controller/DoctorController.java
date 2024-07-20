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
import ar.edu.itba.paw.webapp.form.DoctorRegisterForm;
import ar.edu.itba.paw.webapp.mediaType.VndType;
import ar.edu.itba.paw.webapp.query.DoctorQuery;
import ar.edu.itba.paw.webapp.utils.ResponseUtil;
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
import org.springframework.security.access.prepost.PreAuthorize;
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

  // ================= doctors ========================

  @GET
  @Produces(VndType.APPLICATION_DOCTOR_LIST)
  public Response listDoctors(@Valid @BeanParam final DoctorQuery doctorQuery) {

    LOGGER.debug("Listing doctors, query: {}", doctorQuery);

    Page<Doctor> doctors =
        doctorService.getFilteredDoctors(
            doctorQuery.getName(),
            doctorQuery.getLocalDate(),
            doctorQuery.getFromTimeEnum(),
            doctorQuery.getToTimeEnum(),
            doctorQuery.getSpecialtiesEnum(),
            doctorQuery.getCities(),
            doctorQuery.getHealthInsurancesEnum(),
            doctorQuery.getMinRating(),
            doctorQuery.getPage(),
            doctorQuery.getPageSize());

    if (doctors.getContent().isEmpty()) {
      LOGGER.debug("No doctors found for query: {}", doctorQuery);
      return Response.noContent().build();
    }

    final List<DoctorDto> dtoList =
        doctors.getContent().stream()
            .map(doctor -> DoctorDto.fromDoctor(uriInfo, doctor))
            .collect(Collectors.toList());

    LOGGER.debug("Returning doctors: {}", dtoList);

    return ResponseUtil.setPaginationLinks(
            Response.ok(new GenericEntity<List<DoctorDto>>(dtoList) {}), uriInfo, doctors)
        .build();
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
        uriInfo.getBaseUriBuilder().path("/doctors").path(String.valueOf(doctor.getId())).build();

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
