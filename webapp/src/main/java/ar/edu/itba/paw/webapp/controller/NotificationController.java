package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.NotificationService;
import ar.edu.itba.paw.models.Notification;
import ar.edu.itba.paw.webapp.dto.NotificationDto;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import ar.edu.itba.paw.webapp.mediaType.VndType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Path("notifications")
@Component
public class NotificationController {

  private static final Logger LOGGER = LoggerFactory.getLogger(IndicationController.class);

  private final NotificationService notificationService;

  @Context private UriInfo uriInfo;

  @Autowired
  public NotificationController(final NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  // ================= notifications ===============

  @GET
  @Produces(VndType.APPLICATION_NOTIFICATIONS_LIST)
  @PreAuthorize("@authorizationFunctions.isUser(authentication, #userId)")
  public Response getNotifications(@NotNull @QueryParam("userId") final Long userId) {

    LOGGER.debug("Getting notifications for user: {}", userId);

    List<Notification> notifications = notificationService.getUserNotifications(userId);

    if (notifications.isEmpty()) {
      return Response.noContent().build();
    }

    List<NotificationDto> notificationDtos =
        notifications.stream()
            .map(notification -> NotificationDto.fromNotification(uriInfo, notification))
            .collect(Collectors.toList());

    return Response.ok(new GenericEntity<List<NotificationDto>>(notificationDtos) {}).build();
  }

  // ================= notifications/{notificationId} ===============

  @Path("{notificationId:\\d+}")
  @GET
  @Produces(VndType.APPLICATION_NOTIFICATION)
  @PreAuthorize("@authorizationFunctions.isNotificationRecipient(authentication, #notificationId)")
  public Response getNotification(@PathParam("notificationId") final Long notificationId) {

    LOGGER.debug("Getting notification with id: {}", notificationId);

    Notification notification = notificationService.getNotification(notificationId).orElse(null);

    if (notification == null) {
      LOGGER.debug("Notification not found: {}", notificationId);
      return Response.status(Response.Status.NOT_FOUND).entity("Notification not found.").build();
    }

    final NotificationDto notificationDto = NotificationDto.fromNotification(uriInfo, notification);

    return Response.ok(notificationDto).build();
  }

  @Path("{notificationId:\\d+}")
  @DELETE
  @PreAuthorize("@authorizationFunctions.isNotificationRecipient(authentication, #notificationId)")
  public Response deleteNotification(@PathParam("notificationId") final Long notificationId) {

    LOGGER.debug("Deleting notification with id: {}", notificationId);

    Notification notification = notificationService.getNotification(notificationId).orElse(null);

    if (notification == null) {
      LOGGER.debug("Notification not found: {}", notificationId);
      return Response.noContent().build();
    }

    notificationService.deleteNotification(notification);

    return Response.noContent().build();
  }
}
