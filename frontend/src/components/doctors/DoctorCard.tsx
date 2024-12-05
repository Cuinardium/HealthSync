import React, { useEffect, useRef, useState } from "react";
import { Button, Card, Image, Stack } from "react-bootstrap";
import { Link } from "react-router-dom";
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

  const stackRef = useRef<HTMLDivElement>(null);
  const [isOverflowingLeft, setIsOverflowingLeft] = useState(false);
  const [isOverflowingRight, setIsOverflowingRight] = useState(false);

  const handleSpecialtyClick = (specialty: string) => {
    addSpecialty(specialty.toUpperCase().replace(/\./g, "_"));
  };

  const handleHealthInsuranceClick = (healthInsurance: string) => {
    addHealthInsurance(healthInsurance.toUpperCase().replace(/\./g, "_"));
  };

  const handleScrollLeft = () => {
    if (stackRef.current) {
      stackRef.current.scrollBy({ left: -200, behavior: "smooth" });
    }
  };

  const handleScrollRight = () => {
    if (stackRef.current) {
      stackRef.current.scrollBy({ left: 200, behavior: "smooth" });
    }
  };

  useEffect(() => {
    const checkOverflow = () => {
      if (stackRef.current) {
        const { scrollLeft, scrollWidth, clientWidth } = stackRef.current;

        setIsOverflowingLeft(scrollLeft > 0);

        setIsOverflowingRight(scrollWidth > clientWidth + scrollLeft);
      }
    };

    checkOverflow();
    window.addEventListener("resize", checkOverflow);

    const stack = stackRef.current;

    if (stack) {
      stack.addEventListener("scroll", checkOverflow);
    }

    return () => {
      window.removeEventListener("resize", checkOverflow);
      if (stack) {
        stack.removeEventListener("scroll", checkOverflow);
      }
    };
  }, []);

  return (
    <Card className="mb-3">
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
        <div className="mb-3 w-auto d-flex flex-row align-items-center">
          <strong className="me-2 text-nowrap">
            {t(
              doctor.healthInsurances.length > 1
                ? "detailedDoctor.insurances"
                : "detailedDoctor.insurance",
            )}
            :
          </strong>

          {/* Left Scroll Control */}
          {isOverflowingLeft && (
            <span
              className="scroll-left"
              onClick={handleScrollLeft}
              style={{
                cursor: "pointer",
              }}
            >
              &lt;
            </span>
          )}

          <Stack
            className="scrollable-stack"
            direction="horizontal"
            gap={3}
            ref={stackRef}
          >
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
          {/* Right Scroll Control */}
          {isOverflowingRight && (
            <span
              className="scroll-right"
              onClick={handleScrollRight}
              style={{
                cursor: "pointer",
              }}
            >
              &gt;
            </span>
          )}
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
