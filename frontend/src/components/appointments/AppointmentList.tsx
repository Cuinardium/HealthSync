import React from "react";
import { useAppointments } from "../../hooks/appointmentHooks";
import { AppointmentQuery } from "../../api/appointment/Appointment";
import Loader from "../Loader";
import { Link } from "react-router-dom";
import { useNotifications } from "../../hooks/notificationHooks";

import "../../css/header.css";
import { FaCircle } from "react-icons/fa6";

interface AppointmentsListProps {
  userId: string;
  page?: number;
  pageSize?: number;
  status?: "CONFIRMED" | "CANCELLED" | "COMPLETED";
  onPageChange: (page: number) => void;
}

const AppointmentList: React.FC<AppointmentsListProps> = ({
  userId,
  page = 1,
  pageSize = 10,
  status,
  onPageChange,
}) => {
  const query: AppointmentQuery = {
    userId,
    page,
    pageSize,
    status,
  };

  const {
    data: appointmentsPage,
    isLoading,
    isError,
    error,
  } = useAppointments(query);

  const { data: notifications, isLoading: isLoadingNotifications } =
    useNotifications(userId);

  if (isLoading) {
    // TODO
    return <Loader />;
  }

  if (isError) {
    // TODO
    return <div>Error fetching appointments: {error?.message}</div>;
  }

  if (!appointmentsPage || appointmentsPage.content.length === 0) {
    // TODO
    return <div>No appointments found</div>;
  }

  // TODO: Hacerlo bien
  return (
    <div>
      <h2>Appointments</h2>
      <ul>
        {appointmentsPage.content.map((appointment) => (
          <li key={appointment.id}>
            <div>
              <strong>Date:</strong> {appointment.date.toLocaleDateString()}
            </div>
            <div>
              <strong>Time Block:</strong> {appointment.timeBlock}
            </div>
            <div>
              <strong>Status:</strong> {appointment.status}
            </div>
            <div>
              <strong>Doctor:</strong> {appointment.doctorId}
            </div>
            <div>
              <strong>Patient:</strong> {appointment.patientId}
            </div>
            <div>
              <strong>Description:</strong> {appointment.description}
            </div>
            {appointment.status === "CANCELLED" &&
              appointment.cancelDescription && (
                <div>
                  <strong>Cancel Reason:</strong>{" "}
                  {appointment.cancelDescription}
                </div>
              )}
            <div>
              <Link to={`/detailed-appointment/${appointment.id}`}>
                More details
              </Link>
              {notifications?.some(
                (notification) => notification.appointmentId === appointment.id,
              ) && <FaCircle className="notification" />}
            </div>
          </li>
        ))}
      </ul>

      {/* Pagination Controls */}
      <div style={{ marginTop: "20px" }}>
        <button disabled={page === 1} onClick={() => onPageChange(page - 1)}>
          Previous
        </button>
        <span style={{ margin: "0 10px" }}>Page {page}</span>
        <button
          onClick={() => onPageChange(page + 1)}
          disabled={
            appointmentsPage.currentPage === appointmentsPage.totalPages
          }
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default AppointmentList;
