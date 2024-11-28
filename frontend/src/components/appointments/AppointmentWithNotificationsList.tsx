import { useAppointmentsFromNotifications } from "../../hooks/appointmentHooks";
import { useTranslation } from "react-i18next";
import { Alert, Stack } from "react-bootstrap";
import React from "react";
import AppointmentCard from "./AppointmentCard";

interface AppointmentWithNotificationsListProps {
  userId: string;
}

const AppointmentWithNotificationsList: React.FC<
  AppointmentWithNotificationsListProps
> = ({ userId }) => {
  const { appointments, isLoading, error } =
    useAppointmentsFromNotifications(userId);

  const { t } = useTranslation();

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error</div>;
  }

  if (!appointments || appointments.length === 0) {
    return (
      <div
        className={"text-center d-flex flex-row justify-content-center mt-5"}
      >
        <Alert variant="info">{t("appointments.noAppointments")}</Alert>
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
