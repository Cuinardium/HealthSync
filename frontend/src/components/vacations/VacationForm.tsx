import React, { useState } from "react";
import { Alert, Button, Col, Form, Row, Spinner, Stack } from "react-bootstrap";
import { TIMES } from "../../api/time/Time";
import { VacationForm as VacationFormType } from "../../api/vacation/Vacation";
import { AxiosError } from "axios";
import { useCreateVacation } from "../../hooks/vacationHooks";
import { useTranslation } from "react-i18next";
import { SubmitHandler, useForm } from "react-hook-form";
import {
  validateVacation,
  validateVacationDate,
  validateVacationTime,
} from "../../api/validation/validations";

interface VacationFormProps {
  doctorId: string;
  onCloseClicked: () => void;
}

const VacationForm: React.FC<VacationFormProps> = ({ doctorId, onCloseClicked}) => {
  const { t } = useTranslation();

  const {
    register,
    handleSubmit,
    formState: { errors },
    setError,
    watch,
  } = useForm<VacationFormType>({
    defaultValues: {
      cancelAppointments: false,
      fromTime: "",
      toTime: "",
    },
  });

  const cancelAppointments = watch("cancelAppointments");
  const fromDate = watch("fromDate");
  const toDate = watch("toDate");

  const [showSuccess, setShowSuccess] = useState<boolean>(false);

  const onSuccess = () => {
    setShowSuccess(true);
  };

  const onError = (error: AxiosError) => {
    console.log(error)
    if (error?.response?.status === 409) {
      setError("root", {
        message: "vacation.vacationOverlaps",
      });
    } else {
      setError("root", {
        message: "vacation.error",
      });
    }
  };

  const createVacationMutation = useCreateVacation(
    doctorId,
    onSuccess,
    onError,
  );

  const onSubmit: SubmitHandler<VacationFormType> = (
    data: VacationFormType,
  ) => {
    const validation = validateVacation(data);
    if (validation !== true) {
      setError("root", {
        message: validation,
      });
      return;
    }

    // If dates are strings, convert them to Date objects
    data.fromDate = typeof data.fromDate === "string" ? new Date(data.fromDate) : data.fromDate;
    data.toDate = typeof data.toDate === "string" ? new Date(data.toDate) : data.toDate;

    createVacationMutation.mutate(data);
  };

  return (
    <Form onSubmit={handleSubmit(onSubmit)}>
      <Row className="mb-3">
        <Form.Group as={Col}>
          <Form.Label>{t("vacation.fromDate")}</Form.Label>
          <Form.Control
            type="date"
            {...register("fromDate", { validate: validateVacationDate })}
            isInvalid={!!errors.fromDate}
          />
          <Form.Control.Feedback type="invalid">
            {errors.fromDate && t(errors.fromDate?.message ?? "")}
          </Form.Control.Feedback>
        </Form.Group>
        <Form.Group as={Col}>
          <Form.Label>{t("vacation.toDate")}</Form.Label>
          <Form.Control
            type="date"
            {...register("toDate", { validate: validateVacationDate })}
            isInvalid={!!errors.toDate}
          />
          <Form.Control.Feedback type="invalid">
            {errors.toDate && t(errors.toDate?.message ?? "")}
          </Form.Control.Feedback>
        </Form.Group>
      </Row>

      <Row className="mb-3">
        <Form.Group as={Col}>
          <Form.Label>{t("vacation.fromTime")}</Form.Label>
          <Form.Control
            {...register("fromTime", {
              validate: (value) => validateVacationTime(value, fromDate),
            })}
            as="select"
            isInvalid={!!errors.fromTime}
          >
            <option value="" disabled>
              {t("vacation.fromTime_hint")}
            </option>
            {TIMES.map((time) => (
              <option key={time} value={time}>
                {time}
              </option>
            ))}
          </Form.Control>
          <Form.Control.Feedback type="invalid">
            {errors.fromTime && t(errors.fromTime?.message ?? "")}
          </Form.Control.Feedback>
        </Form.Group>
        <Form.Group as={Col}>
          <Form.Label>{t("vacation.toTime")}</Form.Label>
          <Form.Control
            {...register("toTime", {
              validate: (value) => validateVacationTime(value, toDate),
            })}
            as="select"
            isInvalid={!!errors.toTime}
          >
            <option value="" disabled>
              {t("vacation.toTime_hint")}
            </option>
            {TIMES.map((time) => (
              <option key={time} value={time}>
                {time}
              </option>
            ))}
          </Form.Control>
          <Form.Control.Feedback type="invalid">
            {errors.toTime && t(errors.toTime?.message ?? "")}
          </Form.Control.Feedback>
        </Form.Group>
      </Row>

      <Form.Group className="mb-3" controlId="cancelAppointments">
        <Form.Check
          type="checkbox"
          label={t("vacation.cancelAppointmentsInVacation")}
          {...register("cancelAppointments")}
        />
      </Form.Group>

      {cancelAppointments && (
        <Form.Group className="mb-3">
          <Form.Label>
            {t("vacation.appointmentsInVacationCancelReasonTitle")}
          </Form.Label>
          <Form.Control
            as="textarea"
            placeholder={t("vacation.reason")}
            {...register("cancelReason", {
              validate: (value) => {
                if (cancelAppointments && (!value || value.trim() === "")) {
                  return "validation.vacation.cancelReason.required";
                }
              },
            })}
            isInvalid={!!errors.cancelReason}
          />
          <Form.Control.Feedback type="invalid">
            {errors.cancelReason && t(errors.cancelReason?.message ?? "")}
          </Form.Control.Feedback>
        </Form.Group>
      )}

      {errors.root && (
        <Alert variant="danger" className="mb-3">
          {t(errors.root?.message ?? "")}
        </Alert>
      )}
      {showSuccess && (
        <Alert variant="success" dismissible className="mb-3">
          {t("vacation.addSuccessDescription")}
        </Alert>
      )}

      <Stack direction="horizontal">
        <Button
          variant="primary"
          type="submit"
          disabled={createVacationMutation.isPending}
          className="ms-auto me-2"
        >
          {createVacationMutation.isPending ? (
            <>
              <Spinner
                as="span"
                animation="border"
                size="sm"
                role="status"
                aria-hidden="true"
              />{" "}
              {t("vacation.loading")}
            </>
          ) : (
            t("vacation.addButton")
          )}
        </Button>
        <Button variant="secondary" onClick={onCloseClicked}>
          {t("vacation.cancel")}
        </Button>
      </Stack>
    </Form>
  );
};

export default VacationForm;
