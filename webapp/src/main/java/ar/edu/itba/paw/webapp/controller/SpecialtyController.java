package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.webapp.dto.ErrorDto;
import ar.edu.itba.paw.webapp.dto.SpecialtyDto;
import ar.edu.itba.paw.webapp.mediaType.VndType;
import ar.edu.itba.paw.webapp.query.SpecialtyQuery;
import ar.edu.itba.paw.webapp.utils.LocaleUtil;
import ar.edu.itba.paw.webapp.utils.ResponseUtil;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Path("specialties")
@Component
public class SpecialtyController {
  private static final Logger LOGGER = LoggerFactory.getLogger(SpecialtyController.class);

  private final MessageSource messageSource;
  private final DoctorService doctorService;

  @Context private UriInfo uriInfo;

  @Autowired
  public SpecialtyController(final MessageSource messageSource, final DoctorService doctorService) {
    this.messageSource = messageSource;
    this.doctorService = doctorService;
  }

  @GET
  @Produces(VndType.APPLICATION_SPECIALTY_LIST)
  public Response listSpecialties(@Valid @BeanParam SpecialtyQuery specialtyQuery) {

    LOGGER.debug("Listing specialties, page: {}", specialtyQuery.getPage());

    List<Specialty> specialties = Arrays.asList(Specialty.values());
    Map<Specialty, Integer> specialtiesPopularity = doctorService.getUsedSpecialties();

    // Compare by ordinal or by popularity
    Comparator<SpecialtyDto> comparator =
        specialtyQuery.sortByPopularity()
            ? Comparator.comparingInt(SpecialtyDto::getPopularity)
            : Comparator.comparingInt(s -> Specialty.valueOf(s.getCode()).ordinal());

    if (specialtyQuery.reversed()) {
      comparator = comparator.reversed();
    }

    // Merge both collections to the map, missing specialties will have a popularity of 0
    specialties.forEach(specialty -> specialtiesPopularity.putIfAbsent(specialty, 0));

    List<SpecialtyDto> specialtyDtoList =
        specialtiesPopularity.entrySet().stream()
            .map(
                entry ->
                    SpecialtyDto.fromSpecialty(
                        uriInfo, messageSource, entry.getKey(), entry.getValue()))
            .sorted(comparator)
            .collect(Collectors.toList());

    Page<SpecialtyDto> specialtyPage =
        new Page<>(specialtyDtoList, specialtyQuery.getPage(), specialtyQuery.getPageSize());

    if (specialtyPage.getContent().isEmpty()) {
      return Response.noContent().build();
    }

    return ResponseUtil.setPaginationLinks(
            Response.ok(new GenericEntity<List<SpecialtyDto>>(specialtyPage.getContent()) {}),
            uriInfo,
            specialtyPage)
        .build();
  }

  // ------------ specialties/{id} ------------

  @GET
  @Path("/{id:\\d+}")
  @Produces(VndType.APPLICATION_SPECIALTY)
  public Response getSpecialty(@PathParam("id") final int id) {

    Specialty[] specialties = Specialty.values();
    if (id < 0 || id >= specialties.length) {
      String message =
          messageSource.getMessage(
              "error.specialtyNotFound", null, LocaleUtil.getCurrentRequestLocale());

      return Response.status(Response.Status.NOT_FOUND)
          .entity(ErrorDto.fromMessage(message))
          .build();
    }

    Specialty specialty = specialties[id];
    int popularity = doctorService.getUsedSpecialties().getOrDefault(specialty, 0);

    LOGGER.debug("returning specialty with id {}", id);

    return Response.ok(SpecialtyDto.fromSpecialty(uriInfo, messageSource, specialty, popularity))
        .build();
  }
}
