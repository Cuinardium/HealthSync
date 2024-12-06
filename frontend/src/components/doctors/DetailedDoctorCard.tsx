import React from "react";
import { Card, Image, Stack, Row, Col, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";
import Rating from "./Rating";
import { Doctor } from "../../api/doctor/Doctor";
import doctorDefault from "../../img/doctorDefault.png";

import "./doctorCard.css";
import { useDoctorQueryContext } from "../../context/DoctorQueryContext";
import {
  FaAddressBook,
  FaCity,
  FaLocationDot,
  FaMapLocationDot,
} from "react-icons/fa6";
import { FaEnvelope } from "react-icons/fa";

interface DoctorCardProps {
  doctor: Doctor;
  onMapClick: () => void;
}

const DoctorCard: React.FC<DoctorCardProps> = ({ doctor, onMapClick }) => {
  const { t } = useTranslation();

  const navigate = useNavigate();
  const { addSpecialty, addHealthInsurance } = useDoctorQueryContext();

  const handleSpecialtyClick = (specialty: string) => {
    addSpecialty(specialty.toUpperCase().replace(/\./g, "_"));
    navigate("/doctor-dashboard");
  };

  const handleHealthInsuranceClick = (healthInsurance: string) => {
    addHealthInsurance(healthInsurance.toUpperCase().replace(/\./g, "_"));
    navigate("/doctor-dashboard");
  };

  return (
    <Card className="mb-4 shadow-sm doctor-card">
      <Card.Body>
        <div className="d-flex">
          <div className="d-flex align-items-start mb-3">
            <Image
              src={doctor.image ? doctor.image : doctorDefault}
              width="80"
              height="80"
              className="rounded-circle me-3"
              alt={`${doctor.firstName} ${doctor.lastName}`}
            />
            <div className="mt-1">
              <Card.Title>
                {doctor.firstName} {doctor.lastName}
              </Card.Title>
              {/* Rating */}
              {doctor.rating && doctor.ratingCount ? (
                <Rating rating={doctor.rating} count={doctor.ratingCount} />
              ) : (
                <Card.Subtitle className="text-muted">
                  {t("doctor.noReviews")}
                </Card.Subtitle>
              )}
            </div>
          </div>
        </div>
        <div>
          <div>
            <div className="d-flex flex-row justify-content-between mt-3">
              <div className="me-5 w-50">
                {/* Specialty */}
                <h5 className="mb-3">{t("detailedDoctor.specialties")}</h5>
                <Stack direction="horizontal" gap={2} className="mb-2">
                  <div
                    className="cardChip badge rounded-pill"
                    style={{
                      cursor: "pointer",
                    }}
                    onClick={() => handleSpecialtyClick(doctor.specialty)}
                  >
                    {" "}
                    {t(`specialty.${doctor.specialty}`)}
                  </div>
                </Stack>

                <h5 className="mb-2 mt-3">
                  {t(
                    doctor.healthInsurances.length > 1
                      ? "detailedDoctor.insurances"
                      : "detailedDoctor.insurance",
                  )}
                </h5>
                {/* Health Insurances */}
                <Stack direction="horizontal" gap={2} className="mb-2">
                  <Row>
                    {doctor.healthInsurances.map((insurance) => (
                      <Col key={insurance}>
                        <div
                          className="cardChip badge rounded-pill"
                          style={{
                            cursor: "pointer",
                          }}
                          onClick={() => handleHealthInsuranceClick(insurance)}
                        >
                          {t(`healthInsurance.${insurance}`)}
                        </div>
                      </Col>
                    ))}
                  </Row>
                </Stack>
              </div>
              <div className="d-flex flex-column me-5">
                <h5 className="d-flex flex-row align-items-center mb-2">
                  {t("detailedDoctor.modal.location")}
                  <FaMapLocationDot className="ms-1" />
                  <Button variant="link" onClick={onMapClick}>
                    {t("detailedDoctor.map")}
                  </Button>
                </h5>

                <Card.Subtitle className="text-muted mb-2">
                  <FaLocationDot /> {doctor.address}
                </Card.Subtitle>

                <Card.Subtitle className="text-muted mb-2">
                  <FaCity /> {doctor.city}
                </Card.Subtitle>

                {/* Email */}
                <h5 className="d-flex flex-row align-items-center mt-3 mb-2">
                  {t("detailedDoctor.contact")}
                  <FaAddressBook className="ms-1" />
                </h5>

                <Card.Subtitle className="text-muted mb-2">
                  <FaEnvelope /> {doctor.email}
                </Card.Subtitle>
              </div>
            </div>
          </div>
        </div>
      </Card.Body>
    </Card>
  );
};

export default DoctorCard;
