import React from "react";
import { Alert, Button, Col, Row, Spinner } from "react-bootstrap";
import { Doctor, DoctorQuery } from "../../api/doctor/Doctor";
import { useDoctors } from "../../hooks/doctorHooks";

import DoctorCard from "./DoctorCard";
import { useTranslation } from "react-i18next";
import DoctorCardPlaceholder from "./DoctorCardPlaceholder";
import {useUser} from "../../context/UserContext";

interface DoctorListProps {
  query: DoctorQuery;
}

const DoctorList: React.FC<DoctorListProps> = ({ query }) => {
  const { t } = useTranslation();

  const { isDoctor } = useUser();

  const {
    data: doctors,
    isLoading,
    error,
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage,
  } = useDoctors(query);

  if (isLoading) {
    return (
      <div>
        <Row>
          {[...Array(query.pageSize ?? 10)].map((_, index) => (
            <Col xs={12} sm={12} md={12} lg={6} key={index}>
              <DoctorCardPlaceholder />
            </Col>
          ))}
        </Row>
      </div>
    );
  }

  if (error) {
    return <Alert variant="danger">{t("doctorDashboard.error")}</Alert>;
  }

  if (
    !doctors ||
    doctors.pages.length === 0 ||
    doctors.pages[0].content.length === 0
  ) {
    return <Alert variant="info">{t("doctorDashboard.no.doctors")}</Alert>;
  }

  return (
    <div>
      <Row direction="vertical" gap={3}>
        {doctors.pages.map((page, index) => (
          <React.Fragment key={index}>
            {page.content.map((doctor: Doctor) => (
              <Col key={doctor.id} xs={12} sm={12} md={12} lg={6}>
                <DoctorCard doctor={doctor} isDoctor={isDoctor ?? false} />
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
