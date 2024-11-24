import React, {useState} from "react";
import {
    Form,
    Button,
    Container,
    Alert,
    Row,
    Col,
    ButtonGroup,
} from "react-bootstrap";
import {useTranslation} from "react-i18next";

import "bootstrap/dist/css/bootstrap.min.css";
import "../../css/main.css";
import "../../css/forms.css";
import {useAuth} from "../../context/AuthContext";
import {Link, useLocation, useNavigate} from "react-router-dom";
import {SubmitHandler, useForm} from "react-hook-form";
import {UserNotVerifiedError} from "../../api/auth/auth";

const RESEND_TOKEN_URL = "/resend-token"
const PATIENT_REGISTER_URL = "/patient-register";

interface LoginForm {
    email: string,
    password: string,
    rememberMe: boolean
}

const Login = () => {
    const {t} = useTranslation();
    const {register, watch, formState: {isSubmitting, errors}, handleSubmit, setError} = useForm<LoginForm>();
    const [isVerifiedError, setIsVerifiedError] = useState<boolean>();
    const email = watch("email");
    const password = watch("password");
    const {login} = useAuth();

    const navigate = useNavigate();
    const location = useLocation();
    const from = location.state?.from?.pathname || "/";


    const onSubmit: SubmitHandler<LoginForm> = async (data) => {

        try {
            // Call login function with provided username and password
            const {email, password, rememberMe} = data;

            await login(email, password, rememberMe);

            navigate(from, {replace: true});
        } catch (err) {
            setError('root', {
                message: "",
            })
            const a = err instanceof UserNotVerifiedError
            console.log(a)
            setIsVerifiedError(err instanceof UserNotVerifiedError);
        }
    };
    return (
        <>
            <Container className="justify-content-center mt-5">
                <Col md={6} lg={6}>
                    <h1>{t("login.title")}</h1>
                    <Form noValidate onSubmit={handleSubmit(onSubmit)}>
                        <Form.Group className="mb-3" controlId="formEmail">
                            <Form.Label>{t("form.email")}</Form.Label>
                            <Form.Control
                                {...register("email")}
                                size="lg"
                                type="email"
                                name="email"
                                placeholder={t("form.email_hint")}
                            />
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="formPassword">
                            <Form.Label>{t("form.password")}</Form.Label>
                            <Form.Control
                                {...register("password")}
                                size="lg"
                                type="password"
                                name="password"
                                placeholder={t("form.password_hint")}
                            />
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="formRememberMe">
                            <Form.Check
                                {...register("rememberMe")}
                                type="checkbox"
                                name="rememberMe"
                                label={t("login.rememberMe")}
                            />
                        </Form.Group>

                        {errors.root &&
                            (<Alert variant="danger" className="text-center">
                                {
                                    isVerifiedError ? (
                                            <Row className="justify-content-center">
                                                <Col md="auto" lg="auto" className="justify-content-center">

                                                        {t("login.verify")}

                                                </Col>
                                                <Col md="auto" lg="auto" className="align-content-center">
                                                    <Link to={RESEND_TOKEN_URL}>{t("login.verifyLink")}</Link>
                                                </Col>
                                            </Row>)
                                        : t("login.error")
                                }

                            </Alert>)}

                        <ButtonGroup className="d-flex">
                            <Button
                                variant="primary"
                                type="submit"
                                className="pt-2 pb-2"
                                disabled={isSubmitting || !email || !password}
                            >
                                {isSubmitting ? t("login.loading") : t("login.submit")}
                            </Button>
                        </ButtonGroup>

                        <Row className="mt-3 justify-content-center">
                            <Col md="auto" lg="auto">
                                <p>
                                    <b>{t("login.haveAccount")}&nbsp;</b>
                                </p>
                            </Col>
                            <Col md="auto" lg="auto">
                                <Link to={PATIENT_REGISTER_URL}>{t("login.register")}</Link>
                            </Col>
                        </Row>
                    </Form>
                </Col>
            </Container>
        </>
    );
};

export default Login;
