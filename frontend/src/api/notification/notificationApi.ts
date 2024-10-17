import { axios } from "../axios";
import { Notification, NotificationQuery } from "./Notification";

const NOTIFICATION_ENDPOINT = "notifications";

const NOTIFICATION_CONTENT_TYPE = "application/vnd.notification.v1+json";
const NOTIFICATION_LIST_CONTENT_TYPE = "application/vnd.notifications-list.v1+json";

// =========== notifications ==============

export async function getNotifications(
  query: NotificationQuery,
): Promise<Notification[]> {
  const response = await axios.get<Notification[]>(NOTIFICATION_ENDPOINT, {
    params: query,
    headers: {
      Accept: NOTIFICATION_LIST_CONTENT_TYPE,
    },
  });

  return response.data;
}


// =========== notifications/{id} =======

export async function getNotification(id: String): Promise<Notification> {
  const response = await axios.get<Notification>(`${NOTIFICATION_ENDPOINT}/${id}`, {
    headers: {
      Accept: NOTIFICATION_CONTENT_TYPE,
    },
  });

  return response.data;
}

export async function deleteNotification(id: String): Promise<void> {
  await axios.delete(`${NOTIFICATION_ENDPOINT}/${id}`);
}

