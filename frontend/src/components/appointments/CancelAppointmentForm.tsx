import { AxiosError } from "axios";
import React, { useState } from "react";
import {
  Alert,
  Button,
  Col,
  Form,
  Modal,
  Spinner,
} from "react-bootstrap";
import { useCancelAppointment } from "../../hooks/appointmentHooks";
import { useTranslation } from "react-i18next";
import { SubmitHandler, useForm } from "react-hook-form";
import { validateCancelDescription } from "../../api/validation/validations";

interface CancelAppointmentFormProps {
  appointmentId: string;
  showCancelModal: boolean;
  onHide: () => void;
}

interface CancelFormType {
  cancelDescription: string;
}

const CancelAppointmentForm: React.FC<CancelAppointmentFormProps> = ({
  appointmentId,
  showCancelModal,
  onHide,
}) => {
  const { t } = useTranslation();

  const [showSuccess, setShowSuccess] = useState<boolean>(false);

  const {
    register,
    handleSubmit,
    formState: { errors },
    setError,
    reset,
  } = useForm<CancelFormType>();

  const handleHide = () => {
    reset();
    setShowSuccess(false);
    onHide();
  };

  const onSuccess = () => {
    setShowSuccess(true);

    // Call onHide after 2 seconds
    setTimeout(() => {
      handleHide();
    }, 2000);
  };

  const onError = (_: AxiosError) => {
    setError("root", {
      message: "appointments.cancelModal.error",
    });

    // Call onHide after 2 seconds
    setTimeout(() => {
      handleHide();
    }, 2000);
  };

  const cancelAppointmentMutation = useCancelAppointment(
    appointmentId,
    onSuccess,
    onError,
  );

  const handleCancelReview: SubmitHandler<CancelFormType> = (
    data: CancelFormType,
  ) => {
    cancelAppointmentMutation.mutate(data.cancelDescription);
  };

  return (
    <div>
      {/* Cancel Appointment Modal */}
      <Modal show={showCancelModal} onHide={handleHide}>
        <Modal.Header closeButton>
          <Modal.Title>{t("appointments.cancelModal.title")}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form onSubmit={handleSubmit(handleCancelReview)}>
            <h5 className="text-muted mb-3">
              {t("appointments.cancelModal.desc")}
            </h5>
            <Form.Group as={Col} controlId="description" className="mb-3">
              <Form.Label>
                {t("appointments.cancelModal.cancelDesc")}
              </Form.Label>
              <Form.Control
                {...register("cancelDescription", {
                  validate: validateCancelDescription,
                })}
                as="textarea"
                name="cancelDescription"
                rows={5}
                isInvalid={!!errors.cancelDescription}
              />
              <Form.Control.Feedback type="invalid">
                {errors.cancelDescription &&
                  t(errors.cancelDescription?.message ?? "")}
              </Form.Control.Feedback>
            </Form.Group>

            {errors.root && (
              <Alert variant="danger" className="mb-3">
                {t(errors.root?.message ?? "")}
              </Alert>
            )}
            {showSuccess && (
              <Alert variant="primary" dismissible className="mb-3">
                {t("appointments.cancelModal.success")}
              </Alert>
            )}

            <div className="d-flex flex-row justify-content-end align-items-center">
              <Button
                variant="danger"
                type="submit"
                disabled={cancelAppointmentMutation.isPending || showSuccess}
              >
                {cancelAppointmentMutation.isPending ? (
                  <>
                    <Spinner
                      as="span"
                      animation="border"
                      size="sm"
                      role="status"
                      aria-hidden="true"
                    />{" "}
                    {t("appointments.cancelModal.cancelling")}
                  </>
                ) : (
                  t("appointments.cancelModal.confirm")
                )}
              </Button>
            </div>
          </Form>
        </Modal.Body>
      </Modal>
    </div>
  );
};

export default CancelAppointmentForm;
