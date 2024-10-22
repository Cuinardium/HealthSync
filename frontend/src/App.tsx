import { BrowserRouter, Route, Routes } from "react-router-dom";
import HomePage from "./home/HomePage";
import { HelmetProvider } from "react-helmet-async";
import { QueryClientProvider } from "@tanstack/react-query";

import { queryClient } from "./api/queryClient";

import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min";

import Error403 from "./errors/403";
import Error404 from "./errors/404";
import Error500 from "./errors/500";
import Login from "./auth/Login";
import PatientRegister from "./auth/PatientRegister";
import DoctorRegister from "./auth/DoctorRegister";
import { AuthProvider } from "./providers/AuthProvider";
import AuthenticatedGuard from "./components/AuthenticatedGuard";
import PatientProfile from "./user/PatientProfile";

function App() {
  const helmetContext = {};

  return (
    <HelmetProvider context={helmetContext}>
      <AuthProvider>
        <QueryClientProvider client={queryClient}>
          <BrowserRouter>
            <Routes>
              <Route path="/" element={<HomePage />} />

              {/* Public */}
              <Route
                path="/doctor-register"
                element={<DoctorRegister hasError={false} error={false} />}
              />
              <Route
                path="/patient-register"
                element={<PatientRegister hasError={false} error={false} />}
              />
              <Route
                path="/login"
                element={<Login/>}
              />

              {/* Only Patient */}
              <Route element={<AuthenticatedGuard requiredRole="ROLE_PATIENT"/>}>
                <Route path="/patient-profile" element={
                  <PatientProfile/>
                }/>
              </Route>

              {/* Only Doctor */}
              <Route element={<AuthenticatedGuard requiredRole="ROLE_DOCTOR"/>}>
                <Route path="/doctor-profile" element={
                  <h1>doctor-profile (WIP)</h1>
                }/>
              </Route>

              {/* Error Pages */}
              <Route path="*" element={<Error404 />} />
              <Route path="/404" element={<Error404 />} />
              <Route path="/500" element={<Error500 />} />
              <Route path="/403" element={<Error403 />} />
            </Routes>
          </BrowserRouter>
        </QueryClientProvider>
      </AuthProvider>
    </HelmetProvider>
  );
}

export default App;
