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

import { FaUser } from "react-icons/fa6";

import "bootstrap/dist/css/bootstrap.min.css";
import "../../css/main.css";
import "../../css/forms.css";
import "../../css/profile.css";
import { usePatient } from "../../hooks/patientHooks";
import { useAuth } from "../../context/AuthContext";

import patientDefault from "../../img/patientDefault.png";
import { Link } from "react-router-dom";

const PATIENT_EDIT_URL = "/patient-edit";
const CHANGE_PASSWORD_URL = "/change-password";

const PatientProfile = () => {
  const { t } = useTranslation();
  const { id } = useAuth();

  const result = usePatient(id as string);
  const patient = result.data;
  const isLoading = result.isLoading;

  return (
    <div>
      <Container className="justify-content-center mt-5">
        <Col md={8} lg={9}>
          <h1>{t("profile.profile")}</h1>
          <Card>
            <Card.Body>
              <Row>
                <Col className="d-flex justify-content-center align-items-center">
                  <Image
                    src={
                      !isLoading && patient?.image
                        ? patient.image
                        : patientDefault
                    }
                    alt={t("user.alt.loggedUserImg")}
                    roundedCircle
                    fluid
                    width="75%"
                  />
                </Col>

                <Col className="profileData">
                  <div className="profileTitle">
                    <strong>{t("profile.personalInfo")}</strong>
                    <FaUser />
                  </div>

                  <Row className="profileRow">
                    <Col className="profileItem">
                      <Form.Label htmlFor="firstName">
                        {t("form.name")}
                      </Form.Label>
                      <Form.Control
                        id="firstName"
                        type="text"
                        value={patient?.firstName}
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
                        value={patient?.lastName}
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
                        value={patient?.email}
                        disabled
                      />
                    </Col>
                  </Row>

                  <Row className="profileRow mb-5">
                    <Col className="profileItem">
                      <Form.Label>{t("form.healthcare")}</Form.Label>
                      <div className="chip">
                        {!isLoading
                          ? t(`healthInsurance.${patient?.healthInsurance}`)
                          : ""}
                      </div>
                    </Col>

                    <Col className="profileItem">
                      <Form.Label>{t("form.locale")}</Form.Label>
                      <div className="chip">
                        {t(`locale.${patient?.locale}`)}
                      </div>
                    </Col>
                  </Row>

                  <div className="profileButtonContainer mt-4">
                    <Button
                      variant="primary"
                      as={Link as any}
                      to={PATIENT_EDIT_URL}
                    >
                      {t("editProfile.title")}
                    </Button>
                    <Button
                      variant="primary"
                      as={Link as any}
                      to={CHANGE_PASSWORD_URL}
                    >
                      {t("profile.changePassword")}
                    </Button>
                  </div>
                </Col>
              </Row>
            </Card.Body>
          </Card>
        </Col>
      </Container>
    </div>
  );
};

export default PatientProfile;
