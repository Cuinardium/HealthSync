import React, { useState } from "react";
import { useCreateAppointment } from "../../hooks/appointmentHooks";
import {
  AppointmentForm as AppointmentFormType,
  DoctorNotAvailable,
  PatientNotAvailable,
} from "../../api/appointment/Appointment";
import {
  Alert,
  Button,
  Col,
  Form,
  Modal,
  Row,
  Spinner,
  Stack,
} from "react-bootstrap";
import { SubmitHandler, useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { formatDatePretty } from "../../api/util/dateUtils";
import { Link } from "react-router-dom";
import { validateAppointmentDescription } from "../../api/validation/validations";
import { Doctor } from "../../api/doctor/Doctor";
import { Patient } from "../../api/patient/Patient";

interface AppointmentFormProps {
  doctor: Doctor;
  user?: Patient;
  date: Date;
  time: string;
  show: boolean;
  onHide: () => void;
}

const AppointmentForm: React.FC<AppointmentFormProps> = ({
  doctor,
  user,
  date,
  time,
  show,
  onHide,
}) => {
  const { t } = useTranslation();

  const {
    register,
    handleSubmit,
    formState: { errors },
    setError,
    reset,
    setValue,
  } = useForm<AppointmentFormType>({
    defaultValues: {
      description: "",
    },
  });

  const [showSuccess, setShowSuccess] = useState<boolean>(false);
  const [appointmentId, setAppointmentId] = useState<number>();

  const [showAreYouSure, setShowAreYouSure] = useState<boolean>(() => {
    if (!user) {
      return false;
    }

    const hasSameHealthInsurance = doctor.healthInsurances.some(
      (hi) => hi === user.healthInsurance,
    );
    return !hasSameHealthInsurance;
  });

  const onSuccess = (appointmentId: number) => {
    handleHide();
    setShowSuccess(true);
    setAppointmentId(appointmentId);
  };

  const onError = (error: Error) => {
    console.log(error)
    if (error instanceof DoctorNotAvailable) {
      setError("root", {
        message: "appointment.doctorNotAvailable",
      });
    } else if (error instanceof PatientNotAvailable) {
      setError("root", {
        message: "appointment.patientNotAvailable",
      });
    } else {
      setError("root", {
        message: "appointment.submitError",
      });
    }
    // Call onHide after 2 seconds
    setTimeout(() => {
      handleHide();
    }, 2000);
  };

  const appointmentMutation = useCreateAppointment(onSuccess, onError);

  const handleCreateAppointment: SubmitHandler<AppointmentFormType> = (
    data: AppointmentFormType,
  ) => {
    data.date = date;
    data.timeBlock = time;
    data.doctorId = doctor.id;

    appointmentMutation.mutate(data);
  };

  const handleHide = () => {
    setValue("description", "");
    reset();
    onHide();
  };

  return (
    <div>
      <Modal show={show} onHide={handleHide}>
        {showAreYouSure && (
          <>
            <Modal.Header closeButton>
              <Modal.Title>{t("doctorDashboard.modal.title")}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              <h5 className="text-muted mb-3">
                {t("doctorDashboard.modal.desc")}
              </h5>
              <Stack direction="horizontal">
                <Button
                  variant="primary"
                  onClick={() => setShowAreYouSure(false)}
                  className="ms-auto me-2"
                >
                  {t("doctorDashboard.modal.confirm")}
                </Button>
                <Button variant="secondary" onClick={handleHide}>
                  {t("doctorDashboard.modal.deny")}
                </Button>
              </Stack>
            </Modal.Body>
          </>
        )}
        {!showAreYouSure && (
          <>
            <Modal.Header closeButton>
              <Modal.Title>{t("appointment.title")}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              <Form onSubmit={handleSubmit(handleCreateAppointment)}>
                <Row>
                  <Col>
                    <Form.Group className="mb-3" controlId="date">
                      <Form.Label>{t("form.date")}</Form.Label>
                      <Form.Control
                        type="text"
                        placeholder={formatDatePretty(date)}
                        disabled
                      />
                    </Form.Group>
                  </Col>
                  <Col>
                    <Form.Group className="mb-3" controlId="timeBlock">
                      <Form.Label>{t("form.hour")}</Form.Label>
                      <Form.Control type="text" placeholder={time} disabled />
                    </Form.Group>
                  </Col>
                </Row>
                <Form.Group className="mb-3" controlId="description">
                  <Form.Label>{t("form.desc")}</Form.Label>
                  <Form.Control
                    as="textarea"
                    cols={5}
                    placeholder={t("form.desc_hint")}
                    {...register("description", {
                      validate: validateAppointmentDescription,
                    })}
                    isInvalid={!!errors.description}
                  />
                  <Form.Control.Feedback type="invalid">
                    {errors.description && t(errors.description?.message ?? "")}
                  </Form.Control.Feedback>
                </Form.Group>

                {errors.root && (
                  <Alert variant="danger" className="mb-3">
                    {t(errors.root?.message ?? "")}
                  </Alert>
                )}

                <Stack direction="horizontal">
                  <Button
                    variant="primary"
                    type="submit"
                    disabled={appointmentMutation.isPending}
                    className="ms-auto"
                  >
                    {appointmentMutation.isPending ? (
                      <>
                        <Spinner
                          as="span"
                          animation="border"
                          size="sm"
                          role="status"
                          aria-hidden="true"
                        />{" "}
                        {t("appointment.submitting")}
                      </>
                    ) : (
                      t("appointment.submit")
                    )}
                  </Button>
                </Stack>
              </Form>
            </Modal.Body>
          </>
        )}
      </Modal>

      <Modal show={showSuccess} onHide={handleHide}>
        <Modal.Header closeButton>
          <Modal.Title>{t("appointment.successfull.modal.title")}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <h5 className="text-muted">
            {t("appointment.successfull.modal.text")}
          </h5>
        </Modal.Body>
        <Modal.Footer>
          <Button
            as={Link as any}
            variant="outline-primary"
            to={`/detailed-appointment/${appointmentId}`}
            disabled={!appointmentId}
          >
            {t("appointment.successfull.modal.button")}
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default AppointmentForm;
