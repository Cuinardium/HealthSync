package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.webapp.utils.LocaleUtil;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.UriInfo;
import org.springframework.context.MessageSource;

public class HealthInsuranceDto {

  private String name;
  private String code;

  private URI self;

  public static HealthInsuranceDto fromHealthInsurance(
      final UriInfo uri, final MessageSource messageSource, final HealthInsurance healthInsurance) {

    final HealthInsuranceDto dto = new HealthInsuranceDto();

    dto.code = healthInsurance.name();

    dto.name =
        messageSource.getMessage(
            healthInsurance.getMessageID(), null, LocaleUtil.getCurrentRequestLocale());

    dto.self =
        uri.getBaseUriBuilder()
            .path("/healthinsurances")
            .path(String.valueOf(healthInsurance.ordinal()))
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
}
