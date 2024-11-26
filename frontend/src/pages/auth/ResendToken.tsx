import {
  Alert,
  Button,
  ButtonGroup,
  Col,
  Container,
  FloatingLabel,
  Form,
  Modal,
  Spinner,
} from "react-bootstrap";
import { resendVerificationToken } from "../../api/token/tokenApi";

import "../../css/main.css";
import "../../css/forms.css";
import { SubmitHandler, useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import React, { useState } from "react";
import { useLocation } from "react-router-dom";

interface MailForm {
  email: string;
}

const ResendToken = () => {
  const { t } = useTranslation();
  const location = useLocation();
  const verificationError = location.state?.verificationError ?? false;
  const [showModal, setShowModal] = useState<boolean>(verificationError);
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting, isSubmitSuccessful },
    watch,
    setError,
  } = useForm<MailForm>();

  const email = watch("email");

  const onSubmit: SubmitHandler<MailForm> = async (data: MailForm) => {
    try {
      await resendVerificationToken(data.email);
    } catch (error) {
      setError("root", {
        message: t("resend.error"),
      });
    }
  };

  return (
    <Container className="justify-content-center mt-5">
      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton onHide={() => setShowModal(false)}>
          <Modal.Title>{t("resend.modal.title")}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Alert variant="danger">{t("resend.modal.body")}</Alert>
        </Modal.Body>
      </Modal>

      <Col md={6} lg={6} className="align-items-center">
        <h1>{t("resend.title")}</h1>
        <h5 className="text-muted">{t("resend.description")}</h5>
        <Form noValidate onSubmit={handleSubmit(onSubmit)}>
          <Form.Group className="mt-4 mb-3" controlId="formEmail">
            <FloatingLabel label={t("form.email")}>
              <Form.Control
                {...register("email")}
                type="text"
                name="email"
                placeholder={t("form.email_hint")}
              />
            </FloatingLabel>
          </Form.Group>

          {isSubmitSuccessful && (
            <Alert variant="primary" dismissible className="text-center">
              {t("resend.success")}
            </Alert>
          )}

          {errors.root && (
            <Alert variant="danger" className="text-center">
              {errors.root.message}
            </Alert>
          )}

          <ButtonGroup className="d-flex">
            <Button
              variant="primary"
              type="submit"
              className="pt-2 pb-2"
              disabled={isSubmitting || !email}
            >
              {isSubmitting ? (
                <>
                  <Spinner
                    as="span"
                    animation="border"
                    size="sm"
                    role="status"
                    aria-hidden="true"
                  />{" "}
                  {t("resend.submitting")}
                </>
              ) : (
                t("resend.button")
              )}
            </Button>
          </ButtonGroup>
        </Form>
      </Col>
    </Container>
  );
};

export default ResendToken;
