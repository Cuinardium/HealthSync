import React from "react";
import { Modal } from "react-bootstrap";
import { useTranslation } from "react-i18next";

interface DoctorLocationMapProps {
  address: string;
  city: string;
  show: boolean;
  onHide: () => void;
}

const API_KEY = process.env.REACT_APP_GOOGLE_MAPS_API_KEY;

const DoctorLocationMap: React.FC<DoctorLocationMapProps> = ({
  address,
  city,
  show,
  onHide,
}) => {
  // El url del iframe de Google Maps
  const mapSrc = `https://www.google.com/maps/embed/v1/place?key=${API_KEY}&q=${encodeURIComponent(
    `${address}, ${city}`,
  )}`;

  const { t } = useTranslation();

  return (
    <div>
      <Modal show={show} onHide={onHide} size="xl">
        <Modal.Header closeButton>
          <Modal.Title>{t("profile.location")}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <iframe
            className="map"
            style={{ border: 0, width: "100%", height: "800px" }}
            referrerPolicy="no-referrer-when-downgrade"
            src={mapSrc}
            allowFullScreen
            title={t("profile.location")}
          ></iframe>
        </Modal.Body>
      </Modal>
    </div>
  );
};

export default DoctorLocationMap;
