import React from 'react';
import { Button, Card, Form, Container, Row, Col, Image } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';

import Header from '../components/Header';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../css/main.css';
import '../css/forms.css';
import '../css/profile.css';
import {Doctor} from "../models/Doctor";

const DoctorProfile = ({ doctor, vacationUrl, doctorEditUrl, changePasswordUrl, thirtyMinuteBlocks, days } :
                           { doctor:Doctor, vacationUrl:string, doctorEditUrl:string, changePasswordUrl:string, thirtyMinuteBlocks:any, days:any }) => {
    const { t } = useTranslation();

    // const loggedUserImg = doctor.image.imageId ? `/img/${doctor.image.imageId}` : '/img/doctorDefault.png';
    const loggedUserImg = '/img/doctorDefault.png';

    return (
        <div>
            <Header user={true} hasNotifications={true} isDoctor={true} />

            <Container className="formContainer generalPadding">
                <h1>{t('profile.profile')}</h1>
                <Card>
                    <div className="profileContainer">
                        <div className="profileImageContainer">
                            <Image src={loggedUserImg} alt={t('user.alt.loggedUserImg')} width="200" height="200" className="rounded-circle" />
                        </div>

                        <div className="profileData">
                            <div className="profileTitle">
                                <strong>{t('profile.personalInfo')}</strong>
                                <i className="fa-solid fa-user"></i>
                            </div>

                            <Row className="profileRow">
                                <Col className="profileItem">
                                    <Form.Label htmlFor="firstName">{t('form.name')}</Form.Label>
                                    <Form.Control id="firstName" type="text" value={doctor.firstName} disabled />
                                </Col>
                                <Col className="profileItem">
                                    <Form.Label htmlFor="lastName">{t('form.lastname')}</Form.Label>
                                    <Form.Control id="lastName" type="text" value={doctor.lastName} disabled />
                                </Col>
                            </Row>

                            <Row className="profileRow">
                                <Col className="profileItem">
                                    <Form.Label htmlFor="email">{t('form.email')}</Form.Label>
                                    <Form.Control id="email" type="text" value={doctor.email} disabled />
                                </Col>
                                <Col className="profileItem">
                                    <Form.Label>{t('form.locale')}</Form.Label>
                                    <div className="chip">
                                        {/*{doctor.locale}*/}
                                    </div>
                                </Col>
                            </Row>
                        </div>
                    </div>

                    <hr />

                    <div className="doctorData">
                        <div className="profileTitle">
                            <strong>{t('profile.location')}</strong>
                            <i className="fa-solid fa-location-dot"></i>
                        </div>

                        <Row className="profileRow">
                            <Col className="profileItem">
                                <Form.Label htmlFor="address">{t('form.address')}</Form.Label>
                                <Form.Control id="address" type="text" value={doctor.address} disabled />
                            </Col>
                            <Col className="profileItem">
                                <Form.Label htmlFor="city">{t('form.city')}</Form.Label>
                                <Form.Control id="city" type="text" value={doctor.city} disabled />
                            </Col>
                        </Row>

                        <hr />

                        <div className="profileTitle">
                            <strong>{t('profile.workInfo')}</strong>
                            <i className="fa-solid fa-user-doctor"></i>
                        </div>

                        <Row className="profileRow">
                            <Col className="profileItem">
                                <Form.Label htmlFor="specialtyCode">{t('form.specialization')}</Form.Label>
                                {/*<Form.Control id="specialtyCode" type="text" value={t(doctor.specialty.messageID)} disabled />*/}
                            </Col>
                            <Col className="profileItem">
                                <Form.Label>{t('form.healthcare')}</Form.Label>
                                <div className="chipsContainer">
                                    {doctor.healthInsurances.map((healthInsurance:any, index:number) => (
                                        <div key={index} className="chip">
                                            {t(healthInsurance.messageID)}
                                        </div>
                                    ))}
                                </div>
                            </Col>
                        </Row>

                        <hr />

                        <div className="profileTitle">
                            <strong>{t('profile.schedule')}</strong>
                            <i className="fa-solid fa-calendar"></i>
                        </div>

                        <div className="scheduleMargin">
                            {/* <scheduleViewer/> */}
                        </div>
                    </div>

                    <div className="profileButtonContainer">
                        <Button variant="primary" href={vacationUrl}>{t('profile.viewVacations')}</Button>
                        <Button variant="primary" href={doctorEditUrl}>{t('profile.edit')}</Button>
                        <Button variant="primary" href={changePasswordUrl}>{t('profile.changePassword')}</Button>
                    </div>
                </Card>
            </Container>
        </div>
    );
};

export default DoctorProfile;
