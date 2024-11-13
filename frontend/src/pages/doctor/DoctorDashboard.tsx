import React, { useState } from "react";
import DoctorList from "../../components/doctors/DoctorList";

const DoctorDashboard: React.FC = () => {
  const [page, setPage] = useState(1);
  const pageSize = 3;

  return (
    <div>
      <h1>Doctors</h1>
      <DoctorList
        page={page}
        pageSize={pageSize}
        onPageChange={setPage}
      />
    </div>
  );
};

export default DoctorDashboard;
