import React from "react";
import { Card, Stack, Badge, Button, Col, Row, Form } from "react-bootstrap";
import {Link, useNavigate} from "react-router-dom";
import {FaArrowUpRightFromSquare, FaCircle, FaFileExcel} from "react-icons/fa6";
import { Appointment } from "../../api/appointment/Appointment";
import { usePatient } from "../../hooks/patientHooks";
import { useDoctor } from "../../hooks/doctorHooks";
import { useUser } from "../../context/UserContext";
import { useTranslation } from "react-i18next";
import AppointmentCardPlaceholder from "./AppointmentCardPlaceholder";
import { FaFileLines, FaUser } from "react-icons/fa6";
import useLocale from "../../hooks/useLocale";
import {formatDatePrettyLong} from "../../api/util/dateUtils";

interface AppointmentProps {
  appointment: Appointment;
  hasNotification: boolean;
  onCancelClick?: (appointmentId: number) => void;
  selected?: boolean;
}

const DetailedAppointmentCard: React.FC<AppointmentProps> = ({
  appointment,
  onCancelClick = () => {},
  selected = false,
}) => {
  const { t } = useTranslation();
  const { locale } = useLocale();

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
    return (
      <AppointmentCardPlaceholder
        status={appointment.status}
        isDoctor={isDoctor ?? false}
        showButtons={false}
      />
    );
  }

  return (
    <Card className="mb-3 shadow-sm" border={color}>
      <Card.Header>
        <Stack direction="horizontal" gap={3}>
          {/* Date and Time */}
          <div className={"d-flex flex-row align-items-center"}>
            <h5 className="mb-0">
              {formatDatePrettyLong(appointment.date, locale)}{" "}
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
          <Row>
            <Col>
              <Stack gap={3}>
                {/* Description */}
                <h5 className="text-center">
                  {t("appointment.description")} <FaFileLines />
                </h5>
                <Form.Control
                  type="textarea"
                  className="text-center"
                  disabled
                  value={appointment.description}
                />

                {/* Cancel Reason */}
                {appointment.status === "CANCELLED" &&
                  appointment.cancelDescription && (
                    <>
                      <h5 className="text-center">
                        {t("appointment.cancelDesc")} <FaFileExcel />
                      </h5>
                      <Form.Control
                        type="textarea"
                        className="text-center"
                        disabled
                        value={appointment.cancelDescription}
                      />
                    </>
                  )}
              </Stack>
            </Col>
            {/* Doctor and Patient */}

            <Col className={"d-flex flex-column justify-content-center"}>
              {isDoctor && (
                <>
                  <h5 className="text-center">
                    {t("appointment.patient")} <FaUser />
                  </h5>
                  <div className="d-flex flex-row justify-content-center">
                    <strong className="me-1">{t("form.name")}:</strong>{" "}
                    {patient?.firstName + " " + patient?.lastName}
                  </div>
                  <div className="d-flex flex-row justify-content-center">
                    <strong className="me-1">{t("form.email")}:</strong>{" "}
                    {patient?.email}
                  </div>

                  <div className="d-flex flex-row justify-content-center">
                    <strong className="me-1">
                      {t("appointment.healthInsurance")}:
                    </strong>{" "}
                    {t(`healthInsurance.${patient?.healthInsurance}`)}
                  </div>
                </>
              )}
              {!isDoctor && (
                <>
                  <h5 className="text-center">
                    {t("appointment.doctor")} <Link to={`/detailed-doctor/${doctor?.id}`} ><FaArrowUpRightFromSquare className="text-primary"/></Link>
                  </h5>
                  <div className="d-flex flex-row justify-content-center">
                    <strong className="me-1">{t("form.name")}:</strong>{" "}
                    {doctor?.firstName + " " + doctor?.lastName}
                  </div>
                  <div className="d-flex flex-row justify-content-center">
                    <strong className="me-1">{t("form.email")}:</strong>{" "}
                    {doctor?.email}
                  </div>
                  <div className="d-flex flex-row justify-content-center">
                    <strong className="me-1">{t("form.address")}:</strong>{" "}
                    {doctor?.address}
                  </div>
                </>
              )}
            </Col>
          </Row>

          {/* Action Links */}
          <div className="d-flex flex-row justify-content-end align-items-center">
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

export default DetailedAppointmentCard;
