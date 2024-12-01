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
import UnauthenticatedGuard from "./components/UnauthenticatedGuard";
import { SelectedTabProvider } from "./providers/SelectedTabProvider";
import { DoctorQueryProvider } from "./providers/DoctorQueryProvider";

const HomePage = lazy(() => import("./pages/home/HomePage"));
const Error403 = lazy(() => import("./pages/errors/403"));
const Error404 = lazy(() => import("./pages/errors/404"));
const Error500 = lazy(() => import("./pages/errors/500"));
const Login = lazy(() => import("./pages/auth/Login"));
const PatientRegister = lazy(() => import("./pages/auth/PatientRegister"));
const DoctorRegister = lazy(() => import("./pages/auth/DoctorRegister"));
const Verification = lazy(() => import("./pages/auth/Verification"));
const ResendToken = lazy(() => import("./pages/auth/ResendToken"));
const RegisterSuccessful = lazy(
  () => import("./pages/auth/RegisterSuccessful"),
);
const DoctorEdit = lazy(() => import("./pages/user/DoctorEdit"));
const ChangeSchedule = lazy(() => import("./pages/user/ChangeSchedule"));
const PatientProfile = lazy(() => import("./pages/user/PatientProfile"));
const PatientEdit = lazy(() => import("./pages/user/PatientEdit"));
const ChangePassword = lazy(() => import("./pages/user/ChangePassword"));
const MyAppointments = lazy(
  () => import("./pages/appointments/MyAppointments"),
);
const DoctorProfile = lazy(() => import("./pages/user/DoctorProfile"));
const DoctorVacations = lazy(() => import("./pages/vacations/DoctorVacations"));
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
            <SelectedTabProvider>
              <DoctorQueryProvider>
                <BrowserRouter basename="/paw-2023a-02">
                  <Layout>
                    <Suspense fallback={<Loader />}>
                      <Routes>
                        <Route path="/" element={<HomePage />} />

                        {/* Public */}
                        <Route
                          path="/doctor-dashboard"
                          element={<DoctorDashboard />}
                        />
                        <Route
                          path="/detailed-doctor/:id"
                          element={<DoctorDetails />}
                        />

                        {/* Only unauthenticated */}
                        <Route element={<UnauthenticatedGuard />}>
                          <Route
                            path="/doctor-register"
                            element={<DoctorRegister />}
                          />
                          <Route
                            path="/patient-register"
                            element={<PatientRegister />}
                          />
                          <Route path="/login" element={<Login />} />

                          <Route
                            path="/register-successful"
                            element={<RegisterSuccessful />}
                          />

                          <Route path="/verify" element={<Verification />} />
                          <Route
                            path="/resend-token"
                            element={<ResendToken />}
                          />
                        </Route>

                        {/* Private both */}
                        <Route
                          element={<AuthenticatedGuard requiredRole={null} />}
                        >
                          <Route
                            path="/my-appointments"
                            element={<MyAppointments />}
                          />
                          <Route
                            path="/detailed-appointment/:id"
                            element={<DetailedAppointment />}
                          />
                          <Route
                            path="/change-password"
                            element={<ChangePassword />}
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
                          <Route
                            path="/patient-edit"
                            element={<PatientEdit />}
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
                          <Route
                            path="/doctor-vacations"
                            element={<DoctorVacations />}
                          />
                          <Route path="/doctor-edit" element={<DoctorEdit />} />
                          <Route
                            path="/change-schedule"
                            element={<ChangeSchedule />}
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
              </DoctorQueryProvider>
            </SelectedTabProvider>
          </UserProvider>
        </AuthProvider>
      </QueryClientProvider>
    </HelmetProvider>
  );
}

export default App;
