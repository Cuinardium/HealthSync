import React, { useState } from "react";
import DoctorList from "../../components/doctors/DoctorList";
import { Button, Col, Container, Form, Stack } from "react-bootstrap";
import { FaMagnifyingGlass } from "react-icons/fa6";
import { useTranslation } from "react-i18next";

import DoctorFilters from "../../components/doctors/DoctorFilters";
import { useDoctorQueryContext } from "../../context/DoctorQueryContext";

const DoctorDashboard: React.FC = () => {
  const { t } = useTranslation();

  const { query, setName } = useDoctorQueryContext();

  const [searchName, setSearchName] = useState<string | undefined>(query.name);

  const applyName = () => {
    const name = searchName && searchName.length > 0 ? searchName : undefined;
    if (name) {
      setName(name);
    }
  };

  return (
    <Container className="d-flex justify-content-center align-items-start mt-5 mb-5">
      <Col className="me-5" md={3} lg={3}>
        <DoctorFilters onReset={() => setSearchName("")} />
      </Col>
      <Col md={9} lg={9}>
        <Stack direction="horizontal" className="mb-3" gap={2}>
          <Form.Control
            type="text"
            placeholder={t("doctorDashboard.placeholder.search")}
            value={searchName}
            onChange={(e) => setSearchName(e.target.value)}
          />
          <Button onClick={applyName} variant="primary">
            <FaMagnifyingGlass />
          </Button>
        </Stack>
        <DoctorList query={query} />
      </Col>
    </Container>
  );
};

export default DoctorDashboard;
