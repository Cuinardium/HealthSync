import React from 'react';
import { Navbar, Nav, NavDropdown, Button, Image } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';

const Header = ({ user, hasNotifications, isDoctor }) => {
    const { t } = useTranslation();

    const urls = {
        mainCss: '/css/main.css',
        headerCss: '/css/header.css',
        homeUrl: '/',
        dashboardUrl: '/doctor-dashboard',
        myAppointmentsUrl: '/my-appointments',
        doctorRegisterUrl: '/doctor-register',
        patientRegisterUrl: '/patient-register',
        doctorProfileUrl: '/doctor-profile',
        patientProfileUrl: '/patient-profile',
        loginUrl: '/login',
        logoutUrl: '/logout',
        logo: '/img/logo.svg'
    };

    return (
            <Navbar bg="light" expand="lg" className="border-bottom">
                <Navbar.Brand href={urls.homeUrl} className="d-flex align-items-center">
                    <img src={urls.logo} alt="HealthSync logo" className="logo" />
                    <div className="health title">Health</div>
                    <div className="sync title">Sync</div>
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Link href={urls.dashboardUrl}>
                            {t('home.checkDoctor')}
                            <i className="fa-solid fa-user-doctor ms-2"></i>
                        </Nav.Link>
                        {user && (
                            <>
                                <Nav.Link href={urls.myAppointmentsUrl}>
                                    {t('home.myAppointments')}
                                    <i className="fa-solid fa-calendar-check ms-2"></i>
                                </Nav.Link>
                                {hasNotifications && <i className="fa-solid fa-circle notification ms-2"></i>}
                            </>
                        )}
                    </Nav>
                    {user ? (
                        <Nav>
                            <NavDropdown
                                title={
                                    <div className="d-flex align-items-center">
                                        <i className={`fa-solid ${isDoctor ? 'fa-user-nurse' : 'fa-user'} iconIdentifier me-2`}></i>
                                        <Image src={urls.loggedUserImg} alt={t('user.alt.loggedUserImg')} roundedCircle width="40" height="40" />
                                    </div>
                                }
                                id="basic-nav-dropdown"
                            >
                                <NavDropdown.Item href={isDoctor ? urls.doctorProfileUrl : urls.patientProfileUrl}>
                                    {t('header.profile')}
                                </NavDropdown.Item>
                                <NavDropdown.Item href={urls.logoutUrl}>
                                    {t('header.logout')}
                                </NavDropdown.Item>
                            </NavDropdown>
                        </Nav>
                    ) : (
                        <Nav>
                            <Button href={urls.loginUrl} variant="primary" className="me-2">
                                {t('login.title')}
                            </Button>
                            <NavDropdown title={t('register.title')} id="basic-nav-dropdown">
                                <NavDropdown.Item href={urls.doctorRegisterUrl}>
                                    {t('header.iAmDoctor')}
                                </NavDropdown.Item>
                                <NavDropdown.Item href={urls.patientRegisterUrl}>
                                    {t('header.iAmPatient')}
                                </NavDropdown.Item>
                            </NavDropdown>
                        </Nav>
                    )}
                </Navbar.Collapse>
            </Navbar>
    );
};

export default Header;
