import React from "react";
import { Navbar, Nav, NavDropdown, Button, Image } from "react-bootstrap";
import { useTranslation } from "react-i18next";

import "bootstrap/dist/css/bootstrap.css";
import "../css/main.css";
import "../css/header.css";

import logo from "../img/logo.svg";
import {
  FaUser,
  FaUserNurse,
  FaUserDoctor,
  FaCalendarCheck,
  FaCircle,
} from "react-icons/fa6";
import { Patient } from "../api/patient/Patient";
import { Doctor } from "../api/doctor/Doctor";
import { useAuth } from "../context/AuthContext";
import { Link, useNavigate } from "react-router-dom";

import patientDefault from "../img/patientDefault.png";
import doctorDefault from "../img/doctorDefault.png";

// Define the API URLs or other variables
const homeUrl = "/";
const dashboardUrl = "/doctor-dashboard";
const myAppointmentsUrl = "/my-appointments";
const doctorRegisterUrl = "/doctor-register";
const patientRegisterUrl = "/patient-register";
const doctorProfileUrl = "/doctor-profile";
const patientProfileUrl = "/patient-profile";
const loginUrl = "/login";

interface HeaderProps {
  user: Doctor | Patient | null;
  hasNotifications: boolean | null;
  isDoctor: boolean | null;
}

const Header: React.FC<HeaderProps> = ({
  user,
  hasNotifications,
  isDoctor,
}) => {
  const { t } = useTranslation();
  const { logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();

    navigate(homeUrl);
  };

  // TODO usar componentes de bootstrap
  return (
    <header className="horizontalPadding border-bottom">
      <div className="head">
        <Link
          to={homeUrl}
          className="d-flex align-items-centera title navbar-brand"
        >
          <Image src={logo} alt="HealthSync logo" className="logo" />
          <div className="health title">Health</div>
          <div className="sync title">Sync</div>
        </Link>
        <div className="buttons">
          <Link to={dashboardUrl} className="headerLink">
            {t("home.checkDoctor")}
            <FaUserDoctor />
          </Link>
          {user && (
            <>
              <Link to={myAppointmentsUrl} className="headerLink">
                {t("home.myAppointments")}
                <FaCalendarCheck />
              </Link>
              {hasNotifications && <FaCircle className="notification" />}
            </>
          )}
        </div>
        {user ? (
          <nav className="navbar navbar-expand-lg">
            <div className="container-fluid">
              {isDoctor ? (
                <>
                  <FaUserNurse className="iconIdentifier" />
                </>
              ) : (
                <>
                  <FaUser className="iconIdentifier" />
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
                        src={
                          user.image
                            ? user.image
                            : isDoctor
                              ? doctorDefault
                              : patientDefault
                        }
                        alt={t("user.alt.loggedUserImg")}
                        width="40"
                        height="40"
                        className="rounded-circle"
                      />
                    </a>
                    <ul className="dropdown-menu">
                      {isDoctor ? (
                        <li>
                          <a
                            className="dropdown-item icon"
                            onClick={() => navigate(doctorProfileUrl)}
                          >
                            {t("header.profile")}
                          </a>
                        </li>
                      ) : (
                        <li>
                          <a className="dropdown-item" onClick={() => navigate(patientProfileUrl)}>
                            {t("header.profile")}
                          </a>
                        </li>
                      )}
                      <li>
                        <a className="dropdown-item" onClick={handleLogout}>
                          {t("header.logout")}
                        </a>
                      </li>
                    </ul>
                  </li>
                </ul>
              </div>
            </div>
          </nav>
        ) : (
          <div className="buttons">
            <Link to={loginUrl} className="btn btn-primary" role="button">
              {t("login.title")}
            </Link>

            <nav className="navbar navbar-expand-lg">
              <div className="container-fluid">
                <div id="navbarNavDropdown">
                  <ul className="navbar-nav">
                    <li className="nav-item dropdown">
                      <button
                        className="btn dropdown-toggle"
                        data-bs-toggle="dropdown"
                        aria-expanded="false"
                      >
                        {t("register.title")}
                      </button>
                      <ul className="dropdown-menu">
                        <li>
                          <Link className="dropdown-item" to={doctorRegisterUrl}>
                            {t("header.iAmDoctor")}
                          </Link>
                        </li>
                        <li>
                          <Link
                            className="dropdown-item"
                            to={patientRegisterUrl}
                          >
                            {t("header.iAmPatient")}
                          </Link>
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
