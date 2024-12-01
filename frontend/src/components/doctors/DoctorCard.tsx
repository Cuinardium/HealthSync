import React from "react";
import { Badge, Button, Card, Image, Stack } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { Doctor } from "../../api/doctor/Doctor";

import doctorDefault from "../../img/doctorDefault.png";
import { useTranslation } from "react-i18next";
import Rating from "./Rating";
import { useDoctorQueryContext } from "../../context/DoctorQueryContext";

import "./doctorCard.css";

interface DoctorCardProps {
  doctor: Doctor;
}

const DoctorCard: React.FC<DoctorCardProps> = ({ doctor }) => {
  const { t } = useTranslation();

  const { addSpecialty, addHealthInsurance } = useDoctorQueryContext();

  const handleSpecialtyClick = (specialty: string) => {
    addSpecialty(specialty.toUpperCase().replace(/\./g, "_"));
  };

  const handleHealthInsuranceClick = (healthInsurance: string) => {
    addHealthInsurance(healthInsurance.toUpperCase().replace(/\./g, "_"));
  };

  return (
    <Card className="mb-3 shadow-sm">
      <Card.Body>
        <div className="d-flex align-items-center mb-3">
          <Image
            src={doctor.image ? doctor.image : doctorDefault}
            width="50"
            height="50"
            className="rounded-circle me-3"
            alt={`${doctor.firstName} ${doctor.lastName}`}
          />
          <div>
            <Card.Title>
              {doctor.firstName} {doctor.lastName}
            </Card.Title>
            <Card.Subtitle className="text-muted">
              {doctor.city + ", " + doctor.address}
            </Card.Subtitle>
          </div>
        </div>
        <div className="mb-3">
          <Stack direction="horizontal" gap={3}>
            <strong>
              {t(
                doctor.healthInsurances.length > 1
                  ? "detailedDoctor.insurances"
                  : "detailedDoctor.insurance",
              )}
              :
            </strong>{" "}
            {doctor.healthInsurances.map((healthInsurance) => (
              <div
                key={healthInsurance}
                className="chip badge rounded-pill"
                style={{
                  cursor: "pointer",
                }}
                onClick={() => handleHealthInsuranceClick(healthInsurance)}
              >
                {t(`healthInsurance.${healthInsurance}`)}
              </div>
            ))}
          </Stack>
        </div>
        <div className="mb-3">
          <Stack direction="horizontal" gap={3}>
            <strong>{t("detailedDoctor.specialties")}:</strong>{" "}
            <div
              className="chip badge rounded-pill"
              style={{
                cursor: "pointer",
              }}
              onClick={() => handleSpecialtyClick(doctor.specialty)}
            >
              {" "}
              {t(`specialty.${doctor.specialty}`)}
            </div>
          </Stack>
        </div>
        {doctor.rating && doctor.ratingCount ? (
          <Rating rating={doctor.rating} count={doctor.ratingCount} />
        ) : (
          <div className="text-muted">{t("doctor.noReviews")}</div>
        )}
        <Stack direction="horizontal">
          <Button
            as={Link as any}
            to={`/detailed-doctor/${doctor.id}`}
            className="ms-auto"
            variant="outline-primary"
          >
            {t("doctorDashboard.button.book")}
          </Button>
        </Stack>
      </Card.Body>
    </Card>
  );
};

export default DoctorCard;
