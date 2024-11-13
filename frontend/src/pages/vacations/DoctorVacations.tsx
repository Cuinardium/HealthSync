import React, { useState } from "react";
import Loader from "../../components/Loader";
import VacationList from "../../components/vacations/VacationList";
import { useAuth } from "../../context/AuthContext";
import VacationForm from "../../components/vacations/VacationForm";

const DoctorVacations: React.FC = () => {
  const { loading, id } = useAuth();
  const [page, setPage] = useState(1);
  const pageSize = 3;

  if (loading) {
    return <Loader />;
  }

  return (
    <div>
      <h1>Vacations</h1>
      <VacationList
        doctorId={id as any}
        page={page}
        pageSize={pageSize}
        onPageChange={setPage}
      />
      {/* Create vacation form */}
      <h2>Create Vacation</h2>
      <VacationForm doctorId={id as any} />
    </div>
  );
};

export default DoctorVacations;
