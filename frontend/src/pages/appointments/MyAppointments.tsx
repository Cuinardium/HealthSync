import React, { useState } from "react";
import AppointmentsList from "../../components/AppointmentList";
import Loader from "../../components/Loader";
import { useAuth } from "../../context/AuthContext";

const MyAppointments: React.FC = () => {
  const { loading, id } = useAuth();
  const [page, setPage] = useState(1);
  const pageSize = 3;

  if (loading) {
    return <Loader />;
  }

  return (
    <div>
      <h1>My Appointments</h1>
      <AppointmentsList
        userId={id as any}
        page={page}
        pageSize={pageSize}
        onPageChange={setPage}
      />
    </div>
  );
};

export default MyAppointments;
