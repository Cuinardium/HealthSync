import { BrowserRouter, Route, Routes } from "react-router-dom";
import React, { Suspense, lazy } from "react";
import { HelmetProvider } from "react-helmet-async";
import { QueryClientProvider } from "@tanstack/react-query";

import { queryClient } from "./api/queryClient";

import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min";

import { AuthProvider } from "./providers/AuthProvider";
import AuthenticatedGuard from "./components/AuthenticatedGuard";

import { UserProvider } from "./providers/UserProvider";
import Layout from "./components/Layout";
import Loader from "./components/Loader";

const HomePage = lazy(() => import("./pages/home/HomePage"));
const Error403 = lazy(() => import("./pages/errors/403"));
const Error404 = lazy(() => import("./pages/errors/404"));
const Error500 = lazy(() => import("./pages/errors/500"));
const Login = lazy(() => import("./pages/auth/Login"));
const PatientRegister = lazy(() => import("./pages/auth/PatientRegister"));
const DoctorRegister = lazy(() => import("./pages/auth/DoctorRegister"));
const PatientProfile = lazy(() => import("./pages/user/PatientProfile"));
const MyAppointments = lazy(
  () => import("./pages/appointments/MyAppointments"),
);
const DoctorProfile = lazy(() => import("./pages/user/DoctorProfile"));
const DoctorDashboard = lazy(() => import("./pages/doctor/DoctorDashboard"));
const DoctorDetails = lazy(() => import("./pages/doctor/DoctorDetails"));
const DetailedAppointment = lazy(
  () => import("./pages/appointments/AppointmentDetails"),
);

function App() {
  const helmetContext = {};

  return (
    <HelmetProvider context={helmetContext}>
      <QueryClientProvider client={queryClient}>
        <AuthProvider>
          <UserProvider>
            <BrowserRouter basename="paw-2023a-02">
              <Layout>
                <Suspense fallback={<Loader />}>
                  <Routes>
                    <Route path="/" element={<HomePage />} />

                    {/* Public */}
                    <Route
                      path="/doctor-register"
                      element={
                        <DoctorRegister hasError={false} error={false} />
                      }
                    />
                    <Route
                      path="/patient-register"
                      element={
                        <PatientRegister hasError={false} error={false} />
                      }
                    />
                    <Route path="/login" element={<Login />} />

                    <Route
                      path="/doctor-dashboard"
                      element={<DoctorDashboard />}
                    />
                    <Route
                      path="/detailed-doctor/:id"
                      element={<DoctorDetails />}
                    />

                    {/* Private both */}
                    <Route element={<AuthenticatedGuard requiredRole={null} />}>
                      <Route
                        path="my-appointments"
                        element={<MyAppointments />}
                      />
                      <Route
                        path="/detailed-appointment/:id"
                        element={<DetailedAppointment />}
                      />
                    </Route>

                    {/* Only Patient */}
                    <Route
                      element={
                        <AuthenticatedGuard requiredRole="ROLE_PATIENT" />
                      }
                    >
                      <Route
                        path="/patient-profile"
                        element={<PatientProfile />}
                      />
                    </Route>

                    {/* Only Doctor */}
                    <Route
                      element={
                        <AuthenticatedGuard requiredRole="ROLE_DOCTOR" />
                      }
                    >
                      <Route
                        path="/doctor-profile"
                        element={<DoctorProfile />}
                      />
                    </Route>

                    {/* Error Pages */}
                    <Route path="*" element={<Error404 />} />
                    <Route path="/404" element={<Error404 />} />
                    <Route path="/500" element={<Error500 />} />
                    <Route path="/403" element={<Error403 />} />
                  </Routes>
                </Suspense>
              </Layout>
            </BrowserRouter>
          </UserProvider>
        </AuthProvider>
      </QueryClientProvider>
    </HelmetProvider>
  );
}

export default App;
