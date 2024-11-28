import React from "react";
import { useAppointments } from "../../hooks/appointmentHooks";
import { AppointmentQuery } from "../../api/appointment/Appointment";
import Loader from "../Loader";
import { useNotifications } from "../../hooks/notificationHooks";

import "../../css/header.css";
import { useTranslation } from "react-i18next";
import { Alert, Button, Spinner, Stack } from "react-bootstrap";
import AppointmentCard from "./AppointmentCard";

interface AppointmentsListProps {
  userId: string;
  pageSize?: number;
  status?: "CONFIRMED" | "CANCELLED" | "COMPLETED";
}

const AppointmentList: React.FC<AppointmentsListProps> = ({
  userId,
  pageSize = 10,
  status,
}) => {
  const query: AppointmentQuery = {
    userId,
    pageSize,
    status,
  };

  const {
    data: appointments,
    isLoading,
    error,
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage,
  } = useAppointments(query);

  const { data: notifications } = useNotifications(userId);

  const { t } = useTranslation();

  if (isLoading) {
    // TODO
    return <Loader />;
  }

  if (error) {
    return (
      <div
        className={"text-center d-flex flex-row justify-content-center mt-5"}
      >
        <Alert variant="danger">{t("appointment.error")}</Alert>
      </div>
    );
  }

  if (
    !appointments ||
    appointments.pages.length === 0 ||
    appointments.pages[0].content.length === 0
  ) {
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
        {appointments.pages.map((page, index) => (
          <React.Fragment key={index}>
            {page.content.map((appointment) => (
              <AppointmentCard
                appointment={appointment}
                hasNotification={
                  notifications?.some(
                    (n) => n.appointmentId === appointment.id,
                  ) ?? false
                }
              />
            ))}
          </React.Fragment>
        ))}
      </Stack>

      {/* Pagination Controls */}
      {/* Load more */}
      <div style={{ marginTop: "20px", textAlign: "center" }}>
        {hasNextPage && (
          <Button
            variant="outline-primary"
            onClick={() => fetchNextPage()}
            disabled={isFetchingNextPage}
          >
            {isFetchingNextPage ? (
              <>
                <Spinner
                  as="span"
                  animation="border"
                  size="sm"
                  role="status"
                  aria-hidden="true"
                />{" "}
                {t("appointments.loadingMore")}
              </>
            ) : (
              t("appointments.loadMore")
            )}
          </Button>
        )}
      </div>
    </div>
  );
};

export default AppointmentList;
