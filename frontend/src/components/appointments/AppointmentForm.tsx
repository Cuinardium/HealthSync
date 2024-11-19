import { AxiosError } from "axios";
import { useState } from "react";
import { useCreateAppointment } from "../../hooks/appointmentHooks";
import { AppointmentForm as AppointmentFormType } from "../../api/appointment/Appointment";
import { TIMES } from "../../api/time/Time";
import { Button, Col, Form, Row } from "react-bootstrap";

interface AppointmentFormProps {
  doctorId: string;
}

const AppointmentForm: React.FC<AppointmentFormProps> = ({ doctorId }) => {
  const [newAppointment, setNewAppointment] = useState<AppointmentFormType>({
    date: new Date(),
    timeBlock: "",
    description: "",
    doctorId: Number(doctorId),
  });

  // TODO
  const onSuccess = () => {
    alert("Appointment created succesfully");
  };

  const onError = (error: AxiosError) => {
    alert(
      `Error creating appointment: ${error.message}, ${JSON.stringify(error.response?.data)}`,
    );
  };

  const appointmentMutation = useCreateAppointment(onSuccess, onError);

  const handleCreateAppointment = () => {
    appointmentMutation.mutate(newAppointment);
  };

  return (
    <Form
      onSubmit={(e) => {
        e.preventDefault();
        handleCreateAppointment();
      }}
    >
      <Row className="mb-3">
        <Form.Group as={Col} controlId="date">
          <Form.Label>Date</Form.Label>
          <Form.Control
            type="date"
            value={newAppointment.date.toISOString().split("T")[0]}
            onChange={(e) =>
              setNewAppointment((prev) => ({
                ...prev,
                date: new Date(e.target.value),
              }))
            }
          />
        </Form.Group>
      </Row>

      <Row className="mb-3">
        <Form.Group as={Col} controlId="time">
          <Form.Label>Time</Form.Label>
          <Form.Control
            as="select"
            value={newAppointment.timeBlock}
            onChange={(e) =>
              setNewAppointment((prev) => ({
                ...prev,
                timeBlock: e.target.value,
              }))
            }
          >
            <option value="">Time</option>
            {TIMES.map((time) => (
              <option key={time} value={time}>
                {time}
              </option>
            ))}
          </Form.Control>
        </Form.Group>
      </Row>

      <Form.Group className="mb-3" controlId="description">
        <Form.Label>Description</Form.Label>
        <Form.Control
          type="text"
          placeholder="Enter reason"
          value={newAppointment.description}
          onChange={(e) =>
            setNewAppointment((prev) => ({
              ...prev,
              description: e.target.value,
            }))
          }
        />
      </Form.Group>
      <Button
        variant="primary"
        type="submit"
        disabled={appointmentMutation.isPending}
      >
        {appointmentMutation.isPending ? "Creating..." : "Create Appointment"}
      </Button>
    </Form>
  );
};

export default AppointmentForm;
