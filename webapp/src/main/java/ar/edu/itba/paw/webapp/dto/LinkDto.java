package ar.edu.itba.paw.webapp.dto;

import java.net.URI;

public class LinkDto {
  private URI href;
  private String rel;
  private String method;

  public static LinkDto fromUri(final URI uri, final String rel, final String method) {
    final LinkDto dto = new LinkDto();
    dto.href = uri;
    dto.rel = rel;
    dto.method = method;
    return dto;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public URI getHref() {
    return href;
  }

  public void setHref(URI href) {
    this.href = href;
  }

  public String getRel() {
    return rel;
  }

  public void setRel(String rel) {
    this.rel = rel;
  }
}
