import React, { FormEvent, useState } from "react";
import {
  Form,
  Button,
  Container,
  Card,
  Alert,
  Row,
  Col,
} from "react-bootstrap";
import { useTranslation } from "react-i18next";

import "bootstrap/dist/css/bootstrap.min.css";
import "../../css/main.css";
import "../../css/forms.css";
import { PatientRegisterForm } from "../../api/patient/Patient";
import { useHealthInsurances } from "../../hooks/healthInsuranceHooks";
import { useCreatePatient } from "../../hooks/patientHooks";
import { useNavigate } from "react-router-dom";
import { AxiosError } from "axios";

const patientRegisterUrl = "/patient-register";

const PatientRegister = ({
  hasError,
  error,
}: {
  hasError: boolean;
  error: any;
}) => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const [form, setForm] = useState<PatientRegisterForm>({
    name: "",
    lastname: "",
    email: "",
    password: "",
    confirmPassword: "",
    healthInsurance: "",
  });
  const [errors, setErrors] = useState({
    name: "",
    lastName: "",
    email: "",
    password: "",
    confirmPassword: "",
  });

  const { data: healthInsurances} = useHealthInsurances()

  const onSuccess = () => {
    alert("Patient created successfully")
    navigate("/register-successful")
  }

  const onError = (error: AxiosError) => {
    const body = JSON.stringify(error.response?.data)
    alert(`Error: ${body}`)
  }

  const createPatientMutation = useCreatePatient(onSuccess, onError);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, checked, type } = e.target;
    setForm({
      ...form,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    // TODO handle submit
    createPatientMutation.mutate(form);
  };

  return (
    <>
      <Container className="formContainer">
        <Row className="formRow">
          <Col className="formCol">
            <h1 className="text-center">{t("registerPatient.title")}</h1>
            <Card>
              <Card.Body>
                <Form
                  onSubmit={handleSubmit}
                  action={patientRegisterUrl}
                  method="POST"
                >
                  {hasError && (
                    <Alert variant="danger">
                      <p>{t("login.error")}</p>
                    </Alert>
                  )}
                  <Form.Group className="formRow" controlId="formName">
                    <div className="form-check">
                      <Form.Label className="label">
                        {t("form.name")}
                      </Form.Label>
                      <Form.Control
                        className="form-input"
                        type="text"
                        name="name"
                        value={form.name}
                        onChange={handleChange}
                        placeholder={t("form.name_hint")}
                        isInvalid={!!errors.name}
                      />
                      <Form.Control.Feedback type="invalid">
                        {errors.name}
                      </Form.Control.Feedback>
                    </div>
                  </Form.Group>
                  <Form.Group className="formRow" controlId="formLastName">
                    <div className="form-check">
                      <Form.Label className="label">
                        {t("form.lastname")}
                      </Form.Label>
                      <Form.Control
                        className="form-input"
                        type="text"
                        name="lastname"
                        value={form.lastname}
                        onChange={handleChange}
                        placeholder={t("form.lastname_hint")}
                        isInvalid={!!errors.lastName}
                      />
                      <Form.Control.Feedback type="invalid">
                        {errors.lastName}
                      </Form.Control.Feedback>
                    </div>
                  </Form.Group>
                  <Form.Group className="formRow" controlId="formEmail">
                    <div className="form-check">
                      <Form.Label className="label">
                        {t("form.email")}
                      </Form.Label>
                      <Form.Control
                        className="form-input"
                        type="text"
                        name="email"
                        value={form.email}
                        onChange={handleChange}
                        placeholder={t("form.email_hint")}
                        isInvalid={!!errors.email}
                      />
                      <Form.Control.Feedback type="invalid">
                        {errors.email}
                      </Form.Control.Feedback>
                    </div>
                  </Form.Group>

                  <Form.Group className="formRow" controlId="formPassword">
                    <div className="form-check">
                      <Form.Label className="label">
                        {t("form.password")}
                      </Form.Label>
                      <Form.Control
                        className="form-input"
                        type="password"
                        name="password"
                        value={form.password}
                        onChange={handleChange}
                        placeholder={t("form.password_hint")}
                        isInvalid={!!errors.password}
                      />
                      <Form.Control.Feedback type="invalid">
                        {errors.password}
                      </Form.Control.Feedback>
                    </div>
                  </Form.Group>
                  <Form.Group
                    className="formRow"
                    controlId="formConfirmPassword"
                  >
                    <div className="form-check">
                      <Form.Label className="label">
                        {t("form.cpassword")}
                      </Form.Label>
                      <Form.Control
                        className="form-input"
                        type="password"
                        name="confirmPassword"
                        value={form.confirmPassword}
                        onChange={handleChange}
                        placeholder={t("form.cpassword_hint")}
                        isInvalid={!!errors.confirmPassword}
                      />
                      <Form.Control.Feedback type="invalid">
                        {errors.confirmPassword}
                      </Form.Control.Feedback>
                    </div>
                  </Form.Group>

                  <Row className="profileRow">
                    <Col className="profileItem">
                      <Form.Label htmlFor="healthInsurance">
                        Health Insurance
                      </Form.Label>
                      <Form.Control
                        id="healthInsurance"
                        name="healthInsurance"
                        as="select"
                        value={form.healthInsurance}
                        onChange={(e) => {
                          setForm((prev) => ({
                            ...prev,
                            healthInsurance: e.target.value,
                          }));
                        }}
                      >
                        {healthInsurances?.map((healthinsurance) => (
                          <option
                            key={healthinsurance.code}
                            value={healthinsurance.code}
                          >
                            {t(
                              `healthInsurance.${healthinsurance.code.replace(/_/g, ".").toLowerCase()}`,
                            )}
                          </option>
                        ))}
                      </Form.Control>
                    </Col>
                  </Row>

                  <div className="d-grid gap-2">
                    <Button
                      variant="primary"
                      type="submit"
                      className="submitButton"
                    >
                      {t("register.submit")}
                    </Button>
                  </div>
                </Form>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </>
  );
};

export default PatientRegister;
