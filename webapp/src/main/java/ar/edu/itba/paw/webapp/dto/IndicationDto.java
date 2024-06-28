package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Indication;

import java.net.URI;
import java.time.LocalDate;

import javax.ws.rs.core.UriInfo;

public class IndicationDto {

  // Properties
  private Long id;
  private LocalDate date;
  private String description;

  // Links
  private URI appointment;
  private URI creator;
  private URI file;
  private URI self;

  public static IndicationDto fromIndication(final UriInfo uri, final Indication indication) {
    final IndicationDto dto = new IndicationDto();

    // Properties
    dto.id = indication.getId();
    dto.date = indication.getDate();
    dto.description = indication.getDescription();

    // Links
    dto.appointment =
        uri.getBaseUriBuilder()
            .path("/appointments")
            .path(String.valueOf(indication.getAppointment().getId()))
            .build();
    dto.creator =
        uri.getBaseUriBuilder()
            .path("/doctors")
            .path(String.valueOf(indication.getUser().getId()))
            .build();
    dto.file =
        uri.getBaseUriBuilder()
            .path("/indications")
            .path(String.valueOf(indication.getId()))
            .path("/file")
            .build();
    dto.self =
        uri.getBaseUriBuilder()
            .path("/indications")
            .path(String.valueOf(indication.getId()))
            .build();

    return dto;
  }


}
