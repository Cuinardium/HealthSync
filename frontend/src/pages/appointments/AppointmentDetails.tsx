import React, { useEffect, useState } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import CancelAppointmentForm from "../../components/appointments/CancelAppointmentForm";
import IndicationForm from "../../components/indications/IndicationForm";
import IndicationList from "../../components/indications/IndicationList";
import { useAuth } from "../../context/AuthContext";
import { useAppointment } from "../../hooks/appointmentHooks";
import {
  useDeleteNotification,
  useNotifications,
} from "../../hooks/notificationHooks";
import { Breadcrumb, Col, Container } from "react-bootstrap";
import { useTranslation } from "react-i18next";
import DetailedAppointmentCard from "../../components/appointments/DetailedAppointmentCard";
import AppointmentCardPlaceholder from "../../components/appointments/AppointmentCardPlaceholder";

const DetailedAppointment: React.FC = () => {
  const { id: appointmentId } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { t } = useTranslation();

  const [deleteCalled, setDeleteCalled] = useState(false);
  const [showCancelModal, setShowCancelModal] = useState(false);
  const pageSize = 10;

  const { id } = useAuth();

  const {
    data: appointment,
    isLoading,
    isError,
    error,
  } = useAppointment(appointmentId as string);

  const { data: notifications } = useNotifications(id as string);

  const deleteNotificationMutation = useDeleteNotification(
    () => {},
    (_) => {},
  );

  useEffect(() => {
    if (!deleteCalled) {
      if (notifications && appointment) {
        const appointmentNotification = notifications.find(
          (notification) => notification.appointmentId === appointment.id,
        );

        if (appointmentNotification) {
          const id = String(appointmentNotification.id);
          deleteNotificationMutation.mutate(id);
        }

        setDeleteCalled(true);
      }
    }
  }, [notifications, appointment, deleteNotificationMutation, deleteCalled]);

  if (
    !appointmentId ||
    isNaN(+Number(appointmentId)) ||
    Number(appointmentId) < 0
  ) {
    navigate("/404");
    return null;
  }

  if (isError) {
    return <div>{error?.message}</div>;
  }

  return (
    <Container className="d-flex justify-content-center align-items-center mt-5 mb-5">
      <Col md={8} lg={9}>
        <Breadcrumb>
          <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/my-appointments" }}>
            {t("home.myAppointments")}
          </Breadcrumb.Item>
          <Breadcrumb.Item active>
            {t("detailedAppointment.title")}
          </Breadcrumb.Item>
        </Breadcrumb>
        <h1 className="mb-3">{t("detailedAppointment.title")}</h1>

        <div className="mb-3">
          {!isLoading && appointment && (
            <DetailedAppointmentCard
              appointment={appointment}
              hasNotification={false}
              onCancelClick={() => setShowCancelModal(true)}
              selected={showCancelModal}
            />
          )}
          {isLoading && (
            <AppointmentCardPlaceholder isDoctor={false} showButtons={false} />
          )}
        </div>

        {appointment?.canIndicate && (
          <>
            <h2 className="mb-3">{t("appointment.indication")}</h2>

            <IndicationList
              appointmentId={appointmentId}
              pageSize={pageSize}
            />
          </>
        )}
      </Col>
    </Container>
  );
};

export default DetailedAppointment;
