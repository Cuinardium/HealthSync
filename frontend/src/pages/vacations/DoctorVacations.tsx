import { useState } from "react";
import { VacationForm } from "../../api/vacation/Vacation";
import Loader from "../../components/Loader";
import VacationList from "../../components/VacationList";
import { useAuth } from "../../context/AuthContext";
import { useCreateVacation } from "../../hooks/vacationHooks";

const DoctorVacations: React.FC = () => {
  const { loading, id } = useAuth();
  const [page, setPage] = useState(1);
  const pageSize = 3;

  const createVacationMutation = useCreateVacation(id as any);

  const [newVacation, setNewVacation] = useState<VacationForm>({
    fromDate: new Date(),
    toDate: new Date(),
    fromTime: "",
    toTime: "",
    cancelReason: undefined,
    cancelAppointments: false,
  });

  const handleCreateVacation = () => {
    // TODO
  };

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
      <form
        onSubmit={(e) => {
          e.preventDefault();
          handleCreateVacation();
        }}
      >
        <div>
          <label>From Date:</label>
          <input
            type="date"
            value={newVacation.fromDate.toISOString().split("T")[0]}
            onChange={(e) =>
              setNewVacation((prev) => ({
                ...prev,
                fromDate: new Date(e.target.value),
              }))
            }
          />
        </div>
        <div>
          <label>To Date:</label>
          <input
            type="date"
            value={newVacation.toDate.toISOString().split("T")[0]}
            onChange={(e) =>
              setNewVacation((prev) => ({
                ...prev,
                toDate: new Date(e.target.value),
              }))
            }
          />
        </div>
        <div>
          <label>From Time:</label>
          <input
            type="time"
            value={newVacation.fromTime}
            onChange={(e) =>
              setNewVacation((prev) => ({ ...prev, fromTime: e.target.value }))
            }
          />
        </div>

        <div>
          <label>To Time:</label>
          <input
            type="time"
            value={newVacation.toTime}
            onChange={(e) =>
              setNewVacation((prev) => ({ ...prev, toTime: e.target.value }))
            }
          />
        </div>

        <div>
          <label>Cancel Appointments:</label>
          <input
            type="checkbox"
            checked={newVacation.cancelAppointments}
            onChange={(e) =>
              setNewVacation((prev) => ({
                ...prev,
                cancelAppointments: e.target.checked,
              }))
            }
          />
        </div>

        <div>
          <label>Reason:</label>
          <input
            type="text"
            value={newVacation.cancelReason}
            disabled={!newVacation.cancelAppointments}
            onChange={(e) =>
              setNewVacation((prev) => ({ ...prev, reason: e.target.value }))
            }
          />
        </div>
        <button type="submit" disabled={createVacationMutation.isPending}>
          {createVacationMutation.isPending ? "Creating..." : "Create Vacation"}
        </button>
      </form>
    </div>
  );
};

export default DoctorVacations;
