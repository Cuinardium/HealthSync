import { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import CancelAppointmentForm from "../../components/appointments/CancelAppointmentForm";
import IndicationList from "../../components/indications/IndicationList";
import { useAppointment } from "../../hooks/appointmentHooks";

const DetailedAppointment: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const [page, setPage] = useState(1);
  const pageSize = 3;

  const {
    data: appointment,
    isLoading,
    isError,
    error,
  } = useAppointment(id as string);

  if (!id || isNaN(+Number(id)) || Number(id) < 0) {
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

      {appointment.status === "CONFIRMED" && (
        <CancelAppointmentForm appointmentId={appointment.id.toString()} />
      )}

      <IndicationList
        appointmentId={id}
        page={page}
        pageSize={pageSize}
        onPageChange={setPage}
      />
    </div>
  );
};

export default DetailedAppointment;
