

import React from "react";
import { Card, Stack, Badge, Button } from "react-bootstrap";
import { Link } from "react-router-dom";
import { FaCircle } from "react-icons/fa";
import {Appointment} from "../../api/appointment/Appointment";

interface AppointmentProps {
    appointment: Appointment;
    hasNotification: boolean;
}

const AppointmentCard: React.FC<AppointmentProps> = ({appointment, hasNotification}) => {

    return (
        <Card className="mb-3 shadow-sm">
            <Card.Body>
                <Stack gap={3}>
                    {/* Date and Time */}
                    <div>
                        <h5>
                            {appointment.date.toLocaleDateString("en-GB")}{" "}
                            <Badge bg="info">{appointment.timeBlock}</Badge>
                        </h5>
                    </div>

                    {/* Status */}
                    <div>
                        <strong>Status:</strong>{" "}
                        <Badge
                            bg={
                                appointment.status === "CANCELLED"
                                    ? "danger"
                                    : appointment.status === "COMPLETED"
                                        ? "success"
                                        : "primary"
                            }
                        >
                            {appointment.status}
                        </Badge>
                    </div>

                    {/* Doctor and Patient */}
                    <div>
                        <strong>Doctor:</strong> {appointment.doctorId}
                    </div>
                    <div>
                        <strong>Patient:</strong> {appointment.patientId}
                    </div>

                    {/* Description */}
                    <div>
                        <strong>Description:</strong> {appointment.description}
                    </div>

                    {/* Cancel Reason */}
                    {appointment.status === "CANCELLED" && appointment.cancelDescription && (
                        <div>
                            <strong>Cancel Reason:</strong> {appointment.cancelDescription}
                        </div>
                    )}

                    {/* Action Links */}
                    <div className="d-flex justify-content-between align-items-center">
                        <Link to={`/detailed-appointment/${appointment.id}`}>
                            <Button variant="outline-primary" size="sm">
                                More Details
                            </Button>
                        </Link>
                        {hasNotification && (
                            <FaCircle className="text-warning ms-2" title="Notification" />
                        )}
                    </div>
                </Stack>
            </Card.Body>
        </Card>
    );
};

export default AppointmentCard;
