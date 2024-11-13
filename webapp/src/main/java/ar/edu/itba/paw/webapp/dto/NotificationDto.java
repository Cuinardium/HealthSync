package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Notification;
import ar.edu.itba.paw.webapp.utils.URIUtil;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.UriInfo;

public class NotificationDto {

  private long id;
  private long userId;
  private long appointmentId;

  private List<LinkDto> links;

  public static NotificationDto fromNotification(
      final UriInfo uri, final Notification notification) {
    final NotificationDto dto = new NotificationDto();

    dto.id = notification.getId();
    dto.userId = notification.getUser().getId();
    dto.appointmentId = notification.getAppointment().getId();

    List<LinkDto> links = new ArrayList<>();
    URI selfURI = URIUtil.getNotificationURI(uri, notification.getId());
    links.add(LinkDto.fromUri(selfURI, "self", HttpMethod.GET));
    links.add(LinkDto.fromUri(selfURI, "delete-self", HttpMethod.DELETE));

    dto.links = links;

    return dto;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public long getAppointmentId() {
    return appointmentId;
  }

  public void setAppointmentId(long appointmentId) {
    this.appointmentId = appointmentId;
  }

  public List<LinkDto> getLinks() {
    return links;
  }

  public void setLinks(List<LinkDto> links) {
    this.links = links;
  }
}
