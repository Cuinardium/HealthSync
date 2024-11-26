import { AxiosError } from "axios";
import { useState } from "react";
import {
  Alert,
  Breadcrumb,
  Button,
  ButtonGroup,
  Card,
  Col,
  Container,
  Form,
  Row,
} from "react-bootstrap";
import { PasswordForm } from "../../api/password/Password";
import { useUser } from "../../context/UserContext";
import { useUpdatePassword } from "../../hooks/passwordHoooks";

import "../../css/main.css";
import "../../css/forms.css";
import "../../css/profile.css";
import "../../css/profileEdit.css";
import { SubmitHandler, useForm } from "react-hook-form";
import {
  validateConfirmPassword,
  validatePassword,
} from "../../api/validation/validations";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";

const ChangePassword = () => {
  const { user, loading, isDoctor } = useUser();
  const { t } = useTranslation();

  const {
    register,
    handleSubmit,
    formState: { errors },
    setError,
  } = useForm<PasswordForm>();

  const [showSuccess, setShowSuccess] = useState<boolean>(false);

  const onSuccess = () => {
    setShowSuccess(true);
  };

  const onError = (error: AxiosError) => {
    if (error.response?.status === 400) {
      setError("oldPassword", {
        message: t("changePassword.invalidOldPassword"),
      });
    } else {
      setError("root", {
        message: t("changePassword.error"),
      });
    }
  };

  const id = user ? String(user.id) : "";

  const updatePasswordMutation = useUpdatePassword(
    id,
    !!isDoctor ? isDoctor : false,
    onSuccess,
    onError,
  );

  const onSubmit: SubmitHandler<PasswordForm> = (data: PasswordForm) => {
    if (data.password !== data.confirmPassword) {
      setError("confirmPassword", {
        message: t("validation.confirmPassword.match"),
      });
      return;
    }

    updatePasswordMutation.mutate(data);
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <Container className="justify-content-center mt-5">
      <Col md={8} lg={9}>
        <Breadcrumb>
          <Breadcrumb.Item
            linkAs={Link}
            linkProps={{
              to: isDoctor ? "/doctor-profile" : "/patient-profile",
            }}
          >
            {t("profile.profile")}
          </Breadcrumb.Item>
          <Breadcrumb.Item active>{t("changePassword.title")}</Breadcrumb.Item>
        </Breadcrumb>
        <h1>{t("changePassword.title")}</h1>
        <Card>
          <Card.Body>
            <h5 className="text-muted mt-3 mb-4">{t("changePassword.description")}</h5>
            <Form onSubmit={handleSubmit(onSubmit)}>
              <Row className="mb-3 mt-3">
                <Col>
                  <Form.Group controlId="oldPassword">
                    <Form.Label> {t("changePassword.oldPassword")}</Form.Label>
                    <Form.Control
                      {...register("oldPassword", {
                        required: t("validation.password.required"),
                      })}
                      id="oldPassword"
                      name="oldPassword"
                      type="password"
                      placeholder={t("changePassword.oldPassword_hint")}
                      isInvalid={!!errors.oldPassword}
                    />
                    <Form.Control.Feedback type="invalid">
                      {t(errors.oldPassword?.message ?? "")}
                    </Form.Control.Feedback>
                  </Form.Group>
                </Col>
              </Row>
              <Row className="mb-3">
                <Col>
                  <Form.Label> {t("changePassword.newPassword")}</Form.Label>
                  <Form.Control
                    {...register("password", { validate: validatePassword })}
                    id="password"
                    name="password"
                    type="password"
                    placeholder={t("changePassword.newPassword_hint")}
                    isInvalid={!!errors.password}
                  />
                  <Form.Control.Feedback type="invalid">
                    {t(errors.password?.message ?? "")}
                  </Form.Control.Feedback>
                </Col>
              </Row>

              <Row className="mb-3">
                <Col>
                  <Form.Group>
                    <Form.Label>
                      {t("changePassword.confirmPassword")}
                    </Form.Label>
                    <Form.Control
                      {...register("confirmPassword", {
                        validate: validateConfirmPassword,
                      })}
                      id="confirmPassword"
                      name="confirmPassword"
                      type="password"
                      placeholder={t("changePassword.confirmPassword_hint")}
                      isInvalid={!!errors.confirmPassword}
                    />
                    <Form.Control.Feedback type="invalid">
                      {t(errors.confirmPassword?.message ?? "")}
                    </Form.Control.Feedback>
                  </Form.Group>
                </Col>
              </Row>

              {showSuccess && (
                <Alert variant="primary" dismissible onClose={() => setShowSuccess(false)} className="mt-3 text-center">
                  {t("changePassword.success")}
                </Alert>
              )}

              <Col className="profileButtonContainer mt-5">
                <Button
                  variant="primary"
                  type="submit"
                  className="submitButton"
                  disabled={updatePasswordMutation.isPending}
                >
                  {updatePasswordMutation.isPending
                    ? t("changePassword.loading")
                    : t("changePassword.submit")}
                </Button>
                <Button
                    variant="secondary"
                    as={Link as any}
                    to={isDoctor ? "/doctor-profile" : "/patient-profile"}
                    className="cancelButton"
                >
                    {t("changePassword.cancel")}
                </Button>
              </Col>
            </Form>
          </Card.Body>
        </Card>
      </Col>
    </Container>
  );
};

export default ChangePassword;
