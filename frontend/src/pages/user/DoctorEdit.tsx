import React, { useCallback, useEffect, useRef, useState } from "react";
import {
  Button,
  Card,
  Form,
  Container,
  Row,
  Col,
  Breadcrumb,
  Image,
  Alert,
  Spinner,
} from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import "../../css/main.css";
import "../../css/forms.css";
import "../../css/profile.css";
import { AxiosError } from "axios";
import { useUser } from "../../context/UserContext";
import { useHealthInsurances } from "../../hooks/healthInsuranceHooks";
import { t } from "i18next";
import { Doctor, DoctorEditForm } from "../../api/doctor/Doctor";
import { useUpdateDoctor } from "../../hooks/doctorHooks";
import { useSpecialties } from "../../hooks/specialtyHooks";
import { SubmitHandler, useForm } from "react-hook-form";
import doctorDefault from "../../img/doctorDefault.png";
import { FaLocationDot, FaUser, FaUserDoctor } from "react-icons/fa6";
import {
  validateAddress,
  validateCity,
  validateImage,
  validateLastname,
  validateLocale,
  validateName,
  validateSpecialty,
} from "../../api/validation/validations";
import HealthInsurancePicker from "../../components/doctors/HealthInsurancePicker";
import PlaceAutocomplete from "../../components/doctors/PlaceAutocomplete";

