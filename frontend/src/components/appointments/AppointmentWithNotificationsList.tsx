import { useAppointmentsFromNotifications } from "../../hooks/appointmentHooks";
import { useTranslation } from "react-i18next";
import { Alert, Stack } from "react-bootstrap";
import React from "react";
import AppointmentCard from "./AppointmentCard";
import AppointmentCardPlaceholder from "./AppointmentCardPlaceholder";

interface AppointmentWithNotificationsListProps {
  userId: string;
  isDoctor: boolean;
}

const AppointmentWithNotificationsList: React.FC<
  AppointmentWithNotificationsListProps
> = ({ userId, isDoctor }) => {
  const { appointments, isLoading, error } =
    useAppointmentsFromNotifications(userId);

  const { t } = useTranslation();

  if (isLoading) {
    return (
        <Stack direction="vertical" gap={3}>
          {[...Array(10)].map((_, index) => (
              <AppointmentCardPlaceholder
                  key={index}
                  isDoctor={isDoctor}
                  status="CONFIRMED"
              />
          ))}
        </Stack>
    );;
  }

  if (error) {
    return (
        <div
            className={"text-center d-flex flex-row justify-content-center mt-5"}
        >
          <Alert variant="danger">{t("appointments.error")}</Alert>
        </div>
    );
  }

  if (!appointments || appointments.length === 0) {
    return (
      <div
        className={"text-center d-flex flex-row justify-content-center mt-5"}
      >
        <Alert variant="info">{t("appointments.noNotifications")}</Alert>
      </div>
    );
  }

  return (
    <div>
      <Stack direction="vertical" gap={3}>
        {appointments.map((appointment) => (
          <AppointmentCard appointment={appointment} hasNotification={true} />
        ))}
      </Stack>
    </div>
  );
};

export default AppointmentWithNotificationsList;
