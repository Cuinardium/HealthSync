import React, { useEffect, useState } from "react";
import { Button, Card, Form, Container, Row, Col } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { useUpdatePatient } from "../../hooks/patientHooks";
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

const DoctorEdit = () => {
  const navigate = useNavigate();
  const { user, loading, isDoctor } = useUser();
  const { data: healthInsurances, isLoading: isLoadingHealhInsurances } =
    useHealthInsurances();
  const { data: specialties, isLoading: isLoadingSpecialties } = useSpecialties(
    { sort: "standard", order: "asc" },
  );
  const [formData, setFormData] = useState<DoctorEditForm>({
    name: "",
    lastname: "",
    healthInsurances: [],
    city: "",
    address: "",
    specialty: "",
    locale: "",
    image: undefined,
  });

  useEffect(() => {
    if (!loading && user && isDoctor) {
      const doctor = user as Doctor;
      setFormData({
        name: doctor.firstName,
        lastname: doctor.lastName,
        healthInsurances: doctor.healthInsurances.map((healthInsurance) =>
          healthInsurance.replace(/\./g, "_").toUpperCase(),
        ),
        city: doctor.city,
        address: doctor.address,
        specialty: doctor.specialty.replace(/\./g, "_").toUpperCase(),
        locale: doctor.locale,
      });
    }
  }, [user, loading]);

  // TODO
  const onSuccess = () => {
    alert("Profile updated successfully");
  };

  const onError = (error: AxiosError) => {
    const body = JSON.stringify(error.response?.data);
    alert(`Failed to update profile: ${error.message}, ${body}`);
  };

  const id = loading || !isDoctor ? "" : String(user!.id);

  const updateDoctorMutation = useUpdateDoctor(id, onSuccess, onError);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const { name, lastname, healthInsurances, locale } = formData;

    if (!name || !lastname || !healthInsurances || !locale) {
      alert("Please fill in all required fields.");
      return;
    }

    updateDoctorMutation.mutate(formData);
  };

  if (isLoadingHealhInsurances) {
    return <p>Loading...</p>;
  }

  return (
    <Container className="generalPadding">
      <h1>Edit Profile</h1>
      <Card>
        <Form onSubmit={handleSubmit}>
          <Row className="profileRow">
            <Col className="profileItem">
              <Form.Label htmlFor="name">First Name</Form.Label>
              <Form.Control
                id="name"
                name="name"
                type="text"
                value={formData.name}
                onChange={(e) => {
                  setFormData((prev) => ({
                    ...prev,
                    name: e.target.value,
                  }));
                }}
              />
            </Col>
            <Col className="profileItem">
              <Form.Label htmlFor="lastname">Last Name</Form.Label>
              <Form.Control
                id="lastname"
                name="lastname"
                type="text"
                value={formData.lastname}
                onChange={(e) => {
                  setFormData((prev) => ({
                    ...prev,
                    lastname: e.target.value,
                  }));
                }}
              />
            </Col>
          </Row>

          <Row className="profileRow">
            <Col className="profileItem">
              <Form.Label htmlFor="name">City</Form.Label>
              <Form.Control
                id="city"
                name="city"
                type="text"
                value={formData.city}
                onChange={(e) => {
                  setFormData((prev) => ({
                    ...prev,
                    city: e.target.value,
                  }));
                }}
              />
            </Col>
            <Col className="profileItem">
              <Form.Label htmlFor="address">Address</Form.Label>
              <Form.Control
                id="address"
                name="address"
                type="text"
                value={formData.address}
                onChange={(e) => {
                  setFormData((prev) => ({
                    ...prev,
                    address: e.target.value,
                  }));
                }}
              />
            </Col>
          </Row>

          <Row className="profileRow">
            <Col className="profileItem">
              <Form.Label htmlFor="healthInsurance">
                Health Insurance
              </Form.Label>
              <Form.Control
                id="healthInsurance"
                name="healthInsurance"
                as="select"
                multiple
                value={formData.healthInsurances}
                onChange={(e) => {
                  const newHealthInsurances =
                    formData.healthInsurances.includes(e.target.value)
                      ? formData.healthInsurances.filter(
                          (insurance) => insurance !== e.target.value,
                        )
                      : [...formData.healthInsurances, e.target.value];

                  setFormData((prev) => ({
                    ...prev,
                    healthInsurances: newHealthInsurances,
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

          <Row className="profileRow">
            <Col className="profileItem">
              <Form.Label htmlFor="specialty">Specialty</Form.Label>
              <Form.Control
                id="specialty"
                name="specialty"
                as="select"
                value={formData.specialty}
                onChange={(e) => {
                  setFormData((prev) => ({
                    ...prev,
                    specialty: e.target.value,
                  }));
                }}
              >
                {specialties?.map((specialty) => (
                  <option key={specialty.code} value={specialty.code}>
                    {t(
                      `specialty.${specialty.code.replace(/_/g, ".").toLowerCase()}`,
                    )}
                  </option>
                ))}
              </Form.Control>
            </Col>
          </Row>

          <Row className="profileRow">
            <Col className="profileItem">
              <Form.Label htmlFor="locale">Locale</Form.Label>
              <Form.Control
                id="locale"
                name="locale"
                type="text"
                value={formData.locale}
                onChange={(e) => {
                  setFormData((prev) => ({
                    ...prev,
                    locale: e.target.value,
                  }));
                }}
              />
            </Col>
            <Col className="profileItem">
              <Form.Label>Profile picture</Form.Label>
              <Form.Control
                type="file"
                onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                  const selectedFile = e.target.files?.[0];
                  if (selectedFile) {
                    setFormData((prev) => ({
                      ...prev,
                      image: selectedFile,
                    }));
                  }
                }}
              />
            </Col>
          </Row>

          <div className="profileButtonContainer">
            <Button
              variant="primary"
              type="submit"
              disabled={updateDoctorMutation.isPending}
            >
              Save Changes
            </Button>
            <Button
              variant="secondary"
              onClick={() => navigate("/doctor-profile")}
            >
              Cancel
            </Button>
          </div>
        </Form>
      </Card>
    </Container>
  );
};

export default DoctorEdit;
