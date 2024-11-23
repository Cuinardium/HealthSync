import { axios } from "../axios";
import { Notification } from "./Notification";

const NOTIFICATION_ENDPOINT = "notifications";

const NOTIFICATION_CONTENT_TYPE = "application/vnd.notification.v1+json";
const NOTIFICATION_LIST_CONTENT_TYPE =
  "application/vnd.notifications-list.v1+json";

// =========== notifications ==============

export async function getNotifications(
  userId: string,
): Promise<Notification[]> {
  const allNotifications: Notification[] = [];
  let nextPageUrl: string | null = NOTIFICATION_ENDPOINT;

  const inititalQuery = {
    userId,
    pageSize: 100,
  };

  while (nextPageUrl) {
    const response = await axios.get<Notification[]>(NOTIFICATION_ENDPOINT, {
      params: inititalQuery,
      headers: {
        Accept: NOTIFICATION_LIST_CONTENT_TYPE,
      },
    });

    allNotifications.push(...response.data);

    const linkHeader: string = response.headers.link;
    nextPageUrl = linkHeader?.match(/<([^>]+)>;\s*rel="next"/)?.[1] || null;
  }

  return allNotifications;
}

// =========== notifications/{id} =======

export async function getNotification(id: String): Promise<Notification> {
  const response = await axios.get<Notification>(
    `${NOTIFICATION_ENDPOINT}/${id}`,
    {
      headers: {
        Accept: NOTIFICATION_CONTENT_TYPE,
      },
    },
  );

  return response.data;
}

export async function deleteNotification(id: String): Promise<void> {
  await axios.delete(`${NOTIFICATION_ENDPOINT}/${id}`);
}
