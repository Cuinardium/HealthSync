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
import { useAuth } from "../../context/AuthContext"
import { Link, useLocation, useNavigate } from "react-router-dom";

const patientRegisterUrl = "/patient-register";

const Login = () => {
  const { t } = useTranslation();
  const [form, setForm] = useState({
    email: "",
    password: "",
    rememberMe: false,
  });
  const [error, setError] = useState(false);
  const { login } = useAuth();

    const navigate = useNavigate();
    const location = useLocation();
    const from = location.state?.from?.pathname || "/";

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, checked, type } = e.target;
    setForm({
      ...form,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setError(false);

    try {
      // Call login function with provided username and password
      const {email, password, rememberMe } = form;
      
      await login(email, password, rememberMe);

      navigate(from, { replace: true });

    } catch (err) {
      setError(true)
    }
  };
  return (
    <>
      <Container className="formContainer">
        <Row className="formRow">
          <Col className="formCol">
            <h1 className="text-center">{t("login.title")}</h1>
            <Card>
              <Card.Body>
                <Form onSubmit={handleSubmit}>

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
                      />
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
                      />
                    </div>
                  </Form.Group>

                  {error && (
                    <Alert variant="danger">
                      <p>{t("login.error")}</p>
                    </Alert>
                  )}

                  <Form.Group className="formRow" controlId="formRememberMe">
                    <Form.Check
                      type="checkbox"
                      name="rememberMe"
                      checked={form.rememberMe}
                      onChange={handleChange}
                      label={t("login.rememberMe")}
                    />
                  </Form.Group>


                  <div className="d-grid gap-2">
                    <Button
                      variant="primary"
                      type="submit"
                      className="submitButton"
                    >
                      {t("login.submit")}
                    </Button>
                  </div>

                  <div className="haveAccountRow mt-3 text-center">
                    <p>
                      <b>{t("login.haveAccount")}&nbsp;</b>
                    </p>
                    <Link to={patientRegisterUrl}>{t("login.register")}</Link>
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

export default Login;


