import React from "react";
import Loader from "./Loader";
import { IndicationQuery } from "../api/indication/Indication";
import { useIndications } from "../hooks/indicationHooks";

interface IndicationListProps {
  appointmentId: string;
  page?: number;
  pageSize?: number;
  onPageChange: (page: number) => void;
}

const IndicationList: React.FC<IndicationListProps> = ({
  appointmentId,
  page = 1,
  pageSize = 10,
  onPageChange,
}) => {
  const query: IndicationQuery = {
    page,
    pageSize,
  };

  const {
    data: indications,
    isLoading,
    isError,
    error,
  } = useIndications(appointmentId, query);

  if (isLoading) {
    return <Loader />;
  }

  if (isError) {
    return <div>Error fetching Indications: {error?.message}</div>;
  }

  if (!indications || indications.length === 0) {
    return <div>No indications found</div>;
  }

  return (
    <div>
      <h2>Indications</h2>
      <ul>
        {indications.map((indication) => {
          const fileUrl = indication.fileData
            ? URL.createObjectURL(indication.fileData.blob)
            : null;

          return (
            <li key={indication.id}>
              <div>
                <strong>Indication:</strong> {indication.description}
              </div>
              {fileUrl && (
                <div>
                  <strong>File:</strong>{" "}
                  <a
                    href={fileUrl}
                    download={indication.fileData?.fileName}
                    onClick={() => {
                      // Revoke the object URL after the file is downloaded
                      setTimeout(() => {
                        URL.revokeObjectURL(fileUrl);
                      }, 100);
                    }}
                  >
                    Download File
                  </a>
                </div>
              )}
              <div>
                <strong>Date:</strong>{" "}
                {new Date(indication.date).toLocaleDateString()}
              </div>
            </li>
          );
        })}
      </ul>

      {/* Pagination Controls */}
      <div style={{ marginTop: "20px" }}>
        <button disabled={page === 1} onClick={() => onPageChange(page - 1)}>
          Previous
        </button>
        <span style={{ margin: "0 10px" }}>Page {page}</span>
        <button
          onClick={() => onPageChange(page + 1)}
          disabled={indications.length < pageSize}
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default IndicationList;
