import React from "react";
import {
  Form,
  Button,
  Container,
  Alert,
  Row,
  Col,
  ButtonGroup,
  Spinner,
} from "react-bootstrap";
import { useTranslation } from "react-i18next";

import "bootstrap/dist/css/bootstrap.min.css";

import { useSpecialties } from "../../hooks/specialtyHooks";
import { useHealthInsurances } from "../../hooks/healthInsuranceHooks";
import { AxiosError } from "axios";
import { useCreateDoctor } from "../../hooks/doctorHooks";
import { DoctorRegisterForm } from "../../api/doctor/Doctor";
import { Link, useNavigate } from "react-router-dom";
import { SubmitHandler, useForm } from "react-hook-form";
import {
  validateAddress,
  validateCity,
  validateConfirmPassword,
  validateEmail,
  validateLastname,
  validateName,
  validatePassword,
  validateSpecialty,
} from "../../api/validation/validations";
import { FaLocationDot, FaUser, FaUserDoctor } from "react-icons/fa6";

import HealthInsurancePicker from "../../components/doctors/HealthInsurancePicker";

const DoctorRegister = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    formState: { errors },
    setError,
    control,
  } = useForm<DoctorRegisterForm>({
    defaultValues: {
      specialty: "",
    },
  });

  const { data: specialties } = useSpecialties({
    sort: "standard",
    order: "asc",
  });
  const { data: healthInsurances } = useHealthInsurances({
    sort: "standard",
    order: "asc",
  });

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

  const createDoctorMutation = useCreateDoctor(onSuccess, onError);

  const onSubmit: SubmitHandler<DoctorRegisterForm> = (
    data: DoctorRegisterForm,
  ) => {
    if (data.password !== data.confirmPassword) {
      setError("confirmPassword", {
        message: "validation.confirmPassword.match",
      });
      return;
    }
    createDoctorMutation.mutate(data);
  };

  return (
    <>
      <Container className="justify-content-center mt-3">
        <Col md={8} lg={8}>
          <h1>{t("registerMedic.title")}</h1>
          <h5 className="text-muted mb-4 mt-4">
            {t("registerMedic.description")}
          </h5>
          <Form onSubmit={handleSubmit(onSubmit)}>
            <hr></hr>
            <h5 className="mb-3 mt-3">
              {t("profile.personalInfo")} <FaUser />
            </h5>

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
                  <Form.Label className="label">
                    {t("form.lastname")}
                  </Form.Label>
                  <Form.Control
                    {...register("lastname", {
                      validate: validateLastname,
                    })}
                    type="text"
                    name="lastname" // Fix the name here to match state
                    placeholder={t("form.lastname_hint")}
                    isInvalid={!!errors.lastname}
                  />
                  <Form.Control.Feedback type="invalid">
                    {errors.lastname && t(errors.lastname.message ?? "")}
                  </Form.Control.Feedback>
                </Form.Group>
              </Col>
              <Col>
                <Form.Group controlId="formEmail">
                  <Form.Label className="label">{t("form.email")}</Form.Label>
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
            </Row>

            <Row className="mb-3">
              <Col>
                <Form.Group controlId="formPassword">
                  <Form.Label>{t("form.password")}</Form.Label>
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
                  <Form.Label>{t("form.cpassword")}</Form.Label>
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

            <hr />
            <h5 className="mt-3 mb-3">
              {t("profile.location")} <FaLocationDot />
            </h5>

            <Row className="mb-3">
              <Col>
                <Form.Group controlId="formAddress">
                  <Form.Label>{t("form.address")}</Form.Label>
                  <Form.Control
                    {...register("address", {
                      validate: validateAddress,
                    })}
                    type="text"
                    name="address"
                    placeholder={t("form.address_hint")}
                    isInvalid={!!errors.address}
                  />
                  <Form.Control.Feedback type="invalid">
                    {errors.address && t(errors.address.message ?? "")}
                  </Form.Control.Feedback>
                </Form.Group>
              </Col>

              <Col>
                <Form.Group controlId="formCity">
                  <Form.Label>{t("form.city")}</Form.Label>
                  <Form.Control
                    {...register("city", {
                      validate: validateCity,
                    })}
                    type="text"
                    name="city"
                    placeholder={t("form.city_hint")}
                    isInvalid={!!errors.city}
                  />
                  <Form.Control.Feedback type="invalid">
                    {errors.city && t(errors.city.message ?? "")}
                  </Form.Control.Feedback>
                </Form.Group>
              </Col>
            </Row>

            <hr />
            <h5 className="mt-3 mb-3">
              {t("profile.workInfo")} <FaUserDoctor />
            </h5>

            <Row className="mb-3">
              <Col>
                <Form.Group controlId="formSpecialty">
                  <Form.Label>{t("form.specialization")}</Form.Label>
                  <Form.Select
                    {...register("specialty", {
                      validate: (value) =>
                        validateSpecialty(value, specialties),
                    })}
                    id="specialty"
                    name="specialty"
                    isInvalid={!!errors.specialty}
                  >
                    <option key="hint" value="" disabled>
                      {t("form.specialization_hint")}
                    </option>
                    {specialties?.map((specialty) => (
                      <option key={specialty.code} value={specialty.code}>
                        {t(
                          `specialty.${specialty.code.replace(/_/g, ".").toLowerCase()}`,
                        )}
                      </option>
                    ))}
                  </Form.Select>
                  <Form.Control.Feedback type="invalid">
                    {errors.specialty && t(errors.specialty.message ?? "")}
                  </Form.Control.Feedback>
                </Form.Group>
              </Col>

              <Col>
                <HealthInsurancePicker
                  healthInsurances={healthInsurances ?? []}
                  control={control}
                  errors={errors}
                />
              </Col>
            </Row>

            {errors.root && (
              <Alert variant="danger" className="text-center">
                {t(errors.root.message ?? "")}
              </Alert>
            )}

            <ButtonGroup className="d-flex mt-4">
              <Button
                variant="primary"
                type="submit"
                className="submitButton"
                disabled={createDoctorMutation.isPending}
              >
                {createDoctorMutation.isPending ? (
                  <>
                    <Spinner
                      as="span"
                      animation="border"
                      size="sm"
                      role="status"
                      aria-hidden="true"
                    />{" "}
                    {t("register.loading")}
                  </>
                ) : (
                  t("register.submit")
                )}
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

export default DoctorRegister;
