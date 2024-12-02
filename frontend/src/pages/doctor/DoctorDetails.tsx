import { Link, useNavigate, useParams } from "react-router-dom";
import { Breadcrumb, Card, Col, Container, Image } from "react-bootstrap";
import { useDoctor } from "../../hooks/doctorHooks";

import doctorDefault from "../../img/doctorDefault.png";
import ReviewList from "../../components/reviews/ReviewList";
import { useState } from "react";
import ReviewForm from "../../components/reviews/ReviewForm";
import { useUser } from "../../context/UserContext";
import AppointmentForm from "../../components/appointments/AppointmentForm";
import { useTranslation } from "react-i18next";
import DetailedDoctorCard from "../../components/doctors/DetailedDoctorCard";

const DoctorDetails: React.FC = () => {
  const { id: doctorId } = useParams<{ id: string }>(); // get the doctor id from the URL
  const navigate = useNavigate();
  const { t } = useTranslation();

  const {
    data: doctor,
    isLoading,
    isError,
    error,
  } = useDoctor(doctorId as string);

  const { user, isDoctor, loading } = useUser();

  if (
    !doctor ||
    !doctorId ||
    isNaN(+Number(doctorId)) ||
    Number(doctorId) < 0
  ) {
    navigate("/404");
    return null;
  }

  if (isLoading || loading) {
    // TODO
    return <div>Loading...</div>;
  }

  if (isError) {
    if (error?.response?.status === 404) {
      navigate("/404");
      return null;
    }
  }

  if (!doctor) {
    navigate("/404");
    return null;
  }

  return (
    <Container className="d-flex justify-content-center align-items-center mt-5 mb-5">
      <Col md={9} lg={9}>
        <Breadcrumb>
          <Breadcrumb.Item
            linkAs={Link}
            linkProps={{ to: "/doctor-dashboard" }}
          >
            {t("doctorDashboard.title")}
          </Breadcrumb.Item>
          <Breadcrumb.Item active>{t("detailedDoctor.title")}</Breadcrumb.Item>
        </Breadcrumb>
        <h1>{t("detailedDoctor.title")}</h1>
        <DetailedDoctorCard doctor={doctor} />
        {user && !isDoctor && (
          <Card className="mb-3">
            <Card.Body>
              <AppointmentForm doctorId={doctorId} />
            </Card.Body>
          </Card>
        )}
        <ReviewList doctorId={doctorId} canReview={doctor.canReview} />
      </Col>
    </Container>
  );
};

export default DoctorDetails;