const DoctorEdit = () => {
  const navigate = useNavigate();
  const { user, loading, isDoctor } = useUser();
  const { data: healthInsurances } = useHealthInsurances({
    sort: "standard",
    order: "asc",
  });
  const { data: specialties } = useSpecialties({
    sort: "standard",
    order: "asc",
  });

  const [imageURL, setImageURL] = useState<string>(doctorDefault);
  const [showSuccess, setShowSuccess] = useState<boolean>(false);

  const [defaultAddress, setDefaultAddress] = useState<string | undefined>(
    undefined,
  );

  const fileInputRef = useRef<HTMLInputElement | null>(null);

  const handleButtonClick = () => {
    if (fileInputRef.current) {
      fileInputRef.current.click();
    }
  };

  const {
    register,
    formState: { errors, isSubmitting },
    setValue,
    handleSubmit,
    watch,
    setError,
    clearErrors,
    control,
  } = useForm<DoctorEditForm>();

  const name = watch("name");
  const lastname = watch("lastname");
  const city = watch("city");
  const address = watch("address");
  const specialty = watch("specialty");
  const locale = watch("locale");
  const doctorHealthInsurances = watch("healthInsurances");

  const hasChanged = useCallback(() => {
    const oldHealthInsurances = (user as Doctor)?.healthInsurances.map(
      (healthInsurance) => {
        return healthInsurance.replace(/\./g, "_").toUpperCase();
      },
    );

    const healthInsurancesChanged =
      doctorHealthInsurances?.sort().join(",") !==
      oldHealthInsurances?.sort().join(",");
    return (
      name !== user?.firstName ||
      lastname !== user?.lastName ||
      locale !== user?.locale ||
      imageURL !== (user?.image ?? doctorDefault) ||
      city !== (user as Doctor)?.city ||
      address !== (user as Doctor)?.address ||
      specialty !==
        (user as Doctor)?.specialty.replace(/\./g, "_").toUpperCase() ||
      healthInsurancesChanged
    );
  }, [
    imageURL,
    name,
    lastname,
    city,
    address,
    specialty,
    locale,
    doctorHealthInsurances,
    user,
  ]);

  useEffect(() => {
    if (!loading && user && isDoctor) {
      const doctor = user as Doctor;

      setValue("name", doctor.firstName);
      setValue("lastname", doctor.lastName);
      setValue("city", doctor.city);
      setValue("address", doctor.address);
      setValue("specialty", doctor.specialty.replace(/\./g, "_").toUpperCase());
      setValue("locale", doctor.locale);
      setValue(
        "healthInsurances",
        doctor.healthInsurances.map((healthInsurance) =>
          healthInsurance.replace(/\./g, "_").toUpperCase(),
        ),
      );

      setDefaultAddress(doctor.address);

      setImageURL(doctor.image ? doctor.image : doctorDefault);
    }
  }, [user, loading, isDoctor, setValue, healthInsurances, specialties]);

  useEffect(() => {
    const validation = validateAddress(address);
    if (isSubmitting) {
      if (validation !== true) {
        setError("address", {
          message: validation,
        });
      }
    }
  }, [isSubmitting, address, setError]);

  const handlePlaceSelect = (address: string, city: string) => {
    if (address) {
      setValue("address", address);
    }

    const validationAddress = validateAddress(address);
    if (validationAddress !== true) {
      setError("address", {
        message: validationAddress,
      });
    } else {
      clearErrors("address");
    }

    if (city) {
      setValue("city", city);
    }

    const validationCity = validateCity(city);
    if (validationCity !== true) {
      setError("city", {
        message: validationCity,
      });
    } else {
      clearErrors("city");
    }
  };

  const onSuccess = () => {
    setShowSuccess(true);
  };

  const onError = (error: AxiosError) => {
    setError("root", {
      message: "profile.error",
    });
  };

  const id = loading || !isDoctor ? "" : String(user!.id);

  const updateDoctorMutation = useUpdateDoctor(id, onSuccess, onError);

  const onSubmit: SubmitHandler<DoctorEditForm> = (data: DoctorEditForm) => {
    const image = data.image;
    if (!(image instanceof File)) {
      data.image = undefined;
    }

    updateDoctorMutation.mutate(data);
  };

  return (
    <Container className="justify-content-center mt-5">
      <Col md={8} lg={9}>
        <Breadcrumb>
          <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/doctor-profile" }}>
            {t("profile.profile")}
          </Breadcrumb.Item>
          <Breadcrumb.Item active>{t("editProfile.title")}</Breadcrumb.Item>
        </Breadcrumb>
        <h1>{t("editProfile.title")}</h1>

        <Card className="mw-100">
          <Card.Body>
            <Form onSubmit={handleSubmit(onSubmit)}>
              <Row>
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
                      src={imageURL}
                      alt={t("user.alt.loggedUserImg")}
                      style={{
                        width: "100%",
                        height: "100%",
                        objectFit: "cover",
                      }}
                    />
                  </div>
                  <div className="mt-5">
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
                            setImageURL(URL.createObjectURL(file));
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
                          {errors.image && t(errors.image.message?? "")}
                        </Form.Control.Feedback>
                      )}
                    </Form.Group>
                  </div>
                </Col>
                <Col>
                  <div className="d-flex flex-row align-items-center justify-content-center mt-3">
                    <h5 className="d-flex flex-row align-items-center">
                      {t("profile.personalInfo")} <FaUser className="ms-2" />
                    </h5>
                  </div>
                  <Row className="mb-3">
                    <Col>
                      <Form.Group>
                        <Form.Label>{t("form.name")}</Form.Label>
                        <Form.Control
                          {...register("name", {
                            validate: validateName,
                          })}
                          id="name"
                          name="name"
                          type="text"
                          placeholder={t("form.name_hint")}
                          isInvalid={!!errors.name}
                        />
                        <Form.Control.Feedback type="invalid">
                          {errors.name && t(errors.name.message ?? "")}
                        </Form.Control.Feedback>
                      </Form.Group>
                    </Col>
                    <Col>
                      <Form.Group>
                        <Form.Label>{t("form.lastname")}</Form.Label>
                        <Form.Control
                          {...register("lastname", {
                            validate: validateLastname,
                          })}
                          id="lastname"
                          name="lastname"
                          type="text"
                          placeholder={t("form.lastname_hint")}
                          isInvalid={!!errors.lastname}
                        />
                        <Form.Control.Feedback type="invalid">
                          {errors.lastname && t(errors.lastname.message ?? "")}
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

                  <div className="d-flex flex-row align-items-center justify-content-center mt-4">
                    <h5 className="d-flex flex-row align-items-center">
                      {t("profile.location")} <FaLocationDot className="ms-2" />
                    </h5>
                  </div>
                  <Row>
                    <Col>
                      <Form.Group>
                        <Form.Label>{t("form.address")}</Form.Label>
                        <PlaceAutocomplete
                          onPlaceSelect={handlePlaceSelect}
                          error={errors.address?.message}
                          defaultValue={defaultAddress}
                        />
                      </Form.Group>
                    </Col>
                    <Col>
                      <Form.Group>
                        <Form.Label>{t("form.city")}</Form.Label>
                        <Form.Control
                          {...register("city", {
                            validate: validateCity,
                          })}
                          id="city"
                          name="city"
                          type="text"
                          placeholder={t("form.city_hint")}
                          isInvalid={!!errors.city}
                          readOnly
                        />
                        <Form.Control.Feedback type="invalid">
                          {errors.city && t(errors.city.message ?? "")}
                        </Form.Control.Feedback>
                      </Form.Group>
                    </Col>
                  </Row>

                  <div className="d-flex flex-row align-items-center justify-content-center mt-4">
                    <h5 className="d-flex flex-row align-items-center">
                      {t("profile.workInfo")} <FaUserDoctor className="ms-2" />
                    </h5>
                  </div>
                  <Row>
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
                  </Row>

                  <Row>
                    <HealthInsurancePicker
                      healthInsurances={healthInsurances ?? []}
                      control={control}
                      errors={errors}
                    />
                  </Row>
                  <Row className="ms-2 mt-3 me-2">
                    {showSuccess && (
                      <Alert
                        variant="primary"
                        dismissible
                        onClose={() => setShowSuccess(false)}
                      >
                        {t("profile.success")}
                      </Alert>
                    )}
                    {errors.root && (
                      <div className="alert alert-danger" role="alert">
                        {t(errors.root.message ?? "")}
                      </div>
                    )}
                  </Row>
                </Col>

                <div className="profileButtonContainer mt-2">
                  <Button
                    variant="primary"
                    type="submit"
                    disabled={updateDoctorMutation.isPending || !hasChanged()}
                  >
                    {updateDoctorMutation.isPending ? (
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
                    onClick={() => navigate("/doctor-profile")}
                  >
                    {t("profile.cancel")}
                  </Button>
                </div>
              </Row>
            </Form>
          </Card.Body>
        </Card>
      </Col>
    </Container>
  );
};

export default DoctorEdit;
