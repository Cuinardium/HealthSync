import React, { useState } from "react";
import VacationList from "../../components/vacations/VacationList";
import { useAuth } from "../../context/AuthContext";
import VacationForm from "../../components/vacations/VacationForm";
import {
  Breadcrumb,
  Button,
  Col,
  Container,
  Modal,
  Spinner,
  Stack,
} from "react-bootstrap";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";

const DoctorVacations: React.FC = () => {
  const { t } = useTranslation();
  const { loading, id } = useAuth();

  const [showCreateForm, setShowCreateForm] = useState(false);

  const pageSize = 10;

  if (loading) {
    return (
      <Container className="d-flex justify-content-center align-items-center mt-5">
        <Spinner animation="border" role="status" variant="primary"></Spinner>
      </Container>
    );
  }

  return (
    <Container className="justify-content-center mt-5 mb-5">
      <Col md={8} lg={9}>
        <Breadcrumb>
          <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/doctor-profile" }}>
            {t("profile.profile")}
          </Breadcrumb.Item>
          <Breadcrumb.Item active>{t("vacation.title")}</Breadcrumb.Item>
        </Breadcrumb>
        <h1>{t("vacation.title")}</h1>
        <Stack className="mb-3" direction="horizontal" gap={5}>
          <h5 className="text-muted">{t("vacation.description")}</h5>
          <Button
            variant="outline-primary"
            className="ms-auto"
            onClick={() => setShowCreateForm(true)}
            active={showCreateForm}
          >
            {t("vacation.add")}
          </Button>
        </Stack>
        {/* Vacation list */}
        <VacationList doctorId={id as any} pageSize={pageSize} />
      </Col>

      <Modal
        size="lg"
        show={showCreateForm}
        onHide={() => setShowCreateForm(false)}
        backdrop="static"
      >
        <Modal.Header closeButton>
          <Modal.Title>{t("vacation.addTitle")}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <VacationForm doctorId={id as any} />
        </Modal.Body>
      </Modal>
    </Container>
  );
};

export default DoctorVacations;
