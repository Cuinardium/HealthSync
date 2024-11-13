package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.webapp.utils.LocaleUtil;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.UriInfo;

import ar.edu.itba.paw.webapp.utils.URIUtil;
import org.springframework.context.MessageSource;

public class HealthInsuranceDto {

  private String name;
  private String code;

  private List<LinkDto> links;

  public static HealthInsuranceDto fromHealthInsurance(
      final UriInfo uri, final MessageSource messageSource, final HealthInsurance healthInsurance) {

    final HealthInsuranceDto dto = new HealthInsuranceDto();

    dto.code = healthInsurance.name();

    dto.name =
        messageSource.getMessage(
            healthInsurance.getMessageID(), null, LocaleUtil.getCurrentRequestLocale());

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

  public List<LinkDto> getLinks() {
    return links;
  }

  public void setLinks(List<LinkDto> links) {
    this.links = links;
  }
}
