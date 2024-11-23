import { AxiosError } from "axios";
import { t } from "i18next";
import { useState } from "react";
import { Button, Card, Container, Form } from "react-bootstrap";
import { resendVerificationToken } from "../../api/token/tokenApi";

const ResendToken = () => {
  const [email, setEmail] = useState<string>("");

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    try {
      await resendVerificationToken(email);
      alert("verfication to email sent");
    } catch (error) {
      if (error instanceof AxiosError) {
        const body = JSON.stringify(error.response?.data);
        alert(`Error: ${body}`);
      }
    }
  };

  return (
    <Container>
      <h1>Resend Token</h1>
      <Card>
        <Card.Body>
          <Form onSubmit={handleSubmit}>
            <Form.Group className="formRow" controlId="formEmail">
              <div className="form-check">
                <Form.Label className="label">{t("form.email")}</Form.Label>
                <Form.Control
                  className="form-input"
                  type="text"
                  name="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder={t("form.email_hint")}
                />
              </div>
            </Form.Group>

            <Button variant="primary" type="submit">
              Resend token
            </Button>
          </Form>
        </Card.Body>
      </Card>
    </Container>
  );
};

export default ResendToken;
