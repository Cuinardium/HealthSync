package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.HealthInsurance;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.UriInfo;

public class HealthInsuranceDto {
  private String healthInsuranceName;

  private URI self;

  public static HealthInsuranceDto fromHealthInsurance(
      final UriInfo uri, final HealthInsurance healthInsurance) {
    final HealthInsuranceDto dto = new HealthInsuranceDto();
    // TODO: translate this to i18n name
    dto.healthInsuranceName = healthInsurance.getMessageID();
    dto.self =
        uri.getBaseUriBuilder()
            .path("/healthinsurances")
            .path(String.valueOf(healthInsurance.ordinal()))
            .build();
    return dto;
  }

  public static List<HealthInsuranceDto> fromHealthInsuranceList(
      UriInfo uriInfo, List<HealthInsurance> healthInsuranceList) {
    return healthInsuranceList
        .stream()
        .map(h -> fromHealthInsurance(uriInfo, h))
        .collect(Collectors.toList());
  }

  public URI getSelf() {
    return self;
  }

  public void setSelf(URI self) {
    this.self = self;
  }

  public String getHealthInsuranceName() {
    return healthInsuranceName;
  }

  public void setHealthInsuranceName(String healthInsuranceName) {
    this.healthInsuranceName = healthInsuranceName;
  }
}
