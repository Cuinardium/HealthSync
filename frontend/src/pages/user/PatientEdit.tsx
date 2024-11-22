import React, { useEffect, useState } from "react";
import {
  Button,
  Card,
  Form,
  Container,
  Row,
  Col,
  Image,
} from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { useUpdatePatient } from "../../hooks/patientHooks";
import { useAuth } from "../../context/AuthContext";
import "../../css/main.css";
import "../../css/forms.css";
import "../../css/profile.css";
import { AxiosError } from "axios";
import { Patient, PatientEditForm } from "../../api/patient/Patient";
import { useUser } from "../../context/UserContext";
import { useHealthInsurances } from "../../hooks/healthInsuranceHooks";
import { t } from "i18next";

const PatientEdit = () => {
  const navigate = useNavigate();
  const { user, loading, isDoctor } = useUser();
  const { data: healthInsurances, isLoading: isLoadingHealhInsurances } =
    useHealthInsurances();
  const [formData, setFormData] = useState<PatientEditForm>({
    name: "",
    lastname: "",
    healthInsurance: "",
    locale: "",
    image: undefined,
  });

  useEffect(() => {
    if (!loading && user && !isDoctor) {
      const patient = user as Patient;
      setFormData({
        name: patient.firstName,
        lastname: patient.lastName,
        healthInsurance:  patient.healthInsurance.replace(/\./g, "_").toUpperCase(),
        locale: patient.locale,
      });
    }
  }, [user, loading]);

  // TODO
  const onSuccess = () => {
    alert("Profile updated successfully");
  };

  const onError = (error: AxiosError) => {
    const body = JSON.stringify(error.response?.data)
    alert(`Failed to update profile: ${error.message}, ${body}`);
  };

  const id = loading || isDoctor ? "" : String(user!.id);

  const updatePatientMutation = useUpdatePatient(id, onSuccess, onError);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const { name, lastname, healthInsurance, locale } = formData;

    if (!name || !lastname || !healthInsurance || !locale) {
      alert("Please fill in all required fields.");
      return;
    }

    updatePatientMutation.mutate(formData);
  };

  if (isLoadingHealhInsurances) {
    return <p>Loading...</p>;
  }

  return (
    <Container className="generalPadding">
      <h1>Edit Profile</h1>
      <Card>
        <Form className="profileContainer" onSubmit={handleSubmit}>
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
              <Form.Label htmlFor="healthInsurance">
                Health Insurance
              </Form.Label>
              <Form.Control
                id="healthInsurance"
                name="healthInsurance"
                as="select"
                value={formData.healthInsurance}
                onChange={(e) => {
                  setFormData((prev) => ({
                    ...prev,
                    healthInsurance: e.target.value,
                  }));
                }}
              >
                  {(
                  healthInsurances?.map((healthinsurance) => (
                    <option
                      key={healthinsurance.code}
                      value={healthinsurance.code}
                    >
                      {t(
                        `healthInsurance.${healthinsurance.code.replace(/_/g, ".").toLowerCase()}`,
                      )}
                    </option>
                  ))
                )}
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
              disabled={updatePatientMutation.isPending}
            >
              Save Changes
            </Button>
            <Button variant="secondary" onClick={() => navigate("/patient-profile")}>
              Cancel
            </Button>
          </div>
        </Form>
      </Card>
    </Container>
  );
};

export default PatientEdit;
