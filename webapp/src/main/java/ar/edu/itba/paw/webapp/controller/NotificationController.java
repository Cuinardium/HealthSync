package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.NotificationService;
import ar.edu.itba.paw.models.Notification;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.webapp.dto.NotificationDto;
import ar.edu.itba.paw.webapp.exceptions.NotificationNotFoundException;
import ar.edu.itba.paw.webapp.mediaType.VndType;
import ar.edu.itba.paw.webapp.query.PageQuery;
import ar.edu.itba.paw.webapp.query.UserQuery;
import ar.edu.itba.paw.webapp.utils.ResponseUtil;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
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
  @PreAuthorize("@authorizationFunctions.isUser(authentication, #userQuery.userId)")
  public Response getNotifications(
      @Valid @BeanParam final UserQuery userQuery, @Valid @BeanParam final PageQuery pageQuery) {

    LOGGER.debug("Getting notifications for user: {}", userQuery.getUserId());

    List<Notification> notifications =
        notificationService.getUserNotifications(userQuery.getUserId());

    if (notifications.isEmpty()) {
      return Response.noContent().build();
    }

    List<NotificationDto> notificationDtos =
        notifications.stream()
            .map(notification -> NotificationDto.fromNotification(uriInfo, notification))
            .collect(Collectors.toList());

    Page<NotificationDto> notificationPage =
        new Page<>(notificationDtos, pageQuery.getPage(), pageQuery.getPageSize());

    if (notificationPage.getContent().isEmpty()) {
      return Response.noContent().build();
    }

    return ResponseUtil.setPaginationLinks(
            Response.ok(new GenericEntity<List<NotificationDto>>(notificationPage.getContent()) {}),
            uriInfo,
            notificationPage)
        .build();
  }

  // ================= notifications/{notificationId} ===============

  @Path("{notificationId:\\d+}")
  @GET
  @Produces(VndType.APPLICATION_NOTIFICATION)
  @PreAuthorize("@authorizationFunctions.isNotificationRecipient(authentication, #notificationId)")
  public Response getNotification(@PathParam("notificationId") final Long notificationId) {

    LOGGER.debug("Getting notification with id: {}", notificationId);

    Notification notification =
        notificationService
            .getNotification(notificationId)
            .orElseThrow(NotificationNotFoundException::new);

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
