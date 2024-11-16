import { useState } from "react";
import { Button, Col, Form, Row } from "react-bootstrap";
import { useCreateIndication } from "../../hooks/indicationHooks";
import { AxiosError } from "axios";

interface CreateIndicationFormProps {
  appointmentId: string;
}

const IndicationForm: React.FC<CreateIndicationFormProps> = ({
  appointmentId,
}) => {
  const [description, setDescription] = useState<string>("");
  const [file, setFile] = useState<File | null>(null);

  const onSuccess = () => {
    alert("Indication created successfully");
  };

  const onError = (error: AxiosError) => {
    alert(`Failed to create indication: ${error.message}`);
  };

  const createIndicationMutation = useCreateIndication(
    appointmentId,
    onSuccess,
    onError,
  );

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const selectedFile = e.target.files?.[0];
    if (selectedFile) {
      setFile(selectedFile);
    }
  };

  const handleSubmit = () => {
    const indicationData = {
      indications: description,
      file: file || undefined,
    };
    createIndicationMutation.mutate(indicationData);
  };

  return (
    <Form
      onSubmit={(e) => {
        e.preventDefault();
        handleSubmit();
      }}
    >
      <Row className="mb-3">
        <Form.Group as={Col} controlId="description">
          <Form.Label>Description</Form.Label>
          <Form.Control
            as="textarea"
            rows={3}
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
        </Form.Group>
      </Row>

      <Row className="mb-3">
        <Form.Group as={Col} controlId="file">
          <Form.Label>Attach a File (optional)</Form.Label>
          <Form.Control type="file" onChange={handleFileChange} />
        </Form.Group>
      </Row>

      <Button
        variant="primary"
        type="submit"
        disabled={
          createIndicationMutation.isPending || description.trim().length === 0
        }
      >
        {createIndicationMutation.isPending
          ? "Creating..."
          : "Create Indication"}
      </Button>
    </Form>
  );
};

export default IndicationForm;
