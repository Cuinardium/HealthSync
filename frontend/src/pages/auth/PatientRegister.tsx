import React from "react";
import {
  Form,
  Button,
  Container,
  Alert,
  Row,
  Col,
  ButtonGroup,
} from "react-bootstrap";
import { useTranslation } from "react-i18next";

import "bootstrap/dist/css/bootstrap.min.css";
import "../../css/main.css";
import "../../css/forms.css";
import { PatientRegisterForm } from "../../api/patient/Patient";
import { useHealthInsurances } from "../../hooks/healthInsuranceHooks";
import { useCreatePatient } from "../../hooks/patientHooks";
import { Link, useNavigate } from "react-router-dom";
import { AxiosError } from "axios";
import { SubmitHandler, useForm } from "react-hook-form";
import {
  validateConfirmPassword,
  validateEmail,
  validateHealthInsurance,
  validateLastname,
  validateName,
  validatePassword,
} from "../../api/validation/validations";

const PatientRegister = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    formState: { isSubmitting, errors },
    setError,
  } = useForm<PatientRegisterForm>();

  const { data: healthInsurances } = useHealthInsurances();

  const onSuccess = () => {
    navigate("/register-successful");
  };

  const onError = (error: AxiosError) => {
    if (error.response?.status === 409) {
      setError("email", {
        message: "register.emailInUse",
      });
    } else {
      setError("root", {
        message: "register.error",
      });
    }
  };

  const createPatientMutation = useCreatePatient(onSuccess, onError);

  const onSubmit: SubmitHandler<PatientRegisterForm> = (
    data: PatientRegisterForm,
  ) => {
    if (data.password !== data.confirmPassword) {
      setError("confirmPassword", {
        message: "validation.confirmPassword.match",
      });
      return;
    }
    createPatientMutation.mutate(data);
  };

  return (
    <>
      <Container className="justify-content-center mt-5">
        <Col md={8} lg={8}>
          <h1>{t("registerPatient.title")}</h1>
          <h5 className="text-muted mb-4">
            {t("registerPatient.description")}
          </h5>
          <Form onSubmit={handleSubmit(onSubmit)}>
            <Row className="mb-3">
              <Col>
                <Form.Group controlId="formName">
                  <Form.Label>{t("form.name")}</Form.Label>
                  <Form.Control
                    {...register("name", {
                      validate: validateName,
                    })}
                    type="text"
                    name="name"
                    placeholder={t("form.name_hint")}
                    isInvalid={!!errors.name}
                  />
                  <Form.Control.Feedback type="invalid">
                    {errors.name && t(errors.name.message ?? "")}
                  </Form.Control.Feedback>
                </Form.Group>
              </Col>
              <Col>
                <Form.Group controlId="formLastName">
                  <Form.Label>{t("form.lastname")}</Form.Label>
                  <Form.Control
                    {...register("lastname", {
                      validate: validateLastname,
                    })}
                    type="text"
                    name="lastname"
                    placeholder={t("form.lastname_hint")}
                    isInvalid={!!errors.lastname}
                  />
                  <Form.Control.Feedback type="invalid">
                    {errors.lastname && t(errors.lastname.message ?? "")}
                  </Form.Control.Feedback>
                </Form.Group>
              </Col>
            </Row>
            <Row className="mb-3">
              <Col>
                <Form.Group controlId="formEmail">
                  <Form.Label>{t("form.email")}</Form.Label>
                  <Form.Control
                    {...register("email", {
                      validate: validateEmail,
                    })}
                    type="text"
                    name="email"
                    placeholder={t("form.email_hint")}
                    isInvalid={!!errors.email}
                  />
                  <Form.Control.Feedback type="invalid">
                    {errors.email && t(errors.email.message ?? "")}
                  </Form.Control.Feedback>
                </Form.Group>
              </Col>
              <Col>
                <Form.Group>
                  <Form.Label htmlFor="healthInsurance">
                    {t("form.healthcare")}
                  </Form.Label>
                  <Form.Select
                    {...register("healthInsurance", {
                      validate: (value) =>
                        validateHealthInsurance(value, healthInsurances),
                    })}
                    id="healthInsurance"
                    name="healthInsurance"
                    isInvalid={!!errors.healthInsurance}
                  >
                    <option key="hint" disabled>{t("form.healthcare_hint")}</option>
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
                  </Form.Select>
                  <Form.Control.Feedback type="invalid">
                    {errors.healthInsurance &&
                      t(errors.healthInsurance.message ?? "")}
                  </Form.Control.Feedback>
                </Form.Group>
              </Col>
            </Row>

            <Row className="mb-3">
              <Col>
                <Form.Group controlId="formPassword">
                  <Form.Label className="label">
                    {t("form.password")}
                  </Form.Label>
                  <Form.Control
                    {...register("password", {
                      validate: validatePassword,
                    })}
                    type="password"
                    name="password"
                    placeholder={t("form.password_hint")}
                    isInvalid={!!errors.password}
                  />
                  <Form.Control.Feedback type="invalid">
                    {errors.password && t(errors.password.message ?? "")}
                  </Form.Control.Feedback>
                </Form.Group>
              </Col>
              <Col>
                <Form.Group controlId="formConfirmPassword">
                  <Form.Label className="label">
                    {t("form.cpassword")}
                  </Form.Label>
                  <Form.Control
                    {...register("confirmPassword", {
                      validate: validateConfirmPassword,
                    })}
                    type="password"
                    name="confirmPassword"
                    placeholder={t("form.cpassword_hint")}
                    isInvalid={!!errors.confirmPassword}
                  />
                  <Form.Control.Feedback type="invalid">
                    {errors.confirmPassword &&
                      t(errors.confirmPassword.message ?? "")}
                  </Form.Control.Feedback>
                </Form.Group>
              </Col>
            </Row>

            {errors.root && (
              <Alert variant="danger" className="text-center">
                {t(errors.root.message ?? "")}
              </Alert>
            )}

            <ButtonGroup className="d-flex mt-4">
              <Button variant="primary" type="submit" className="submitButton" disabled={isSubmitting}>
                {isSubmitting ? t("register.loading") : t("register.submit")}
              </Button>
            </ButtonGroup>
          </Form>

          <Row className="mt-3 justify-content-center">
            <Col md="auto" lg="auto">
              <b>{t("register.haveAccount")}</b>
            </Col>
            <Col md="auto" lg="auto">
              <Link to="/login">{t("register.login")}</Link>
            </Col>
          </Row>
        </Col>
      </Container>
    </>
  );
};

export default PatientRegister;
