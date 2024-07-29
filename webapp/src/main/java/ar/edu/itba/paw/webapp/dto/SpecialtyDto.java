package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.webapp.utils.LocaleUtil;
import java.net.URI;
import javax.ws.rs.core.UriInfo;
import org.springframework.context.MessageSource;

public class SpecialtyDto {

  private String name;
  private String code;
  private int popularity;

  private URI self;

  public static SpecialtyDto fromSpecialty(
      final UriInfo uriInfo,
      final MessageSource messageSource,
      final Specialty specialty,
      final int popularity) {

    final SpecialtyDto dto = new SpecialtyDto();

    dto.code = specialty.name();

    dto.name =
        messageSource.getMessage(
            specialty.getMessageID(), null, LocaleUtil.getCurrentRequestLocale());

    dto.popularity = popularity;

    dto.self =
        uriInfo
            .getBaseUriBuilder()
            .path("/specialties")
            .path(String.valueOf(specialty.ordinal()))
            .build();

    return dto;
  }

  public URI getSelf() {
    return self;
  }

  public void setSelf(URI self) {
    this.self = self;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public int getPopularity() {
    return popularity;
  }

  public void setPopularity(int popularity) {
    this.popularity = popularity;
  }
}
