import React, { useState } from "react";
import DoctorList from "../../components/doctors/DoctorList";
import {
  Button,
  Card,
  Col,
  Container,
  Form,
  Row,
  Stack,
} from "react-bootstrap";
import { DoctorQuery, DoctorRegisterForm } from "../../api/doctor/Doctor";
import { FaMagnifyingGlass } from "react-icons/fa6";
import { useTranslation } from "react-i18next";
import { Control, Controller, useForm } from "react-hook-form";
import { validateHealthInsurances } from "../../api/validation/validations";
import Select from "react-select";
import { useHealthInsurances } from "../../hooks/healthInsuranceHooks";
import { useSpecialties } from "../../hooks/specialtyHooks";
import { useCities } from "../../hooks/cityHooks";
import makeAnimated from "react-select/animated";
import DoctorFilters from "../../components/doctors/DoctorFilters";

const animatedHealthInsuranceComponents = makeAnimated();
const animatedSpecialtyComponents = makeAnimated();
const animatedCityComponents = makeAnimated();

const DoctorDashboard: React.FC = () => {
  const { t } = useTranslation();

  const { data: healthInsurances } = useHealthInsurances({
    sort: "popularity",
    order: "desc",
  });
  const { data: specialties } = useSpecialties({
    sort: "popularity",
    order: "desc",
  });
  const { data: cities } = useCities();

  const healthInsuranceOptions =
    healthInsurances
      ?.filter((healthInsurance) => healthInsurance.popularity > 0)
      .map((healthInsurance) => ({
        value: healthInsurance.code,
        label: t(
          `healthInsurance.${healthInsurance.code.replace(/_/g, ".").toLowerCase()}`,
        ),
      })) || [];

  const specialtyOptions =
    specialties
      ?.filter((specialty) => specialty.popularity > 0)
      .map((specialty) => ({
        value: specialty.code,
        label: t(
          `specialty.${specialty.code.replace(/_/g, ".").toLowerCase()}`,
        ),
      })) || [];

  const cityOptions =
    cities
      ?.filter((city) => city.popularity > 0)
      .map((city) => ({
        value: city.name,
        label: city.name,
      })) || [];

  const [query, setQuery] = useState<DoctorQuery>({
    pageSize: 10,
  });

  const { register, watch, control } = useForm<DoctorQuery>({
    defaultValues: query,
  });

  const handleOptionChange = (field: keyof DoctorQuery, values: any[]) => {
    setQuery((prevQuery) => ({
      ...prevQuery,
      [field]: values,
    }));
  };
  const watchName = watch("name");

  const applyName = () => {
    const name =
      watchName && watchName.length > 0 ? watchName : undefined;
    setQuery({
      ...query,
      name,
    });
  };

  return (
    <Container className="d-flex justify-content-center align-items-start mt-5 mb-5">
      <Col className="me-5" md={3} lg={3}>
          <DoctorFilters
              control={control}
              cityOptions={cityOptions}
              healthInsuranceOptions={healthInsuranceOptions}
              specialtyOptions={specialtyOptions}
              handleOptionChange={handleOptionChange}
          />
      </Col>
      <Col md={9} lg={9}>
        <Stack direction="horizontal" className="mb-3" gap={2}>
          <Form.Control
            type="text"
            placeholder={t("doctorDashboard.placeholder.search")}
            {...register("name")}
          />
          <Button onClick={applyName} variant="primary">
            <FaMagnifyingGlass />
          </Button>
        </Stack>
        <DoctorList
          query={query}
          onPageChange={(page: number) => setQuery({ ...query, page })}
        />
      </Col>
    </Container>
  );
};

export default DoctorDashboard;
