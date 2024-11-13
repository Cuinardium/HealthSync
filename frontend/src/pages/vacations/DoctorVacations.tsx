import { AxiosError } from "axios";
import { useState } from "react";
import { Button, Col, Form, Row } from "react-bootstrap";
import { TIMES } from "../../api/time/Time";
import { VacationForm } from "../../api/vacation/Vacation";
import Loader from "../../components/Loader";
import VacationList from "../../components/vacations/VacationList";
import { useAuth } from "../../context/AuthContext";
import { useCreateVacation } from "../../hooks/vacationHooks";

const DoctorVacations: React.FC = () => {
  const { loading, id } = useAuth();
  const [page, setPage] = useState(1);
  const pageSize = 3;


  const [newVacation, setNewVacation] = useState<VacationForm>({
    fromDate: new Date(),
    toDate: new Date(),
    fromTime: "",
    toTime: "",
    cancelReason: undefined,
    cancelAppointments: false,
  });

  // TODO
  const onSuccess = () => {
    alert("Vacation created successfully");
  }

  const onError = (error: AxiosError) => {
    alert(`Failed to create vacation: ${error.message}`);
  }


  const createVacationMutation = useCreateVacation(id as any, onSuccess, onError);

  const handleCreateVacation = () => {
    createVacationMutation.mutate(newVacation)
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
      <Form
        onSubmit={(e) => {
          e.preventDefault();
          handleCreateVacation();
        }}
      >
        <Row className="mb-3">
          <Form.Group as={Col} controlId="fromDate">
            <Form.Label>From Date</Form.Label>
            <Form.Control
              type="date"
              value={newVacation.fromDate.toISOString().split("T")[0]}
              onChange={(e) =>
                setNewVacation((prev) => ({
                  ...prev,
                  fromDate: new Date(e.target.value),
                }))
              }
            />
          </Form.Group>
          <Form.Group as={Col} controlId="toDate">
            <Form.Label>To Date</Form.Label>
            <Form.Control
              type="date"
              value={newVacation.toDate.toISOString().split("T")[0]}
              onChange={(e) =>
                setNewVacation((prev) => ({
                  ...prev,
                  toDate: new Date(e.target.value),
                }))
              }
            />
          </Form.Group>
        </Row>

        <Row className="mb-3">
          <Form.Group as={Col} controlId="fromTime">
            <Form.Label>From Time</Form.Label>
            <Form.Control
              as="select"
              value={newVacation.fromTime}
              onChange={(e) =>
                setNewVacation((prev) => ({
                  ...prev,
                  fromTime: e.target.value,
                }))
              }
            >
              <option value="">from time</option>
              {TIMES.map((time) => (
                <option key={time} value={time}>
                  {time}
                </option>
              ))}
            </Form.Control>
          </Form.Group>
          <Form.Group as={Col} controlId="toTime">
            <Form.Label>To Time</Form.Label>
            <Form.Control
              as="select"
              value={newVacation.toTime}
              onChange={(e) =>
                setNewVacation((prev) => ({ ...prev, toTime: e.target.value }))
              }
            >
              <option value="">to time</option>
              {TIMES.map((time) => (
                <option key={time} value={time}>
                  {time}
                </option>
              ))}
            </Form.Control>
          </Form.Group>
        </Row>

        <Form.Group className="mb-3" controlId="cancelAppointments">
          <Form.Check
            type="checkbox"
            label="Cancel Appointments"
            checked={newVacation.cancelAppointments}
            onChange={(e) =>
              setNewVacation((prev) => ({
                ...prev,
                cancelAppointments: e.target.checked,
              }))
            }
          />
        </Form.Group>

        { newVacation.cancelAppointments &&
        <Form.Group className="mb-3" controlId="reason">
          <Form.Label>Reason</Form.Label>
          <Form.Control
            type="text"
            placeholder="Enter reason"
            value={newVacation.cancelReason}
            disabled={!newVacation.cancelAppointments}
            onChange={(e) =>
              setNewVacation((prev) => ({ ...prev, cancelReason: e.target.value }))
            }
          />
        </Form.Group>
        }
        <Button
          variant="primary"
          type="submit"
          disabled={createVacationMutation.isPending}
        >
          {createVacationMutation.isPending ? "Creating..." : "Create Vacation"}
        </Button>
      </Form>{" "}
    </div>
  );
};

export default DoctorVacations;
