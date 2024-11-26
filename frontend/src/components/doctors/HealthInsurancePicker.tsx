import { Control, Controller, FieldErrors } from "react-hook-form";
import { HealthInsurance } from "../../api/health-insurance/HealthInsurance";
import React from "react";
import { useTranslation } from "react-i18next";
import { validateHealthInsurances } from "../../api/validation/validations";
import Select from "react-select";
import makeAnimated from "react-select/animated";
import { DoctorEditForm, DoctorRegisterForm } from "../../api/doctor/Doctor";
import { Form } from "react-bootstrap";

interface HealthInsurancePickerProps {
  healthInsurances: HealthInsurance[];
  control: Control<DoctorRegisterForm> | Control<DoctorEditForm>;
  errors: FieldErrors<DoctorRegisterForm> | FieldErrors<DoctorEditForm>;
}

const animatedComponents = makeAnimated();

const HealthInsurancePicker: React.FC<HealthInsurancePickerProps> = ({
  healthInsurances,
  control,
  errors,
}) => {
  const { t } = useTranslation();

  const customStyles = {
    control: (styles: any) => ({
      ...styles,
      borderColor: errors.healthInsurances ? "#dc3545" : styles.borderColor,
      boxShadow: errors.healthInsurances
        ? "0 0 0 0.2rem rgba(255, 0, 0, 0.25)"
        : styles.boxShadow,
    }),
  };

  const options = healthInsurances.map((healthinsurance) => ({
    value: healthinsurance.code,
    label: t(
      `healthInsurance.${healthinsurance.code.replace(/_/g, ".").toLowerCase()}`,
    ),
  }));

  const textStyle = {
    marginTop: "0.25rem",
    fontSize: "0.875em",
    color: "var(--bs-form-invalid-color)",
  }

  return (
    <Form.Group>
      <Form.Label>{t("form.healthcare")}</Form.Label>
      <Controller
        name="healthInsurances"
        control={control as Control<DoctorRegisterForm>}
        rules={{
          validate: (value) =>
            validateHealthInsurances(value, healthInsurances),
        }}
        render={({ field: { onChange, value } }) => (
          <Select
            isMulti
            components={animatedComponents}
            name="healthInsurances"
            options={options}
            onChange={(option) => {
              if (option) {
                onChange(option.map((o) => o.value));
              }
            }}
            value={options.filter((option) => value?.includes(option.value))}
            placeholder={t("form.healthcare_hint")}
            styles={customStyles}
          />
        )}
      />

      <div className="text-danger" style={textStyle}>
        {errors.healthInsurances && t(errors.healthInsurances.message ?? "")}
      </div>
    </Form.Group>
  );
};

export default HealthInsurancePicker;
