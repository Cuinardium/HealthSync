import React, { useEffect, useState } from "react";
import { Card, Col, Row, Button, Stack, Form } from "react-bootstrap";
import Select from "react-select";
import makeAnimated from "react-select/animated";
import { useTranslation } from "react-i18next";
import { Time, TIMES, TIMES_WITH_BLOCK_END } from "../../api/time/Time";
import { parseLocalDate } from "../../api/util/dateUtils";
import { useDoctorQueryContext } from "../../context/DoctorQueryContext";
import ClickableRating from "./ClickableRating";
import { useHealthInsurances } from "../../hooks/healthInsuranceHooks";
import { useSpecialties } from "../../hooks/specialtyHooks";
import { useCities } from "../../hooks/cityHooks";

const animatedHealthInsuranceComponents = makeAnimated();
const animatedSpecialtyComponents = makeAnimated();
const animatedCityComponents = makeAnimated();

type DoctorFiltersProps = {
  onReset?: () => void;
};

const DoctorFilters: React.FC<DoctorFiltersProps> = ({
  onReset = () => {},
}) => {
  const { t } = useTranslation();
  const {
    query,
    setCity,
    setSpecialty,
    setHealthInsurance,
    setMinRating,
    setDate,
    setFromTime,
    setToTime,
    resetQuery,
  } = useDoctorQueryContext();

  const { data: healthInsurances } = useHealthInsurances({
    ...query,
    healthInsurance: undefined,
    sort: "popularity",
    order: "desc",
  });
  const { data: specialties } = useSpecialties({
    ...query,
    specialty: undefined,
    sort: "popularity",
    order: "desc",
  });
  const { data: cities } = useCities({
    ...query,
    city: undefined,
    sort: "popularity",
    order: "desc",
  });

  const healthInsuranceOptions =
    healthInsurances
      ?.filter((healthInsurance) => healthInsurance.popularity > 0)
      .map((healthInsurance) => ({
        value: healthInsurance.code,
        label:
          t(
            `healthInsurance.${healthInsurance.code.replace(/_/g, ".").toLowerCase()}`,
          ) +
          " (" +
          healthInsurance.popularity +
          ")",
      })) || [];

  const specialtyOptions =
    specialties
      ?.filter((specialty) => specialty.popularity > 0)
      .map((specialty) => ({
        value: specialty.code,
        label:
          t(`specialty.${specialty.code.replace(/_/g, ".").toLowerCase()}`) +
          " (" +
          specialty.popularity +
          ")",
      })) || [];

  const cityOptions =
    cities
      ?.filter((city) => city.popularity > 0)
      .map((city) => ({
        value: city.name,
        label: city.name + " (" + city.popularity + ")",
      })) || [];
  const [toTimes, setToTimes] = useState<string[]>(TIMES_WITH_BLOCK_END);
  const [error, setShowError] = useState(false);

  const handleReset = () => {
    resetQuery();
    setShowError(false);
    onReset();
  };

  useEffect(() => {
    const handleTimeChange = () => {
      const fromTime = query.fromTime;
      const fromIndex = TIMES.indexOf(fromTime as Time);

      if (fromTime) {
        const availableToTimes = TIMES_WITH_BLOCK_END.filter(
          (time) => TIMES.indexOf(time) > fromIndex,
        );

        if (!availableToTimes.includes("00:00")) {
          availableToTimes.push("00:00");
        }

        setToTimes(availableToTimes);
      }
    };
    handleTimeChange();
  }, [query.fromTime]);

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
          <Select
            isMulti
            components={animatedCityComponents}
            options={cityOptions}
            value={cityOptions.filter((option) =>
              query.city?.includes(option.value),
            )}
            onChange={(selected) =>
              setCity(selected ? selected.map((s) => s.value) : [])
            }
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>{t("form.healthcare")}</Form.Label>
          <Select
            isMulti
            components={animatedHealthInsuranceComponents}
            options={healthInsuranceOptions}
            value={healthInsuranceOptions.filter((option) =>
              query.healthInsurance?.includes(option.value),
            )}
            onChange={(selected) =>
              setHealthInsurance(selected ? selected.map((s) => s.value) : [])
            }
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>{t("form.specialization")}</Form.Label>
          <Select
            isMulti
            components={animatedSpecialtyComponents}
            options={specialtyOptions}
            value={specialtyOptions.filter((option) =>
              query.specialty?.includes(option.value),
            )}
            onChange={(selected) =>
              setSpecialty(selected ? selected.map((s) => s.value) : [])
            }
          />
        </Form.Group>

        <hr />
        <Card.Subtitle className="mt-3 mb-3">
          {t("filters.byRating")}
        </Card.Subtitle>

        <Form.Group className="mb-3">
          <ClickableRating
            initialRating={query.minRating}
            onClick={(rating) => setMinRating(rating)}
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
            min={new Date().toISOString().split("T")[0]}
            value={query.date ? query.date.toISOString().split("T")[0] : ""}
            onChange={(e) => {
              const date = parseLocalDate(e.target.value);
              const today = new Date();
              today.setHours(0, 0, 0, 0);

              if (date && date >= today) {
                setShowError(false);

                setDate(date);
              } else if (date < today) {
                setShowError(true);
                setDate(undefined);
                return;
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

        {query.date && (
          <>
            <Row className="mb-3">
              <Col>
                <Form.Label>{t("vacation.fromTime")}</Form.Label>
                <Form.Select
                  value={query.fromTime || ""}
                  onChange={(e) => setFromTime(e.target.value as Time)}
                >
                  {TIMES.map((time) => (
                    <option key={time} value={time}>
                      {time}
                    </option>
                  ))}
                </Form.Select>
              </Col>
              <Col>
                <Form.Label>{t("vacation.toTime")}</Form.Label>
                <Form.Select
                  value={query.toTime || ""}
                  onChange={(e) => setToTime(e.target.value as Time)}
                >
                  {toTimes.map((time) => (
                    <option key={time} value={time}>
                      {time === "00:00" ? "24:00" : time}
                    </option>
                  ))}
                </Form.Select>
              </Col>
            </Row>
            <Button
              variant="link"
              onClick={() => {
                setDate(undefined);
                setShowError(false);
                setToTime(undefined);
                setFromTime(undefined);
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
