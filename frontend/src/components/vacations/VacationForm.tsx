import React, {useEffect, useState} from "react";
import { Alert, Button, Col, Form, Row, Spinner, Stack } from "react-bootstrap";
import { Time, TIMES } from "../../api/time/Time";
import { VacationForm as VacationFormType } from "../../api/vacation/Vacation";
import { AxiosError } from "axios";
import { useCreateVacation } from "../../hooks/vacationHooks";
import { useTranslation } from "react-i18next";
import { SubmitHandler, useForm } from "react-hook-form";
import {
  validateCancelDescription,
  validateVacation,
  validateVacationDate,
  validateVacationTime,
} from "../../api/validation/validations";
import { parseLocalDate } from "../../api/util/dateUtils";

interface VacationFormProps {
  doctorId: string;
}

const VacationForm: React.FC<VacationFormProps> = ({ doctorId }) => {
  const { t } = useTranslation();

  const {
    register,
    handleSubmit,
    formState: { errors },
    setError,
    watch,
    reset,
    setValue,
    resetField,
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
  const fromTime = watch("fromTime");
  const toTime = watch("toTime");

  const [showSuccess, setShowSuccess] = useState<boolean>(false);

  const onSuccess = () => {
    setShowSuccess(true);

    reset();
  };

  const onError = (error: AxiosError) => {
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
    data.fromDate =
      typeof data.fromDate === "string"
        ? parseLocalDate(data.fromDate)
        : data.fromDate;
    data.toDate =
      typeof data.toDate === "string"
        ? parseLocalDate(data.toDate)
        : data.toDate;

    createVacationMutation.mutate(data);
  };

  useEffect(() => {
    if (toDate && fromDate && new Date(toDate) < new Date(fromDate)) {
      resetField("toDate");
    }
  }, [fromDate, toDate, setValue]);

  useEffect(() => {
    if (
        fromDate &&
        toDate &&
        fromDate === toDate &&
        toTime &&
        fromTime &&
        TIMES.indexOf(toTime as Time) <= TIMES.indexOf(fromTime as Time)
    ) {
      setValue("toTime", "");
    }
  }, [fromTime, toTime, fromDate, toDate, setValue]);

  return (
    <Form onSubmit={handleSubmit(onSubmit)}>
      <Row className="mb-3">
        <Form.Group as={Col}>
          <Form.Label>{t("vacation.fromDate")}</Form.Label>
          <Form.Control
            type="date"
            min={new Date().toISOString().split("T")[0]}
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
            min={
              fromDate?.toString().split("T")[0] ??
              new Date().toISOString().split("T")[0]
            }
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
            {TIMES.filter(
              (time) =>
                !fromDate ||
                !toDate ||
                fromDate !== toDate ||
                TIMES.indexOf(time) > TIMES.indexOf(fromTime as Time),
            ).map((time) => (
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

      <div className="mb-3 text-muted">
        {t("vacation.appointmentsInVacation")}
      </div>

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
            rows={5}
            placeholder={t("vacation.reason")}
            {...register("cancelReason", {
              validate: (value) => {
                if (cancelAppointments) {
                  return validateCancelDescription(value);
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
        <Alert variant="primary" dismissible className="mb-3">
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
      </Stack>
    </Form>
  );
};

export default VacationForm;
