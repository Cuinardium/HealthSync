import { BrowserRouter, Route, Routes } from "react-router-dom";
import HomePage from "./pages/home/HomePage";
import { HelmetProvider } from "react-helmet-async";
import { QueryClientProvider } from "@tanstack/react-query";

import { queryClient } from "./api/queryClient";

import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min";

import Error403 from "./pages/errors/403";
import Error404 from "./pages/errors/404";
import Error500 from "./pages/errors/500";
import Login from "./pages/auth/Login";
import PatientRegister from "./pages/auth/PatientRegister";
import DoctorRegister from "./pages/auth/DoctorRegister";
import { AuthProvider } from "./providers/AuthProvider";
import AuthenticatedGuard from "./components/AuthenticatedGuard";
import PatientProfile from "./pages/user/PatientProfile";
import MyAppointments from "./pages/appointments/MyAppointments";
import { UserProvider } from "./providers/UserProvider";
import Layout from "./components/Layout";
import DoctorProfile from "./pages/user/DoctorProfile";

function App() {
  const helmetContext = {};

  return (
    <HelmetProvider context={helmetContext}>
      <QueryClientProvider client={queryClient}>
        <AuthProvider>
          <UserProvider>
            <BrowserRouter>
              <Layout>
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
                  <Route path="/login" element={<Login />} />

                  {/* Private both */}
                  <Route element={<AuthenticatedGuard requiredRole={null} />}>
                    <Route
                      path="my-appointments"
                      element={<MyAppointments />}
                    />
                  </Route>

                  {/* Only Patient */}
                  <Route
                    element={<AuthenticatedGuard requiredRole="ROLE_PATIENT" />}
                  >
                    <Route
                      path="/patient-profile"
                      element={<PatientProfile />}
                    />
                  </Route>

                  {/* Only Doctor */}
                  <Route
                    element={<AuthenticatedGuard requiredRole="ROLE_DOCTOR" />}
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
              </Layout>
            </BrowserRouter>
          </UserProvider>
        </AuthProvider>
      </QueryClientProvider>
    </HelmetProvider>
  );
}

export default App;
