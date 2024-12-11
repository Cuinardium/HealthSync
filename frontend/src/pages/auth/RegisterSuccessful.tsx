import { useTranslation } from "react-i18next";
import { Card, Col, Container, Row } from "react-bootstrap";
import { Link } from "react-router-dom";
import { FaCheck } from "react-icons/fa";

const RegisterSuccessful = () => {
  const { t } = useTranslation();

  return (
    <Container className="justify-content-center mt-5">
      <Col md={6} lg={6}>
        <h1 className="mb-5">{t("register_success.title")}</h1>
        <Card>
          <Card.Body className="d-flex flex-column align-items-center m-5">
            <FaCheck size={150} className="backGroundSquareIcon mt-3 mb-4" />
            <Card.Title>
              <h2 className="text-center">
                {t("register_success.verify_account")}
              </h2>
            </Card.Title>
            <Card.Text>
              <h5 className="text-muted text-center">
                {t("register_success.description")}
              </h5>
              <Row className="mt-5">
                <Col className="d-flex justify-content-end">
                  <b className="text-end">
                    {t("register_success.didnt_get_mail")}
                  </b>
                </Col>
                <Col className="d-flex justify-content-center">
                  <Link to={"/resend-token"}>{t("register_success.resend_mail")}</Link>
                </Col>
              </Row>
            </Card.Text>
          </Card.Body>
        </Card>
      </Col>
    </Container>
  );
};

export default RegisterSuccessful;
