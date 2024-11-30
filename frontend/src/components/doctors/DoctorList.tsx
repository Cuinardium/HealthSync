import React from "react";
import {Button, Col, Image, Row, Spinner, Stack} from "react-bootstrap";
import { Doctor, DoctorQuery } from "../../api/doctor/Doctor";
import { useDoctors } from "../../hooks/doctorHooks";
import Loader from "../Loader";

import doctorDefault from "../../img/doctorDefault.png";
import { Link } from "react-router-dom";
import DoctorCard from "./DoctorCard";
import {useTranslation} from "react-i18next";

interface DoctorListProps {
  query: DoctorQuery;
  onPageChange: (page: number) => void;
}

const DoctorList: React.FC<DoctorListProps> = ({ query, onPageChange }) => {

  const {t} = useTranslation();

  const {
    data: doctors,
    isLoading,
    error,
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage,
  } = useDoctors(query);

  if (isLoading) {
    // TODO
    return <Loader />;
  }

  if (error) {
    // TODO
    return <div>Error fetching doctors: {error?.message}</div>;
  }

  if (
    !doctors ||
    doctors.pages.length === 0 ||
    doctors.pages[0].content.length === 0
  ) {
    // TODO
    return <div>No doctors found</div>;
  }

  return (
    <div>
      <Row direction="vertical" gap={3}>
        {doctors.pages.map((page, index) => (
          <React.Fragment key={index}>
            {page.content.map((doctor: Doctor) => (
              <Col xs={6}>
                <DoctorCard doctor={doctor} key={doctor.id} />
              </Col>
            ))}
          </React.Fragment>
        ))}
      </Row>

      {/* Pagination Controls */}
      {/* Load more */}
      <div style={{ marginTop: "20px", textAlign: "center" }}>
        {hasNextPage && (
            <Button
                variant="outline-primary"
                onClick={() => fetchNextPage()}
                disabled={isFetchingNextPage}
            >
              {isFetchingNextPage ? (
                  <>
                    <Spinner
                        as="span"
                        animation="border"
                        size="sm"
                        role="status"
                        aria-hidden="true"
                    />{" "}
                    {t("appointments.loadingMore")}
                  </>
              ) : (
                  t("appointments.loadMore")
              )}
            </Button>
        )}
      </div>
    </div>
  );
};

export default DoctorList;
