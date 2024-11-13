import React, { useState } from "react";
import { Button, Col, Form, Row } from "react-bootstrap";
import { TIMES } from "../../api/time/Time";
import { VacationForm as VacationFormType } from "../../api/vacation/Vacation";
import { AxiosError } from "axios";
import { useCreateVacation } from "../../hooks/vacationHooks";

interface VacationFormProps {
  doctorId: string;
}

const VacationForm: React.FC<VacationFormProps> = ({ doctorId }) => {
  const [newVacation, setNewVacation] = useState<VacationFormType>({
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
  };

  const onError = (error: AxiosError) => {
    alert(`Failed to create vacation: ${error.message}`);
  };

  const createVacationMutation = useCreateVacation(
    doctorId,
    onSuccess,
    onError,
  );

  const handleCreateVacation = () => {
    createVacationMutation.mutate(newVacation);
  };

  return (
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

      {newVacation.cancelAppointments && (
        <Form.Group className="mb-3" controlId="reason">
          <Form.Label>Reason</Form.Label>
          <Form.Control
            type="text"
            placeholder="Enter reason"
            value={newVacation.cancelReason}
            disabled={!newVacation.cancelAppointments}
            onChange={(e) =>
              setNewVacation((prev) => ({
                ...prev,
                cancelReason: e.target.value,
              }))
            }
          />
        </Form.Group>
      )}
      <Button
        variant="primary"
        type="submit"
        disabled={createVacationMutation.isPending}
      >
        {createVacationMutation.isPending ? "Creating..." : "Create Vacation"}
      </Button>
    </Form>
  );
};

export default VacationForm;