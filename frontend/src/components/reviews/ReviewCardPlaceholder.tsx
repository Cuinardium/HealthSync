import React from "react";
import { Card, Placeholder, Stack, Col, Row } from "react-bootstrap";

const ReviewCardPlaceholder: React.FC = () => {
  return (
    <Card className="mb-3 shadow-sm">
      <Card.Body>
        {/* Placeholder for Rating */}
        <Placeholder as="div" animation="wave">
          <Placeholder
            xs={2}
            style={{ height: "15px", color: "gold" }}
            className="mb-3"
          />
        </Placeholder>

        {/* Placeholder for Description */}
        <Placeholder as="div" animation="wave">
          <Placeholder xs={10} style={{ height: "15px" }} className="mb-2" />
          <Placeholder xs={8} style={{ height: "15px" }} className="mb-2" />
          <Placeholder xs={7} style={{ height: "15px" }} />
        </Placeholder>

        {/* Placeholder for Patient Info */}
        <Stack direction="horizontal" className="mb-3 mt-4">
          <Placeholder as={Col} xs={1} animation="wave">
            <Placeholder
              className="rounded-circle me-3"
              style={{ width: "50px", height: "50px" }}
            />
          </Placeholder>
          <Col xs={6}>
            <Stack direction="vertical" gap={3}>
              <Placeholder as={Row} animation="wave">
                <Placeholder xs={3} />
              </Placeholder>
              <Placeholder as={Row} animation="wave">
                <Placeholder xs={2} className="text-muted" />
              </Placeholder>
            </Stack>
          </Col>
        </Stack>
      </Card.Body>
    </Card>
  );
};

export default ReviewCardPlaceholder;
