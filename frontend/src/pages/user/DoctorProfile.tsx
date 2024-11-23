import React from "react";
import {
  Button,
  Card,
  Form,
  Container,
  Row,
  Col,
  Image,
} from "react-bootstrap";
import { useTranslation } from "react-i18next";

import { FaUserDoctor, FaCalendar, FaLocationDot } from "react-icons/fa6";

import "bootstrap/dist/css/bootstrap.min.css";
import "../../css/main.css";
import "../../css/forms.css";
import "../../css/profile.css";

import { useAuth } from "../../context/AuthContext";
import { useDoctor } from "../../hooks/doctorHooks";

import doctorDefault from "../../img/doctorDefault.png";
import { Link } from "react-router-dom";
import ScheduleViewer from "../../components/doctors/ScheduleViewer";

const DOCTOR_EDIT_URL = "/doctor-edit";
const CHANGE_SCHEDULE_URL = "/change-schedule";
const CHANGE_PASSWORD_URL = "/change-password";
const VACATION_URL = "/doctor-vacations";

const DoctorProfile = () => {
  const { t } = useTranslation();
  const { id } = useAuth();

  const result = useDoctor(id as string);
  const doctor = result.data;
  const isLoading = result.isLoading;

  return (
    <div>
      <Container className="formContainer generalPadding">
        <h1>{t("profile.profile")}</h1>
        <Card>
          <div className="profileContainer">
            <div className="profileImageContainer">
              <Image
                src={!isLoading && doctor?.image ? doctor.image : doctorDefault}
                alt={t("user.alt.loggedUserImg")}
                width="200"
                height="200"
                className="rounded-circle"
              />
            </div>

            <div className="profileData">
              <div className="profileTitle">
                <strong>{t("profile.personalInfo")}</strong>
                <FaUserDoctor />
              </div>

              <Row className="profileRow">
                <Col className="profileItem">
                  <Form.Label htmlFor="firstName">{t("form.name")}</Form.Label>
                  <Form.Control
                    id="firstName"
                    type="text"
                    value={doctor?.firstName}
                    disabled
                  />
                </Col>
                <Col className="profileItem">
                  <Form.Label htmlFor="lastName">
                    {t("form.lastname")}
                  </Form.Label>
                  <Form.Control
                    id="lastName"
                    type="text"
                    value={doctor?.lastName}
                    disabled
                  />
                </Col>
              </Row>

              <Row className="profileRow">
                <Col className="profileItem">
                  <Form.Label htmlFor="email">{t("form.email")}</Form.Label>
                  <Form.Control
                    id="email"
                    type="text"
                    value={doctor?.email}
                    disabled
                  />
                </Col>
                <Col className="profileItem">
                  <Form.Label>{t("form.locale")}</Form.Label>
                  <div className="chip">{doctor?.locale}</div>
                </Col>
              </Row>
            </div>
          </div>

          <hr />

          <div className="doctorData">
            <div className="profileTitle">
              <strong>{t("profile.location")}</strong>
              <FaLocationDot />
            </div>

            <Row className="profileRow">
              <Col className="profileItem">
                <Form.Label htmlFor="address">{t("form.address")}</Form.Label>
                <Form.Control
                  id="address"
                  type="text"
                  value={doctor?.address}
                  disabled
                />
              </Col>
              <Col className="profileItem">
                <Form.Label htmlFor="city">{t("form.city")}</Form.Label>
                <Form.Control
                  id="city"
                  type="text"
                  value={doctor?.city}
                  disabled
                />
              </Col>
            </Row>

            <hr />

            <div className="profileTitle">
              <strong>{t("profile.workInfo")}</strong>
              <FaUserDoctor />
            </div>

            <Row className="profileRow">
              <Col className="profileItem">
                <Form.Label htmlFor="specialtyCode">
                  {t("form.specialization")}
                </Form.Label>
                {
                  <Form.Control
                    id="specialtyCode"
                    type="text"
                    value={
                      !isLoading ? t(`specialty.${doctor?.specialty}`) : ""
                    }
                    disabled
                  />
                }
              </Col>
              <Col className="profileItem">
                <Form.Label>{t("form.healthcare")}</Form.Label>
                <div className="chipsContainer">
                  {doctor?.healthInsurances.map(
                    (healthInsurance: any, index: number) => (
                      <div key={index} className="chip">
                        {t(`healthInsurance.${healthInsurance}`)}
                      </div>
                    ),
                  )}
                </div>
              </Col>
            </Row>

            <hr />

            <div className="profileTitle">
              <strong>{t("profile.schedule")}</strong>
              <FaCalendar />
            </div>

            <div className="scheduleMargin">{doctor && <ScheduleViewer doctorId={String(doctor.id)}/>}</div>
          </div>

          <div className="profileButtonContainer">
            <Button variant="primary" as={Link as any} to={VACATION_URL}>
              {t("profile.viewVacations")}
            </Button>
            <Button variant="primary" as={Link as any} to={DOCTOR_EDIT_URL}>
              {t("profile.edit.title")}
            </Button>
            <Button variant="primary" as={Link as any} to={CHANGE_SCHEDULE_URL}>
              Change Schedule
            </Button>
            <Button variant="primary" as={Link as any} to={CHANGE_PASSWORD_URL}>
              {t("profile.changePassword")}
            </Button>
          </div>
        </Card>
      </Container>
    </div>
  );
};

export default DoctorProfile;


