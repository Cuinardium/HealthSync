import React, { useCallback, useState } from "react";
import { useAppointments } from "../../hooks/appointmentHooks";
import { AppointmentQuery } from "../../api/appointment/Appointment";
import { useNotifications } from "../../hooks/notificationHooks";

import "../../css/header.css";
import { useTranslation } from "react-i18next";
import { Alert, Button, Spinner, Stack } from "react-bootstrap";
import AppointmentCard from "./AppointmentCard";
import CancelAppointmentForm from "./CancelAppointmentForm";
import AppointmentCardPlaceholder from "./AppointmentCardPlaceholder";

interface AppointmentsListProps {
  userId: string;
  isDoctor: boolean;
  pageSize?: number;
  status?: "CONFIRMED" | "CANCELLED" | "COMPLETED";
  order?: "asc" | "desc",
  date?: Date;
}

const AppointmentList: React.FC<AppointmentsListProps> = ({
  userId,
  pageSize = 10,
  status,
  isDoctor,
  order,
  date
}) => {

  const query: AppointmentQuery = {
    userId,
    pageSize,
    status,
    order,
    from: date,
    to: date,
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

  const [showCancelModal, setShowCancelModal] = useState(false);
  const [appointmentIdToCancel, setAppointmentIdToCancel] = useState<
    number | null
  >(null);

  const selected = useCallback(
    (id: number) => {
      return id === appointmentIdToCancel;
    },
    [appointmentIdToCancel],
  );

  if (isLoading) {
    return (
      <Stack direction="vertical" gap={3}>
        {[...Array(pageSize)].map((_, index) => (
          <AppointmentCardPlaceholder
            key={index}
            isDoctor={isDoctor}
            status={status ?? "CONFIRMED"}
          />
        ))}
      </Stack>
    );
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
                key={appointment.id}
                appointment={appointment}
                hasNotification={
                  notifications?.some(
                    (n) => n.appointmentId === appointment.id,
                  ) ?? false
                }
                onCancelClick={(appointmentId) => {
                  setAppointmentIdToCancel(appointmentId);
                  setShowCancelModal(true);
                }}
                selected={selected(appointment.id)}
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

      {/* Cancel Appointment Modal */}
      <CancelAppointmentForm
        appointmentId={String(appointmentIdToCancel)}
        showCancelModal={showCancelModal}
        onHide={() => {
          setAppointmentIdToCancel(null);
          setShowCancelModal(false);
        }}
      />
    </div>
  );
};

export default AppointmentList;
