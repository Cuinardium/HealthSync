// Loader component
// Used for short times

import React from "react";
import {Image, Spinner} from "react-bootstrap";

import logo from "../img/logo.svg";

const Loader: React.FC = () => {
  return (
    <div className="d-flex flex-row align-items-center justify-content-center min-vh-100">
        <div
            className="d-flex align-items-centera title navbar-brand me-5"
        >
            <Image src={logo} alt="HealthSync logo" className="logo" />
            <div className="health title">Health</div>
            <div className="sync title">Sync</div>
        </div>
      <Spinner className="ms-3 health" animation="border" role="status" />
    </div>
  );
};

export default Loader;
