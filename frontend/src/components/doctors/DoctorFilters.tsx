import React from "react";
import { Controller, Control } from "react-hook-form";
import { Form, Card } from "react-bootstrap";
import Select from "react-select";
import makeAnimated from "react-select/animated";
import { useTranslation } from "react-i18next";
import {DoctorQuery} from "../../api/doctor/Doctor";

const animatedHealthInsuranceComponents = makeAnimated();
const animatedSpecialtyComponents = makeAnimated();
const animatedCityComponents = makeAnimated();

type DoctorFiltersProps = {
  control: Control<any>;
  cityOptions: { value: string; label: string }[];
  healthInsuranceOptions: { value: string; label: string }[];
  specialtyOptions: { value: string; label: string }[];
  handleOptionChange: (field: keyof DoctorQuery, values: any[]) => void;
};

const DoctorFilters: React.FC<DoctorFiltersProps> = ({
  control,
  cityOptions,
  healthInsuranceOptions,
  specialtyOptions,
  handleOptionChange,
}) => {
  const { t } = useTranslation();

  return (
    <Card>
      <Card.Body>
        <Card.Title>{t("filters.title")}</Card.Title>

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
                  handleOptionChange("city", values);
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
                  handleOptionChange("healthInsurance", values);
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
                  handleOptionChange("specialty", values);
                }}
                value={specialtyOptions.filter((option) =>
                  value?.includes(option.value),
                )}
                placeholder={t("form.specialization_hint")}
              />
            )}
          />
        </Form.Group>
      </Card.Body>
    </Card>
  );
};

export default DoctorFilters;
