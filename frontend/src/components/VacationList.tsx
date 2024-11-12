import React from "react";
import {
  useVacations,
  useDeleteVacation,
} from "../hooks/vacationHooks";

interface VacationPageProps {
  doctorId: string;
  page: number;
  pageSize: number;
  onPageChange: (page: number) => void;
}

const VacationList: React.FC<VacationPageProps> = ({
  doctorId,
  page,
  pageSize,
  onPageChange,
}) => {


  const {
    data: vacations,
    isLoading,
    error,
  } = useVacations(doctorId, { page, pageSize });

  const deleteVacationMutation = useDeleteVacation(doctorId);

  const handleDeleteVacation = (vacationId: string) => {
    // TODO
  };

  // TODO
  if (isLoading) return <div>Loading vacations...</div>;
  if (error) return <div>Error loading vacations: {error.message}</div>;

  if (!vacations || !vacations.content) return <div>No vacations found</div>;

  // TODO
  return (
    <div>
      {/* List of vacations */}
      <ul>
        {vacations.content.map((vacation) => (
          <li key={vacation.id}>
            <span>
              {vacation.fromDate.toISOString().split("T")[0]} - {vacation.fromTime}
                { " - "}  
              {vacation.toDate.toISOString().split("T")[0]} - {vacation.toTime}
            </span>
            <button onClick={() => handleDeleteVacation(vacation.id)}>
              Delete
            </button>
          </li>
        ))}
      </ul>

      {/* Pagination controls */}
      <div style={{ marginTop: "20px" }}>
        <button disabled={page === 1} onClick={() => onPageChange(page - 1)}>
          Previous
        </button>
        <span style={{ margin: "0 10px" }}>Page {page}</span>
        <button
          onClick={() => onPageChange(page + 1)}
          disabled={vacations.currentPage === vacations.totalPages}
        >
          Next
        </button>
      </div>

    </div>
  );
};

export default VacationList;
