import React, { useState } from "react";
import DoctorList from "../../components/doctors/DoctorList";
import { Button, Col, Container, Form, Stack } from "react-bootstrap";
import { DoctorQuery } from "../../api/doctor/Doctor";
import { FaMagnifyingGlass } from "react-icons/fa6";
import { useTranslation } from "react-i18next";
import { useForm } from "react-hook-form";
import { useHealthInsurances } from "../../hooks/healthInsuranceHooks";
import { useSpecialties } from "../../hooks/specialtyHooks";
import { useCities } from "../../hooks/cityHooks";
import DoctorFilters from "../../components/doctors/DoctorFilters";
import { isSameDay } from "../../api/util/dateUtils";

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

  const [query, setQuery] = useState<DoctorQuery>({
    pageSize: 10,
  });

  const form = useForm<DoctorQuery>({
    defaultValues: query,
  });

  const handleFilterChange = (
    field: keyof DoctorQuery,
    values: any[] | any,
  ) => {
    setQuery((prevQuery) => {
      const newQuery = { ...prevQuery, [field]: values };

      // If date does nor change in days
      if (isSameDay(newQuery.date, prevQuery.date)) {
        newQuery.date = prevQuery.date;
      }

      if (newQuery.date && (!newQuery.fromTime || !newQuery.toTime)) {
        newQuery.fromTime = "08:00";
        newQuery.toTime = "18:00";
        form.setValue("fromTime", "08:00");
        form.setValue("toTime", "18:00");
      }

      return newQuery;
    });
  };

  const resetFilters = () => {
    form.reset();
    form.setValue("name", "");
    setQuery({
      pageSize: 10,
    });
  };

  const watchName = form.watch("name");

  const applyName = () => {
    const name = watchName && watchName.length > 0 ? watchName : undefined;
    setQuery({
      ...query,
      name,
    });
  };

  return (
    <Container className="d-flex justify-content-center align-items-start mt-5 mb-5">
      <Col className="me-5" md={3} lg={3}>
        <DoctorFilters
          cityOptions={cityOptions}
          healthInsuranceOptions={healthInsuranceOptions}
          specialtyOptions={specialtyOptions}
          handleFilterChange={handleFilterChange}
          reset={resetFilters}
          form={form}
        />
      </Col>
      <Col md={9} lg={9}>
        <Stack direction="horizontal" className="mb-3" gap={2}>
          <Form.Control
            type="text"
            placeholder={t("doctorDashboard.placeholder.search")}
            {...form.register("name")}
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
