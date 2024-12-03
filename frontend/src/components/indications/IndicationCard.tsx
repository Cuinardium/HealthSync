import React from "react";
import { Card } from "react-bootstrap";
import { IndicationWithFileData } from "../../hooks/indicationHooks";
import {formatDatePretty} from "../../api/util/dateUtils";

interface IndicationCardProps {
  indication: IndicationWithFileData;
  isCreator: boolean;
}

const IndicationCard: React.FC<IndicationCardProps> = ({
  indication,
  isCreator,
}) => {
  const {  fileData, date } = indication;
  const fileUrl = fileData ? URL.createObjectURL(fileData.blob) : null;
  const descriptionLines = indication.description.split("\n");

  return (
    <div className="d-flex flex-column" style={{ maxWidth: "50%" }}>
      <Card
        className={`mb-3 shadow-sm ${isCreator ? "bg-primary text-light" : ""}`}
      >
        <Card.Body>
          <div>
            {descriptionLines.map((line, index) => (
                <div key={index}>{line}</div>
                ))}
            {fileUrl && (
              <a
                href={fileUrl}
                download={fileData?.fileName}
                onClick={() =>
                  setTimeout(() => URL.revokeObjectURL(fileUrl), 100)
                }
                className={isCreator ? "text-light" : ""}
              >
                <b>{fileData?.fileName}</b>
              </a>
            )}
          </div>
        </Card.Body>
      </Card>
      <div
        className={isCreator ? "text-muted text-end" : "text-muted text-start"}
      >
        {formatDatePretty(date)}
      </div>
    </div>
  );
};

export default IndicationCard;
