import React, { useState } from 'react';
import { Form, Button, Container, Card, Alert, Row, Col } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';

import Header from '../components/Header';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../css/main.css';
import '../css/forms.css';

const loginUrl= '/login';
const patientRegisterUrl= '/patient-register';

const Login = ({ hasError, error }) => {
    const { t } = useTranslation();
    const [form, setForm] = useState({ email: '', password: '', rememberMe: false });
    const [errors, setErrors] = useState({ email: '', password: '' });

    const handleChange = (e) => {
        const { name, value, checked, type } = e.target;
        setForm({
            ...form,
            [name]: type === 'checkbox' ? checked : value,
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        // TODO handle submit
    };

    return (
        <>
            <Header user={false} hasNotifications={false} isDoctor={false}/>

            <Container className="formContainer">
                <Row className="formRow">
                    <Col className="formCol">
                        <h1 className="text-center">{t('login.title')}</h1>
                        <Card>
                            <Card.Body>
                                <Form onSubmit={handleSubmit} action={loginUrl} method="POST">
                                    {hasError && (
                                        <Alert variant="danger">
                                            <p>{t('login.error')}</p>
                                        </Alert>
                                    )}
                                    <Form.Group className="formRow" controlId="formEmail">
                                        <div className="form-check">
                                        <Form.Label className="label">{t('form.email')}</Form.Label>
                                        <Form.Control
                                            className="form-input"
                                            type="text"
                                            name="email"
                                            value={form.email}
                                            onChange={handleChange}
                                            placeholder={t('form.email_hint')}
                                            isInvalid={!!errors.email}
                                        />
                                        <Form.Control.Feedback type="invalid">
                                            {errors.email}
                                        </Form.Control.Feedback>
                                        </div>
                                    </Form.Group>

                                    <Form.Group className="formRow" controlId="formPassword">
                                        <div className="form-check">
                                            <Form.Label className="label">{t('form.password')}</Form.Label>
                                            <Form.Control
                                                className="form-input"
                                                type="password"
                                                name="password"
                                                value={form.password}
                                                onChange={handleChange}
                                                placeholder={t('form.password_hint')}
                                                isInvalid={!!errors.password}
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {errors.password}
                                            </Form.Control.Feedback>
                                        </div>
                                    </Form.Group>

                                    <Form.Group className="formRow" controlId="formRememberMe">
                                        <Form.Check
                                            type="checkbox"
                                            name="rememberMe"
                                            checked={form.rememberMe}
                                            onChange={handleChange}
                                            label={t('login.rememberMe')}
                                        />
                                    </Form.Group>

                                    <div className="d-grid gap-2">
                                        <Button variant="primary" type="submit" className="submitButton">
                                            {t('login.submit')}
                                        </Button>
                                    </div>

                                    <div className="haveAccountRow mt-3 text-center">
                                        <p><b>{t('login.haveAccount')}&nbsp;</b></p>
                                        <a href={patientRegisterUrl}>{t('login.register')}</a>
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
