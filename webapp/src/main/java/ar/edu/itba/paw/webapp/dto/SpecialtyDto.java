package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.webapp.utils.LocaleUtil;
import ar.edu.itba.paw.webapp.utils.URIUtil;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.UriInfo;
import org.springframework.context.MessageSource;

public class SpecialtyDto {

  private String name;
  private String code;
  private int popularity;

  private List<LinkDto> links;

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

    URI selfURI = URIUtil.getSpecialtyURI(uriInfo, specialty.ordinal());
    dto.links = new ArrayList<>(1);
    dto.links.add(LinkDto.fromUri(selfURI, "self", HttpMethod.GET));

    return dto;
  }

  public List<LinkDto> getLinks() {
    return links;
  }

  public void setLinks(List<LinkDto> links) {
    this.links = links;
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
