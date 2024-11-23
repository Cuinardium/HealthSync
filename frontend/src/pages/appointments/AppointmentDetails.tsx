import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import CancelAppointmentForm from "../../components/appointments/CancelAppointmentForm";
import IndicationForm from "../../components/indications/IndicationForm";
import IndicationList from "../../components/indications/IndicationList";
import { useAuth } from "../../context/AuthContext";
import { useAppointment } from "../../hooks/appointmentHooks";
import { useDeleteNotification, useNotifications } from "../../hooks/notificationHooks";

const DetailedAppointment: React.FC = () => {
  const { id: appointmentId } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const [page, setPage] = useState(1);
  const pageSize = 3;

  const {
    id
  } = useAuth()

  const {
    data: appointment,
    isLoading,
    isError,
    error,
  } = useAppointment(appointmentId as string);

  const {
    data: notifications
  } = useNotifications(id as string);

  const deleteNotificationMutation = useDeleteNotification(() => {}, (_) => {})

  useEffect(() => {

    if (notifications && appointment) {
      const appointmentNotification = notifications.find((notification) => notification.appointmentId === appointment.id);

      if (appointmentNotification) {
        const id = String(appointmentNotification.id)
        deleteNotificationMutation.mutate(id);
      }
    }
  }, [notifications, appointment])

  if (!appointmentId || isNaN(+Number(appointmentId)) || Number(appointmentId) < 0) {
    navigate("/404");
    return null;
  }

  if (isLoading) {
    // TODO
    return <div>Loading...</div>;
  }

  if (isError) {
    return <div>{error?.message}</div>;
  }

  if (!appointment) {
    navigate("/404");
    return null;
  }

  return (
    <div>
      <h1>Appointment Details</h1>
      <p>
        <strong>Date:</strong> {appointment.date.toString()}
      </p>
      <p>
        <strong>Time:</strong> {appointment.timeBlock}
      </p>
      <p>
        <strong>Doctor:</strong> Dr. {appointment.doctorId}
      </p>
      <p>
        <strong>Patient:</strong> {appointment.patientId}
      </p>
      <p>
        <strong>Status:</strong> {appointment.status}
      </p>
      <p>
        <strong>Description:</strong> {appointment.description}
      </p>

      {appointment.canCancel && (
        <CancelAppointmentForm appointmentId={appointment.id.toString()} />
      )}

      {appointment.canIndicate && (
        <IndicationForm appointmentId={appointment.id.toString()} />
      )}

      <IndicationList
        appointmentId={appointmentId}
        page={page}
        pageSize={pageSize}
        onPageChange={setPage}
      />
    </div>
  );
};

export default DetailedAppointment;
