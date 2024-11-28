import React from "react";
import { Card, Stack, Badge, Button, Col, Row } from "react-bootstrap";
import { Link } from "react-router-dom";
import { FaBell, FaCircle } from "react-icons/fa";
import { Appointment } from "../../api/appointment/Appointment";
import { usePatient } from "../../hooks/patientHooks";
import { useDoctor } from "../../hooks/doctorHooks";
import { useAuth } from "../../context/AuthContext";
import { useUser } from "../../context/UserContext";
import { useTranslation } from "react-i18next";
import AppointmentCardPlaceholder from "./AppointmentCardPlaceholder";

interface AppointmentProps {
  appointment: Appointment;
  hasNotification: boolean;
  onCancelClick?: (appointmentId: number) => void;
  selected?: boolean;
}

const AppointmentCard: React.FC<AppointmentProps> = ({
  appointment,
  hasNotification,
  onCancelClick = () => {},
  selected = false,
}) => {
  const { t } = useTranslation();

  const color =
    appointment.status === "CANCELLED"
      ? "danger"
      : appointment.status === "COMPLETED"
        ? "success"
        : "primary";

  const { data: patient, isLoading: isPatientLoading } = usePatient(
    appointment.patientId,
  );
  const { data: doctor, isLoading: isDoctorLoading } = useDoctor(
    appointment.doctorId,
  );

  const { isDoctor, loading } = useUser();

  if (isPatientLoading || isDoctorLoading || loading) {
    return (<AppointmentCardPlaceholder status={appointment.status} isDoctor={isDoctor?? false}/>)
  }

  return (
    <Card className="mb-3 shadow-sm" border={color}>
      <Card.Header>
        <Stack direction="horizontal" gap={3}>
          {/* Date and Time */}
          <div className={"d-flex flex-row align-items-center"}>
            <h5 className="mb-0">
              {appointment.date.toLocaleDateString("en-GB")}{" "}
              <Badge className="ms-1" bg="primary">
                {appointment.timeBlock}
              </Badge>
            </h5>
          </div>

          {/* Status */}
          <div className={"d-flex flex-row align-items-center ms-auto"}>
            <Badge bg={color}>
              {t(`appointmentStatus.${appointment.status.toLowerCase()}`)}
            </Badge>
          </div>
        </Stack>
      </Card.Header>
      <Card.Body>
        <Stack gap={3}>
          {/* Doctor and Patient */}
          {isDoctor && (
            <div>
              <strong>{t("appointment.patient")}:</strong>{" "}
              {patient?.firstName + " " + patient?.lastName}
            </div>
          )}
          {!isDoctor && (
            <div>
              <strong>{t("appointment.doctor")}:</strong>{" "}
              {doctor?.firstName + " " + doctor?.lastName}
            </div>
          )}
          {/* address */}
          {!isDoctor && (
            <div>
              <strong>{t("appointment.address")}:</strong> {doctor?.address}
            </div>
          )}

          {/* Health Insurance */}
          {isDoctor && (
            <div>
              <strong>{t("appointment.healthInsurance")}:</strong>{" "}
              {t(`healthInsurance.${patient?.healthInsurance}`)}
            </div>
          )}

          {/* Description */}
          <div>
            <strong>{t("appointment.description")}:</strong>{" "}
            {appointment.description}
          </div>

          {/* Cancel Reason */}
          {appointment.status === "CANCELLED" &&
            appointment.cancelDescription && (
              <div>
                <strong>{t("appointment.cancelDesc")}:</strong> {appointment.cancelDescription}
              </div>
            )}

          {/* Action Links */}
          <div className="d-flex flex-row justify-content-between align-items-center">
            <Link to={`/detailed-appointment/${appointment.id}`}>
              <Button variant="outline-primary" size="sm">
                {t("appointment.details")}

                {hasNotification && (
                  <FaCircle className="text-danger ms-2" title="Notification" />
                )}
              </Button>
            </Link>

            {appointment.canCancel && (
              <Button
                variant="danger"
                size="sm"
                onClick={() => onCancelClick(appointment.id)}
                disabled={selected}
              >
                {t("appointment.cancel")}
              </Button>
            )}
          </div>
        </Stack>
      </Card.Body>
    </Card>
  );
};

export default AppointmentCard;
