import React, { useEffect, useState } from "react";
import { Controller, UseFormReturn, useWatch } from "react-hook-form";
import { Form, Card, Col, Row, Button, Stack } from "react-bootstrap";
import Select from "react-select";
import makeAnimated from "react-select/animated";
import { useTranslation } from "react-i18next";
import { DoctorQuery } from "../../api/doctor/Doctor";
import ClickableRating from "./ClickableRating";
import { Time, TIMES, TIMES_WITH_BLOCK_END } from "../../api/time/Time";
import { parseLocalDate } from "../../api/util/dateUtils";

const animatedHealthInsuranceComponents = makeAnimated();
const animatedSpecialtyComponents = makeAnimated();
const animatedCityComponents = makeAnimated();

type DoctorFiltersProps = {
  cityOptions: { value: string; label: string }[];
  healthInsuranceOptions: { value: string; label: string }[];
  specialtyOptions: { value: string; label: string }[];
  handleFilterChange: (field: keyof DoctorQuery, values: any[] | any) => void;
  reset: () => void;
  form: UseFormReturn<DoctorQuery>;
};

const DoctorFilters: React.FC<DoctorFiltersProps> = ({
  cityOptions,
  healthInsuranceOptions,
  specialtyOptions,
  handleFilterChange,
  reset,
  form,
}) => {
  const { t } = useTranslation();
  const { control, register } = form;

  const [date, setDate] = useState<Date | undefined>(undefined);
  const [toTimes, setToTimes] = useState<string[]>(TIMES_WITH_BLOCK_END);
  const [error, setShowError] = useState(false);

  const [selectedFromTime, setSelectedFromTime] = useState<Time | null>(
    "08:00",
  );
  const [selectedToTime, setSelectedToTime] = useState<Time | null>("18:00");

  useEffect(() => {
    let fromTime = selectedFromTime;
    let toTime = selectedToTime;
    if (selectedFromTime) {
      let selectedToTimeIndex =
        (TIMES.indexOf(selectedToTime as Time) - 1) % TIMES.length;
      selectedToTimeIndex =
        selectedToTimeIndex < 0 ? TIMES.length - 1 : selectedToTimeIndex;

      const selectedFromTimeIndex = TIMES.indexOf(selectedFromTime as Time);

      if (selectedToTimeIndex < selectedFromTimeIndex) {
        toTime = TIMES_WITH_BLOCK_END[selectedFromTimeIndex];

        form.setValue("toTime", toTime);
        setSelectedToTime(toTime);
      }

      const newToTimes = TIMES_WITH_BLOCK_END.filter(
        (time) => TIMES.indexOf(time) > TIMES.indexOf(selectedFromTime as Time),
      );

      if (!newToTimes.includes("00:00")) {
        newToTimes.push("00:00");
      }

      setToTimes(newToTimes);
    }

    if (date) {
      handleFilterChange("fromTime", fromTime);
      handleFilterChange("toTime", toTime);
    }
  }, [selectedFromTime, selectedToTime]);

  const handleReset = () => {
    setDate(undefined);
    setShowError(false);
    setSelectedFromTime("08:00");
    setSelectedToTime("18:00");
    form.setValue("date", undefined);
    form.setValue("minRating", undefined);
    reset();
  };

  return (
    <Card>
      <Card.Body>
        <Card.Title>
          <Stack direction="horizontal" gap={2}>
            {t("filters.title")}
            <Button
              variant="outline-danger"
              className="ms-auto"
              onClick={handleReset}
            >
              {t("filters.clear")}
            </Button>
          </Stack>
        </Card.Title>

        <Form.Group className="mb-3">
          <Form.Label>{t("form.city")}</Form.Label>
          <Controller
            name="city"
            control={control}
            render={({ field: { onChange, value } }) => (
              <Select
                isMulti
                components={animatedCityComponents}
                name="cities"
                options={cityOptions}
                onChange={(option) => {
                  const values = option ? option.map((o) => o.value) : [];
                  onChange(values);
                  handleFilterChange("city", values);
                }}
                value={cityOptions.filter((option) =>
                  value?.includes(option.value),
                )}
                placeholder={t("form.city_hint")}
              />
            )}
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>{t("form.healthcare")}</Form.Label>
          <Controller
            name="healthInsurance"
            control={control}
            render={({ field: { onChange, value } }) => (
              <Select
                isMulti
                components={animatedHealthInsuranceComponents}
                name="healthInsurances"
                options={healthInsuranceOptions}
                onChange={(option) => {
                  const values = option ? option.map((o) => o.value) : [];
                  onChange(values);
                  handleFilterChange("healthInsurance", values);
                }}
                value={healthInsuranceOptions.filter((option) =>
                  value?.includes(option.value),
                )}
                placeholder={t("form.healthcare_hint")}
              />
            )}
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>{t("form.specialization")}</Form.Label>
          <Controller
            name="specialty"
            control={control}
            render={({ field: { onChange, value } }) => (
              <Select
                isMulti
                components={animatedSpecialtyComponents}
                name="specialties"
                options={specialtyOptions}
                onChange={(option) => {
                  const values = option ? option.map((o) => o.value) : [];
                  onChange(values);
                  handleFilterChange("specialty", values);
                }}
                value={specialtyOptions.filter((option) =>
                  value?.includes(option.value),
                )}
                placeholder={t("form.specialization_hint")}
              />
            )}
          />
        </Form.Group>

        <hr />
        <Card.Subtitle className="mt-3 mb-3">
          {t("filters.byRating")}
        </Card.Subtitle>
        <Form.Group className="mb-3">
          <Controller
            render={({ field: { onChange, value } }) => (
              <ClickableRating
                onClick={(rating) => {
                  onChange(rating);
                  handleFilterChange("minRating", rating);
                }}
                initialRating={value}
              />
            )}
            name="minRating"
            control={control}
          />
        </Form.Group>

        <hr />

        <Card.Subtitle className="mt-3 mb-3">
          {t("filters.byAvailability")}
        </Card.Subtitle>

        <Form.Group className="mb-3">
          <Form.Label>{t("vacation.date")}</Form.Label>
          <Form.Control
            type="date"
            {...register("date")}
            onChange={(e) => {
              const date = parseLocalDate(e.target.value);
              const today = new Date();
              today.setHours(0, 0, 0, 0);

              if (date && date >= today) {
                setShowError(false);
                setDate(date);
                handleFilterChange("date", date);
              } else {
                setDate(undefined);
                setShowError(true);
              }
            }}
            isInvalid={error}
          />
          {error && (
            <Form.Control.Feedback type="invalid">
              {t("filters.pastDate")}
            </Form.Control.Feedback>
          )}
        </Form.Group>

        {date && (
          <>
            <Row className="mb-3">
              <Form.Group as={Col}>
                <Form.Label>{t("vacation.fromTime")}</Form.Label>
                <Form.Control
                  {...register("fromTime")}
                  as="select"
                  onChange={(e) => {
                    setSelectedFromTime(e.target.value as Time);
                  }}
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
              </Form.Group>
              <Form.Group as={Col}>
                <Form.Label>{t("vacation.toTime")}</Form.Label>
                <Form.Control
                  {...register("toTime")}
                  as="select"
                  onChange={(e) => {
                    setSelectedToTime(e.target.value as Time);
                  }}
                >
                  <option value="" disabled>
                    {t("vacation.toTime_hint")}
                  </option>
                  {toTimes.map((time) => (
                    <option key={time} value={time}>
                      {time === "00:00" ? "24:00" : time}
                    </option>
                  ))}
                </Form.Control>
              </Form.Group>
            </Row>

            <Button
              variant="link"
              onClick={() => {
                setDate(undefined);
                form.setValue("date", undefined);
                setShowError(false);
                setSelectedFromTime("08:00");
                setSelectedToTime("18:00");
                handleFilterChange("date", undefined);
                handleFilterChange("fromTime", undefined);
                handleFilterChange("toTime", undefined);
              }}
            >
              {t("filters.clear")}
            </Button>
          </>
        )}
      </Card.Body>
    </Card>
  );
};

export default DoctorFilters;
