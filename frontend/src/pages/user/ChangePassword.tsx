import { AxiosError } from "axios";
import { useState } from "react";
import { Button, Card, Col, Container, Form, Row } from "react-bootstrap";
import { PasswordForm } from "../../api/password/Password";
import { useUser } from "../../context/UserContext";
import { useUpdatePassword } from "../../hooks/passwordHoooks";

const ChangePassword = () => {
  const { user, loading, isDoctor } = useUser();

  const [formData, setFormData] = useState<PasswordForm>({
    oldPassword: "",
    password: "",
    confirmPassword: "",
  });

  // TODO
  const onSuccess = () => {
    alert("Password changed successfully");
  };

  const onError = (error: AxiosError) => {
    const body = JSON.stringify(error.response?.data);
    alert(`Error: ${body}`);
  };

  const id = user ? String(user.id) : "";

  const updatePasswordMutation = useUpdatePassword(
    id,
    !!isDoctor ? isDoctor : false,
    onSuccess,
    onError,
  );

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    updatePasswordMutation.mutate(formData);
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <Container className="generalPadding">
      <h1>Change Password</h1>
      <Card>
        <Form className="profileContainer" onSubmit={handleSubmit}>
          <Row className="profileRow">
            <Col className="profileItem">
              <Form.Label htmlFor="password">Password</Form.Label>
              <Form.Control
                id="password"
                name="password"
                type="password"
                value={formData.password}
                onChange={(e) => {
                  setFormData((prev) => ({
                    ...prev,
                    password: e.target.value,
                  }));
                }}
              />
            </Col>
          </Row>

          <Row className="profileRow">
            <Col className="profileItem">
              <Form.Label htmlFor="repeatpassword">Repeat Password</Form.Label>
              <Form.Control
                id="repeatpassword"
                name="repeatpassword"
                type="password"
                value={formData.confirmPassword}
                onChange={(e) => {
                  setFormData((prev) => ({
                    ...prev,
                    confirmPassword: e.target.value,
                  }));
                }}
              />
            </Col>
          </Row>
          <Row className="profileRow">
            <Col className="profileItem">
              <Form.Label htmlFor="oldpassword">Old Password</Form.Label>
              <Form.Control
                id="oldpassword"
                name="oldpassword"
                type="password"
                value={formData.oldPassword}
                onChange={(e) => {
                  setFormData((prev) => ({
                    ...prev,
                    oldPassword: e.target.value,
                  }));
                }}
              />
            </Col>
          </Row>

          <div className="d-grid gap-2">
            <Button variant="primary" type="submit" className="submitButton" disabled={updatePasswordMutation.isPending}>
              {updatePasswordMutation.isPending ? "Loading..." : "Change Password"}
            </Button>
          </div>
        </Form>
      </Card>
    </Container>
  );
};

export default ChangePassword;
