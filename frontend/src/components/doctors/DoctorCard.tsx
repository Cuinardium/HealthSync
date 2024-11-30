import React from "react";
import { Badge, Button, Card, Image, Stack } from "react-bootstrap";
import { Link } from "react-router-dom";
import { Doctor } from "../../api/doctor/Doctor";

import doctorDefault from "../../img/doctorDefault.png";
import { useTranslation } from "react-i18next";
import Rating from "./Rating";

interface DoctorCardProps {
  doctor: Doctor;
}

const DoctorCard: React.FC<DoctorCardProps> = ({ doctor }) => {
  const { t } = useTranslation();

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
        <Card.Text>
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
              <Badge pill bg="primary" className="chip">
                {t(`healthInsurance.${healthInsurance}`)}
              </Badge>
            ))}
          </Stack>
        </Card.Text>
        <Card.Text>
          <Stack direction="horizontal" gap={3}>
            <strong>{t("detailedDoctor.specialties")}:</strong> <Badge pill bg="primary"> {t(`specialty.${doctor.specialty}`)}</Badge>
          </Stack>
        </Card.Text>
        {(doctor.rating && doctor.ratingCount) ? (
          <Rating rating={doctor.rating} count={doctor.ratingCount}/>
        ): (
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
