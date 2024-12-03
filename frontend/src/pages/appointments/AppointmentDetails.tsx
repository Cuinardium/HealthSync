import React, { useEffect, useState } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import CancelAppointmentForm from "../../components/appointments/CancelAppointmentForm";
import IndicationList from "../../components/indications/IndicationList";
import { useAuth } from "../../context/AuthContext";
import { useAppointment } from "../../hooks/appointmentHooks";
import {
  useDeleteNotification,
  useNotifications,
} from "../../hooks/notificationHooks";
import { Alert, Breadcrumb, Col, Container } from "react-bootstrap";
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

  const { id, loading } = useAuth();

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

  useEffect(() => {
    if (!appointmentId || isNaN(Number(appointmentId)) || Number(appointmentId) < 0) {
      navigate("/404", { replace: true });
      return;
    }

    if (isError) {
      const status = error?.response?.status;
      if (status === 404 || status === 403) {
        navigate("/404", { replace: true });
      }
    }
  }, [appointmentId, isError, error, navigate]);

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
          {!isLoading && !loading && appointment && (
            <DetailedAppointmentCard
              appointment={appointment}
              hasNotification={false}
              onCancelClick={() => setShowCancelModal(true)}
              selected={showCancelModal}
            />
          )}
          {(isLoading || loading) && (
            <AppointmentCardPlaceholder isDoctor={false} showButtons={false} />
          )}
          {error && <Alert variant="danger">{t("appointment.error")}</Alert>}
        </div>

        {appointment?.canIndicate && appointmentId && (
          <>
            <h2 className="mb-3">{t("appointment.indication")}</h2>

            <IndicationList appointmentId={appointmentId} pageSize={pageSize} />
          </>
        )}
      </Col>

      {appointment?.canCancel && appointmentId && (
        <CancelAppointmentForm
          appointmentId={appointmentId}
          showCancelModal={showCancelModal}
          onHide={() => setShowCancelModal(false)}
        />
      )}
    </Container>
  );
};

export default DetailedAppointment;
