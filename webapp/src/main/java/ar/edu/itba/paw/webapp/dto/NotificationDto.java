package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Notification;
import java.net.URI;
import javax.ws.rs.core.UriInfo;

public class NotificationDto {

  private long id;
  private long userId;
  private long appointmentId;

  private URI self;

  public static NotificationDto fromNotification(
      final UriInfo uri, final Notification notification) {
    final NotificationDto dto = new NotificationDto();

    dto.id = notification.getId();
    dto.userId = notification.getUser().getId();
    dto.appointmentId = notification.getAppointment().getId();

    dto.self = uri.getBaseUriBuilder().path("notifications").path(String.valueOf(dto.id)).build();

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

  public URI getSelf() {
    return self;
  }

  public void setSelf(URI self) {
    this.self = self;
  }
}
