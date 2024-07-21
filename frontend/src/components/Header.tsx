import React from 'react';
import { Navbar, Nav, NavDropdown, Button, Image } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';

import 'bootstrap/dist/css/bootstrap.css';
import '../css/main.css'
import '../css/header.css'

// Define the API URLs or other variables
const homeUrl= '/';
const dashboardUrl= '/doctor-dashboard';
const myAppointmentsUrl= '/my-appointments';
const doctorRegisterUrl= '/doctor-register';
const patientRegisterUrl= '/patient-register';
const doctorProfileUrl= '/doctor-profile';
const patientProfileUrl= '/patient-profile';
const loginUrl= '/login';
const logoutUrl= '/logout';

const Header = ({ user, hasNotifications, isDoctor } : { user:any, hasNotifications:boolean, isDoctor:boolean }) => {
    const { t } = useTranslation();

    return (
            <header className="border-bottom">
                <div className="head">
                <a href={homeUrl} className="d-flex align-items-center">
                    <Image src='./img/logo.svg' alt="HealthSync logo" className="logo" />
                    <div className="health title">Health</div>
                    <div className="sync title">Sync</div>
                </a>
                    <div className="buttons">
                        <a href={dashboardUrl} className="headerLink">
                            {t('home.checkDoctor')}
                            <i className="fa-solid fa-user-doctor"></i>
                        </a>
                        {user && (
                            <>
                                <a href={myAppointmentsUrl} className="headerLink">
                                    {t('home.myAppointments')}
                                    <i className="fa-solid fa-calendar-check"></i>
                                </a>
                                {hasNotifications && <i className="fa-solid fa-circle notification"></i>}
                            </>
                        )}
                    </div>
                    {user ? (
                        <nav className="navbar navbar-expand-lg">
                            <div className="container-fluid">
                                {isDoctor ? (
                                    <>
                                        <i className="fa-solid fa-user-nurse iconIdentifier"></i>
                                    </>
                                ) : (
                                    <>
                                        <i className="fa-solid fa-user iconIdentifier"></i>
                                    </>
                                )}
                                <div id="profileDropdown">
                                    <ul className="navbar-nav">
                                        <li className="nav-item dropdown">
                                            <a
                                                className="nav-link dropdown-toggle"
                                                role="button"
                                                data-bs-toggle="dropdown"
                                                aria-haspopup="true"
                                                aria-expanded="false"
                                                data-toggle="tooltip"
                                                data-placement="top"
                                            >
                                                <Image
                                                    src='./img/doctorDefault.png'
                                                    alt={t('user.alt.loggedUserImg')}
                                                    width="40"
                                                    height="40"
                                                    className="rounded-circle"
                                                />
                                            </a>
                                            <ul className="dropdown-menu">
                                                {isDoctor ? (
                                                    <li>
                                                        <a className="dropdown-item icon" href={doctorProfileUrl}>
                                                            {t('header.profile')}
                                                        </a>
                                                    </li>
                                                ) : (
                                                    <li>
                                                        <a className="dropdown-item" href={patientProfileUrl}>
                                                            {t('header.profile')}
                                                        </a>
                                                    </li>
                                                )}
                                                <li>
                                                    <a className="dropdown-item" href={logoutUrl}>
                                                        {t('header.logout')}
                                                    </a>
                                                </li>
                                            </ul>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </nav>
                    ): (
                        <div className="buttons">
                            <a href={loginUrl} className="btn btn-primary" role="button">
                                {t('login.title')}
                            </a>

                            <nav className="navbar navbar-expand-lg">
                                <div className="container-fluid">
                                    <div id="navbarNavDropdown">
                                        <ul className="navbar-nav">
                                            <li className="nav-item dropdown">
                                                <button className="btn dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                                                    {t('register.title')}
                                                </button>
                                                <ul className="dropdown-menu">
                                                    <li>
                                                        <a className="dropdown-item" href={doctorRegisterUrl}>
                                                            {t('header.iAmDoctor')}
                                                        </a>
                                                    </li>
                                                    <li>
                                                        <a className="dropdown-item" href={patientRegisterUrl}>
                                                            {t('header.iAmPatient')}
                                                        </a>
                                                    </li>
                                                </ul>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </nav>
                        </div>
                    )}
                </div>
            </header>
                    );
                    };

                    export default Header;
