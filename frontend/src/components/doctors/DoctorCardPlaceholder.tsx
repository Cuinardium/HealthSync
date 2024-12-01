import React from "react";
import {
  Card,
  Placeholder,
  Stack,
  Badge,
  Button,
  Col,
  Row,
} from "react-bootstrap";

const DoctorCardPlaceholder: React.FC = () => {
  return (
    <Card className="mb-3 shadow-sm">
      <Card.Body>
        <Stack direction="horizontal" className="mb-3">
          <Placeholder as={Col} xs={2} animation="wave">
            <Placeholder
              className="rounded-circle me-3"
              style={{ width: "50px", height: "50px" }}
            />
          </Placeholder>
          <Col xs={6}>
            <Stack direction="vertical" gap={3}>
              <Placeholder as={Row} animation="wave">
                <Placeholder xs={6} />
              </Placeholder>
              <Placeholder as={Row} animation="wave">
                <Placeholder xs={8} className="text-muted" />
              </Placeholder>
            </Stack>
          </Col>
        </Stack>
        <Stack className="mb-3" direction="horizontal" gap={3}>
          <Placeholder as={Col} xs={2}>
            <Placeholder xs={4} animation="wave" />
          </Placeholder>
          <Placeholder as={Col} xs={2} animation="wave">
            <Placeholder xs={12} variant="primary" className="text-primary" />
          </Placeholder>
          <Placeholder as={Col} xs={2} animation="wave">
            <Placeholder xs={12} variant="primary" className="text-primary" />
          </Placeholder>
        </Stack>
        <Stack className="mb-3" direction="horizontal" gap={3}>
          <Placeholder as={Col} xs={2}>
            <Placeholder xs={4} animation="wave" />
          </Placeholder>
          <Placeholder as={Col} xs={3} animation="wave">
            <Placeholder xs={12} variant="primary" className="text-primary" />
          </Placeholder>
        </Stack>
        <Stack className="mb-3" direction="horizontal" gap={3}>
          <Placeholder as={Col} xs={3} animation="wave">
            <Placeholder xs={12} className="text-muted" />
          </Placeholder>
        </Stack>
        <div className="text-muted">
          <Placeholder xs={5} animation="wave" />
        </div>
        <div className="d-flex flex-row justify-content-end">
          <Placeholder as={Col} xs={3} className="ms-auto" animation="wave">
            <Placeholder.Button xs={12} variant="outline-primary" />
          </Placeholder>
        </div>
      </Card.Body>
    </Card>
  );
};

export default DoctorCardPlaceholder;
