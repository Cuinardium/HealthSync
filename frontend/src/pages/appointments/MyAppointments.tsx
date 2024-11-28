import React, { useState } from "react";
import AppointmentsList from "../../components/appointments/AppointmentList";
import { useAuth } from "../../context/AuthContext";
import { Col, Container, Row, Spinner, Tab, Tabs } from "react-bootstrap";
import { useTranslation } from "react-i18next";
import {useSelectedTabContext} from "../../context/SelectedTabContext";
import AppointmentWithNotificationsList from "../../components/appointments/AppointmentWithNotificationsList";

const MyAppointments: React.FC = () => {
  const { loading, id } = useAuth();
  const { t } = useTranslation();

  const {selectedTab, setSelectedTab} = useSelectedTabContext();
  const pageSize = 3;


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
                onSelect={(k) => setSelectedTab(k || "today")}
                className="mb-3"
            >
              {/* Today Tab */}
              <Tab eventKey="today" title={t("appointments.today")}>
                <AppointmentsList
                    userId={id as any}
                    pageSize={pageSize}
                    status="CONFIRMED"
                />
              </Tab>

              {/* Upcoming Tab */}
              <Tab eventKey="upcoming" title={t("appointments.upcoming")}>
                <AppointmentsList
                    userId={id as any}
                    pageSize={pageSize}
                    status="CONFIRMED"
                />
              </Tab>

              {/* Cancelled Tab */}
              <Tab eventKey="cancelled" title={t("appointments.cancelled")}>
                <AppointmentsList
                    userId={id as any}
                    pageSize={pageSize}
                    status="CANCELLED"
                />
              </Tab>

              {/* Completed Tab */}
              <Tab eventKey="completed" title={t("appointments.completed")}>
                <AppointmentsList
                    userId={id as any}
                    pageSize={pageSize}
                    status="COMPLETED"
                />
              </Tab>

              {/* Notifications Tab */}
              <Tab eventKey="notifications" title={t("appointments.notification")}>
                <AppointmentWithNotificationsList userId={id as any} />
              </Tab>
            </Tabs>
          </Col>
      </Container>
  );
};

export default MyAppointments;
