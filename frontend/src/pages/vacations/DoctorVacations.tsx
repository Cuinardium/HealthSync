import React, { useState } from "react";
import Loader from "../../components/Loader";
import VacationList from "../../components/vacations/VacationList";
import { useAuth } from "../../context/AuthContext";
import VacationForm from "../../components/vacations/VacationForm";
import {
  Breadcrumb,
  Button,
  Col,
  Container,
  Modal,
  Row,
  Stack,
} from "react-bootstrap";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";

const DoctorVacations: React.FC = () => {
  const { t } = useTranslation();
  const { loading, id } = useAuth();
  const [page, setPage] = useState(1);

  const [showCreateForm, setShowCreateForm] = useState(false);

  const pageSize = 3;

  if (loading) {
    return <Loader />;
  }

  return (
    <Container className="justify-content-center mt-5">
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
            onClick={() => setShowCreateForm(true)}
            active={showCreateForm}
          >
            {t("vacation.add")}
          </Button>
        </Stack>
        {/* Vacation list */}
        <VacationList
          doctorId={id as any}
          page={page}
          pageSize={pageSize}
          onPageChange={setPage}
        />
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
          <VacationForm doctorId={id as any} onCloseClicked={() => setShowCreateForm(false)} />
        </Modal.Body>
      </Modal>
    </Container>
  );
};

export default DoctorVacations;
