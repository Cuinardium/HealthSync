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

import Header from "../../components/Header";
import "bootstrap/dist/css/bootstrap.min.css";
import "../../css/main.css";
import "../../css/forms.css";
import { useSpecialties } from "../../hooks/specialtyHooks";

const patientRegisterUrl = "/patient-register";

const DoctorRegister = ({
  hasError,
  error,
}: {
  hasError: boolean;
  error: any;
}) => {
  const { t } = useTranslation();
  const [form, setForm] = useState({
    name: "",
    lastName: "",
    email: "",
    password: "",
    confirmPassword: "",
    address: "",
    city: "",
    specialty: "",
    healthInsurances: [] as number[], // Add health insurance state
  });
  const [errors, setErrors] = useState({
    name: "",
    lastName: "",
    email: "",
    password: "",
    confirmPassword: "",
    address: "",
    city: "",
    specialty: "",
    healthInsurances: "", // Add health insurance errors
  });

  const {
    data: specialties,
    isLoading,
    isError,
  } = useSpecialties({ sort: "standard", order: "asc" });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, checked, type } = e.target;
    setForm({
      ...form,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  const handleHealthInsuranceChange = (selectedCodes: number[]) => {
    setForm((prevForm) => ({
      ...prevForm,
      healthInsurances: selectedCodes,
    }));
  };

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    // TODO: handle form submission
  };

  return (
    <>
      <Container className="formContainer">
        <Row className="formRow">
          <Col className="formCol">
            <h1 className="text-center">{t("registerMedic.title")}</h1>
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
                        name="lastName" // Fix the name here to match state
                        value={form.lastName}
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

                  <Form.Group className="formRow" controlId="formAddress">
                    <div className="form-check">
                      <Form.Label className="label">
                        {t("form.address")}
                      </Form.Label>
                      <Form.Control
                        className="form-input"
                        type="text"
                        name="address"
                        value={form.address}
                        onChange={handleChange}
                        placeholder={t("form.address_hint")}
                        isInvalid={!!errors.address}
                      />
                      <Form.Control.Feedback type="invalid">
                        {errors.address}
                      </Form.Control.Feedback>
                    </div>
                  </Form.Group>

                  <Form.Group className="formRow" controlId="formCity">
                    <div className="form-check">
                      <Form.Label className="label">
                        {t("form.city")}
                      </Form.Label>
                      <Form.Control
                        className="form-input"
                        type="text"
                        name="city"
                        value={form.city}
                        onChange={handleChange}
                        placeholder={t("form.city_hint")}
                        isInvalid={!!errors.city}
                      />
                      <Form.Control.Feedback type="invalid">
                        {errors.city}
                      </Form.Control.Feedback>
                    </div>
                  </Form.Group>


                  <Form.Group className="formRow" controlId="formSpecialty">
                    <div className="form-check">
                      <Form.Label className="label">
                        {t("form.specialization")}
                      </Form.Label>
                      <Form.Control
                        as="select"
                        name="specialty"
                        value={form.specialty}
                        onChange={handleChange}
                        isInvalid={!!errors.specialty}
                      >
                        <option value="">
                          {t("form.specialization_hint")}
                        </option>
                        {isLoading ? (
                          <option>{t("form.loading")}</option>
                        ) : isError ? (
                          <option>{t("form.error_loading_specialties")}</option>
                        ) : (
                          specialties?.map((specialty) => (
                            <option key={specialty.name} value={specialty.name}>
                              {t(
                                `specialty.${specialty.code.replace(/_/g, '.').toLowerCase()}`,
                              )}
                            </option>
                          ))
                        )}
                      </Form.Control>
                      <Form.Control.Feedback type="invalid">
                        {errors.specialty}
                      </Form.Control.Feedback>
                    </div>
                  </Form.Group>

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

export default DoctorRegister;