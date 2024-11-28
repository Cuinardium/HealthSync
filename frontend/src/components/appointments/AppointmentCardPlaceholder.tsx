import React from "react";
import { Card, Stack, Placeholder, Button, Badge, Col } from "react-bootstrap";

interface AppointmentCardPlaceholderProps {
  status: "CONFIRMED" | "CANCELLED" | "COMPLETED";
  isDoctor: boolean;
}

const AppointmentCardPlaceholder: React.FC<AppointmentCardPlaceholderProps> = ({
  status,
  isDoctor,
}) => {
  const color =
    status === "CANCELLED"
      ? "danger"
      : status === "COMPLETED"
        ? "success"
        : "primary";

  return (
    <Card className="mb-3 shadow-sm" border={color}>
      <Card.Header>
        <div className="d-flex flex-row justify-content-between align-items-center">
          {/* Placeholder for Date and Time */}
          <Placeholder as={Col} xs={10} animation="wave" className="mb-0 w-50">
            <Placeholder xs={4} />
          </Placeholder>

          {/* Placeholder for Status */}
          <Placeholder as={Col} xs={1} animation="wave">
            <Placeholder.Button xs={12} variant={color} />
          </Placeholder>
        </div>
      </Card.Header>
      <Card.Body>
        <Stack gap={3}>
          {/* Placeholder for Doctor/Patient */}
          <Placeholder animation="wave" className="w-75">
            <Placeholder xs={8} />
          </Placeholder>

          {/* Placeholder for Address */}
          {!isDoctor && (
            <Placeholder animation="wave" className="w-50">
              <Placeholder xs={6} />
            </Placeholder>
          )}

          {/* Placeholder for Health Insurance */}
          <Placeholder animation="wave" className="w-50">
            <Placeholder xs={6} />
          </Placeholder>

          {/* Placeholder for Description */}
          <Placeholder animation="wave" className="w-100">
            <Placeholder xs={10} />
          </Placeholder>

          {/* Placeholder for Cancel Reason */}
          {status === "CANCELLED" && (
            <Placeholder animation="wave" className="w-100">
              <Placeholder xs={8} />
            </Placeholder>
          )}

          {/* Placeholder for Buttons */}
          <div className="d-flex flex-row justify-content-between align-items-center">
            <Placeholder as={Col} xs={10} animation="wave">
              <Placeholder.Button variant="outline-primary" xs={1} />
            </Placeholder>
            {status === "CONFIRMED" && (
              <Placeholder as={Col} xs={1} animation="wave">
                <Placeholder.Button variant="danger" xs={12} />
              </Placeholder>
            )}
          </div>
        </Stack>
      </Card.Body>
    </Card>
  );
};

export default AppointmentCardPlaceholder;
