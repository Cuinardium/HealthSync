package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.webapp.utils.LocaleUtil;
import ar.edu.itba.paw.webapp.utils.URIUtil;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.UriInfo;
import org.springframework.context.MessageSource;

public class HealthInsuranceDto {

  private String name;
  private String code;
  private int popularity;

  private List<LinkDto> links;

  public static HealthInsuranceDto fromHealthInsurance(
      final UriInfo uri,
      final MessageSource messageSource,
      final HealthInsurance healthInsurance,
      final int popularity) {

    final HealthInsuranceDto dto = new HealthInsuranceDto();

    dto.code = healthInsurance.name();

    dto.name =
        messageSource.getMessage(
            healthInsurance.getMessageID(), null, LocaleUtil.getCurrentRequestLocale());

    dto.popularity = popularity;

    URI selfURI = URIUtil.getHealthInsuranceURI(uri, healthInsurance.ordinal());

    dto.links = new ArrayList<>(1);
    dto.links.add(LinkDto.fromUri(selfURI, "self", "GET"));

    return dto;
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

  public List<LinkDto> getLinks() {
    return links;
  }

  public void setLinks(List<LinkDto> links) {
    this.links = links;
  }
}
