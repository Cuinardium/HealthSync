import React from 'react';
import { Button, Card, Form, Container, Row, Col, Image } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';

import { FaUser } from 'react-icons/fa6';

import Header from '../components/Header';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../css/main.css';
import '../css/forms.css';
import '../css/profile.css';
import {Patient} from "../models/Patient";

const PatientProfile = ({ patient, patientEditUrl, changePasswordUrl } :
                            { patient:Patient, patientEditUrl:string, changePasswordUrl:string }) => {
    const { t } = useTranslation();

    // const loggedUserImg = patient.image.imageId ? `/img/${patient.image.imageId}` : '/img/patientDefault.png';
    const loggedUserImg = '/img/patientDefault.png';

    return (
        <div>
            <Header user={true} hasNotifications={true} isDoctor={true} />

            <Container className="generalPadding">
                <h1>{t('profile.profile')}</h1>
                <Card>
                    <div className="profileContainer">
                        <div className="profileImageContainer">
                            <Image src={loggedUserImg} alt={t('user.alt.loggedUserImg')} width="200" height="200" className="rounded-circle" />
                        </div>

                        <div className="profileData">
                            <div className="profileTitle">
                                <strong>{t('profile.personalInfo')}</strong>
                                <FaUser />
                            </div>

                            <Row className="profileRow">
                                <Col className="profileItem">
                                    <Form.Label htmlFor="firstName">{t('form.name')}</Form.Label>
                                    <Form.Control id="firstName" type="text" value={patient.firstName} disabled />
                                </Col>
                                <Col className="profileItem">
                                    <Form.Label htmlFor="lastName">{t('form.lastname')}</Form.Label>
                                    <Form.Control id="lastName" type="text" value={patient.lastName} disabled />
                                </Col>
                            </Row>

                            <Row className="profileRow">
                                <Col className="profileItem">
                                    <Form.Label>{t('form.healthcare')}</Form.Label>
                                    <div className="chip">
                                        {/*{t(patient.healthInsurance.messageID)}*/}
                                    </div>
                                </Col>
                                <Col className="profileItem">
                                    <Form.Label htmlFor="email">{t('form.email')}</Form.Label>
                                    <Form.Control id="email" type="text" value={patient.email} disabled />
                                </Col>
                            </Row>

                            <Row className="profileRow">
                                <Col className="profileItem">
                                    <Form.Label>{t('form.locale')}</Form.Label>
                                    <div className="chip">
                                        {/*{patient.locale}*/}
                                    </div>
                                </Col>
                                <Col className="profileItem"></Col>
                            </Row>
                        </div>
                    </div>

                    <div className="profileButtonContainer">
                        <Button variant="primary" href={patientEditUrl}>{t('profile.edit')}</Button>
                        <Button variant="primary" href={changePasswordUrl}>{t('profile.changePassword')}</Button>
                    </div>
                </Card>
            </Container>
        </div>
    );
};

export default PatientProfile;
