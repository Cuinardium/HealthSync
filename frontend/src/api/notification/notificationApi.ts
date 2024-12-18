import { axios } from "../axios";
import { Notification } from "./Notification";
import { fetchAllPaginatedData } from "../page/Page";

const NOTIFICATION_ENDPOINT = "notifications";

const NOTIFICATION_CONTENT_TYPE = "application/vnd.notification.v1+json";
const NOTIFICATION_LIST_CONTENT_TYPE =
  "application/vnd.notifications-list.v1+json";

// =========== notifications ==============

export async function getNotifications(
  userId: string,
): Promise<Notification[]> {
  const initialQuery = {
    userId,
    pageSize: 50,
  };

  return fetchAllPaginatedData<Notification>(
    NOTIFICATION_ENDPOINT,
    initialQuery,
    { Accept: NOTIFICATION_LIST_CONTENT_TYPE },
  );
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
