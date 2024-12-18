import React from "react";
import AppointmentsList from "../../components/appointments/AppointmentList";
import { Col, Container, Spinner, Tab, Tabs } from "react-bootstrap";
import { useTranslation } from "react-i18next";
import { useSelectedTabContext } from "../../context/SelectedTabContext";
import AppointmentWithNotificationsList from "../../components/appointments/AppointmentWithNotificationsList";
import { useUser } from "../../context/UserContext";

const MyAppointments: React.FC = () => {
  const { loading, user, isDoctor } = useUser();
  const { t } = useTranslation();

  const { selectedTab, setSelectedTab } = useSelectedTabContext();
  const pageSize = 10;

  if (loading) {
    return (
      <Container className="d-flex justify-content-center align-items-center mt-5">
        <Spinner animation="border" role="status" variant="primary"></Spinner>
      </Container>
    );
  }

  return (
    <Container className="mt-5 mb-5 justify-content-center">
      <Col md={10} lg={10}>
        <Tabs
          id="appointments-tabs"
          activeKey={selectedTab}
          onSelect={(k) => setSelectedTab(k || "upcoming")}
          className="mb-3"
        >

          {/* Upcoming Tab */}
          <Tab eventKey="upcoming" title={t("appointments.upcoming")}>
            <AppointmentsList
              userId={user?.id as any}
              isDoctor={isDoctor ?? false}
              pageSize={pageSize}
              status="CONFIRMED"
              order="asc"
            />
          </Tab>

          {/* Cancelled Tab */}
          <Tab eventKey="cancelled" title={t("appointments.cancelled")}>
            <AppointmentsList
              userId={user?.id as any}
              isDoctor={isDoctor ?? false}
              pageSize={pageSize}
              status="CANCELLED"
              order="desc"
            />
          </Tab>

          {/* Completed Tab */}
          <Tab eventKey="completed" title={t("appointments.completed")}>
            <AppointmentsList
              userId={user?.id as any}
              isDoctor={isDoctor ?? false}
              pageSize={pageSize}
              status="COMPLETED"
              order="desc"
            />
          </Tab>

          {/* Notifications Tab */}
          <Tab eventKey="notifications" title={t("appointments.notification")}>
            <AppointmentWithNotificationsList
              userId={user?.id as any}
              isDoctor={isDoctor ?? false}
            />
          </Tab>
        </Tabs>
      </Col>
    </Container>
  );
};

export default MyAppointments;
