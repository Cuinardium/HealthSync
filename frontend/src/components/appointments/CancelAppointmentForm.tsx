import { AxiosError } from "axios";
import { useState } from "react";
import { Button, Col, Form, Row } from "react-bootstrap";
import { useCancelAppointment } from "../../hooks/appointmentHooks";

interface CancelAppointmentFormProps {
  appointmentId: string;
}

const CancelAppointmentForm: React.FC<CancelAppointmentFormProps> = ({ appointmentId }) => {
  const [cancelDescription, setCancelDescription] = useState<string>("");

  //TODO
  const onSuccess = () => {
    alert("Appointment cancelled succesfully");
  };

  const onError = (error: AxiosError) => {
    alert(`failed to cancel appointment: ${error.message}`);
  };

  const cancelAppointmentMutation = useCancelAppointment(
    appointmentId,
    onSuccess,
    onError,
  );

  const handleCancelReview = () => {
    cancelAppointmentMutation.mutate(cancelDescription);
  };

  return (
    <Form
      onSubmit={(e) => {
        e.preventDefault();
        handleCancelReview();
      }}
    >
      <Row className="mb-3">
        <Form.Group as={Col} controlId="description">
          <Form.Label>Reason</Form.Label>
          <Form.Control
            type="textarea"
            value={cancelDescription}
            onChange={(e) => setCancelDescription(e.target.value)}
          />
        </Form.Group>
      </Row>
      <Button
        variant="primary"
        type="submit"
        disabled={
          cancelDescription.length === 0 || cancelAppointmentMutation.isPending
        }
      >
        {cancelAppointmentMutation.isPending
          ? "Cancelling..."
          : "Cancel appointment"}
      </Button>
    </Form>
  );
};

export default CancelAppointmentForm;
