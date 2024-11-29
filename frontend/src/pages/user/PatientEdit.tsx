import React, { useCallback, useEffect, useRef, useState } from "react";
import {
  Button,
  Card,
  Form,
  Container,
  Row,
  Col,
  Image,
  Breadcrumb,
  Alert,
  Spinner,
} from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { useUpdatePatient } from "../../hooks/patientHooks";

import "../../css/main.css";
import "../../css/forms.css";
import "../../css/profile.css";
import "../../css/profileEdit.css";

import { AxiosError } from "axios";
import { Patient, PatientEditForm } from "../../api/patient/Patient";
import { useUser } from "../../context/UserContext";
import { useHealthInsurances } from "../../hooks/healthInsuranceHooks";
import { t } from "i18next";

import patientDefault from "../../img/patientDefault.png";
import { FaUser } from "react-icons/fa6";
import { SubmitHandler, useForm } from "react-hook-form";
import {
  validateHealthInsurance,
  validateImage,
  validateLastname,
  validateLocale,
  validateName,
} from "../../api/validation/validations";

const PatientEdit = () => {
  const navigate = useNavigate();
  const { user, loading, isDoctor } = useUser();
  const { data: healthInsurances } = useHealthInsurances({
    sort: "standard",
    order: "asc"
  });
  const [imageUrl, setImageUrl] = useState<string>(patientDefault);
  const [showSuccess, setShowSuccess] = useState<boolean>(false);

  const fileInputRef = useRef<HTMLInputElement | null>(null);

  const handleButtonClick = () => {
    if (fileInputRef.current) {
      fileInputRef.current.click();
    }
  };

  const {
    register,
    formState: { errors },
    setValue,
    handleSubmit,
    watch,
    setError,
  } = useForm<PatientEditForm>();

  const name = watch("name");
  const lastname = watch("lastname");
  const healthInsurance = watch("healthInsurance");
  const locale = watch("locale");

  useEffect(() => {
    if (!loading && user && !isDoctor) {
      const patient = user as Patient;
      setValue("name", patient.firstName);
      setValue("lastname", patient.lastName);
      setValue(
        "healthInsurance",
        patient.healthInsurance.replace(/\./g, "_").toUpperCase(),
      );
      setValue("locale", patient.locale);

      setImageUrl(patient.image ? patient.image : patientDefault);
    }
  }, [user, loading, isDoctor, setValue]);

  const hasChanged = useCallback(() => {
    return (
      imageUrl !== (user?.image ?? patientDefault) ||
      name !== user?.firstName ||
      lastname !== user?.lastName ||
      healthInsurance !==
        (user as Patient)?.healthInsurance.replace(/\./g, "_").toUpperCase() ||
      locale !== user?.locale
    );
  }, [imageUrl, user, name, lastname, healthInsurance, locale]);

  const onError = (_: AxiosError) => {
    setError("root", {
      message: "profile.error",
    });
  };

  const onSuccess = () => {
    setShowSuccess(true);
  };

  const id = loading || isDoctor ? "" : String(user!.id);

  const updatePatientMutation = useUpdatePatient(id, onSuccess, onError);

  const onSubmit: SubmitHandler<PatientEditForm> = (data: PatientEditForm) => {
    const image = data.image;
    if (!(image instanceof File)) {
      data.image = undefined;
    }

    updatePatientMutation.mutate(data);
  };

  return (
    <Container className="justify-content-center mt-5">
      <Col md={8} lg={9}>
        <Breadcrumb>
          <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/patient-profile" }}>
            {t("profile.profile")}
          </Breadcrumb.Item>
          <Breadcrumb.Item active>{t("editProfile.title")}</Breadcrumb.Item>
        </Breadcrumb>
        <h1>{t("editProfile.title")}</h1>
        <Card className="mw-100 mh-100">
          <Card.Body>
            <Form onSubmit={handleSubmit(onSubmit)}>
              <Row className="mb-5">
                <Col className="d-flex flex-column justify-content-center align-items-center">
                  <div
                    style={{
                      width: "75%",
                      aspectRatio: "1",
                      overflow: "hidden",
                      borderRadius: "50%",
                    }}
                  >
                    <Image
                      src={imageUrl}
                      alt={t("user.alt.loggedUserImg")}
                      style={{
                        width: "100%",
                        height: "100%",
                        objectFit: "cover",
                      }}
                    />
                  </div>
                </Col>
                <Col className="d-flex flex-column justify-content-between">
                  <div className="profileTitle">
                    <strong>{t("profile.personalInfo")}</strong>
                    <FaUser />
                  </div>
                  <Row>
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

                  <Row>
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
                          {loading && (
                            <option key="hint" value="" disabled>
                              {t("form.healthcare_hint")}
                            </option>
                          )}
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

                    <Col>
                      <Form.Group>
                        <Form.Label htmlFor="locale">
                          {t("form.locale")}
                        </Form.Label>
                        <Form.Select
                          {...register("locale", {
                            validate: validateLocale,
                          })}
                          id="locale"
                          name="locale"
                          isInvalid={!!errors.locale}
                        >
                          <option key="en" value="en">
                            {t("locale.en")}
                          </option>
                          <option key="es" value="es">
                            {t("locale.es")}
                          </option>
                        </Form.Select>
                        <Form.Control.Feedback type="invalid">
                          {errors.locale && t(errors.locale.message ?? "")}
                        </Form.Control.Feedback>
                      </Form.Group>
                    </Col>
                  </Row>

                  {errors.root && (
                    <Row className="ms-5 me-5 mt-5">
                      <Alert variant="danger" className="text-center">
                        {t(errors.root.message ?? "")}
                      </Alert>
                    </Row>
                  )}
                  {showSuccess && (
                    <Row className="ms-5 me-5 mt-5">
                      <Alert
                        variant="primary"
                        dismissible
                        className="text-center"
                        onClose={() => setShowSuccess(false)}
                      >
                        {t("profile.success")}
                      </Alert>
                    </Row>
                  )}
                </Col>
              </Row>

              <Row className="mt-5">
                <Col className="d-flex flex-column align-items-center">
                  <Form.Group>
                    {/* Hidden File Input */}
                    <Form.Control
                      {...register("image", {
                        validate: (value: File | undefined) => {
                          const file =
                            value instanceof File ? value : undefined;
                          return validateImage(file);
                        },
                      })}
                      type="file"
                      accept="image/png, image/jpeg, image/jpg"
                      ref={(e: HTMLInputElement | null) => {
                        register("image").ref(e);
                        fileInputRef.current = e;
                      }}
                      style={{ display: "none" }}
                      onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                        const file = e.target.files?.[0];
                        if (file) {
                          setImageUrl(URL.createObjectURL(file));
                          setValue("image", file);
                        }
                      }}
                    />
                    {/* Trigger Button */}
                    <Button variant="primary" onClick={handleButtonClick}>
                      {t("form.image")}
                    </Button>
                    {/* Error Feedback */}
                    {errors.image && (
                      <Form.Control.Feedback
                        type="invalid"
                        style={{ display: "block" }}
                      >
                        {errors.image.message}
                      </Form.Control.Feedback>
                    )}
                  </Form.Group>
                </Col>
                <Col className="profileButtonContainer">
                  <Button
                    variant="primary"
                    type="submit"
                    disabled={updatePatientMutation.isPending || !hasChanged()}
                  >
                    {updatePatientMutation.isPending ? (
                      <>
                        <Spinner
                          as="span"
                          animation="border"
                          size="sm"
                          role="status"
                          aria-hidden="true"
                        />{" "}
                        {t("profile.loading")}
                      </>
                    ) : (
                      t("profile.saveChanges")
                    )}
                  </Button>
                  <Button
                    variant="secondary"
                    onClick={() => navigate("/patient-profile")}
                    disabled={updatePatientMutation.isPending}
                  >
                    {t("profile.cancel")}
                  </Button>
                </Col>
              </Row>
            </Form>
          </Card.Body>
        </Card>
      </Col>
    </Container>
  );
};

export default PatientEdit;
