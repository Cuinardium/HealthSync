import { ReactNode, useCallback, useEffect, useState } from "react";
import { Helmet } from "react-helmet-async";
import { useTranslation } from "react-i18next";
import { useLocation } from "react-router-dom";
import { useUser } from "../context/UserContext";
import { useNotifications } from "../hooks/notificationHooks";
import Header from "./Header";
import Loader from "./Loader";

interface LayoutProps {
  children: ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  const location = useLocation();
  const [showHeader, setShowHeader] = useState(true);
  const [showCheckDoctor, setShowCheckDoctor] = useState(true);
  const [showAuth, setShowAuth] = useState(true);
  const [title, setTitle] = useState("home.home");
  const { t } = useTranslation();

  const { user, isDoctor, loading } = useUser();
  const id = user ? String(user.id) : "";
  const { data: notifications, isLoading } = useNotifications(id);

  const shouldShowHeader = useCallback(() => {
    return location.pathname !== "/verify";
  }, [location.pathname]);

  const shouldShowCheckDoctor = useCallback(() => {
    return (
      location.pathname !== "/login" &&
      location.pathname !== "/patient-register" &&
      location.pathname !== "/doctor-register" &&
      location.pathname !== "/resend-token" &&
      location.pathname !== "/register-successful" &&
      location.pathname !== "/verify"
    );
  }, [location.pathname]);

  const shouldShowAuth = useCallback(() => {
    return (
      location.pathname !== "/login" &&
      location.pathname !== "/patient-register" &&
      location.pathname !== "/doctor-register" &&
      location.pathname !== "/register-successful" &&
      location.pathname !== "/verify"
    );
  }, [location.pathname]);

  useEffect(() => {
    setShowHeader(shouldShowHeader());
  }, [shouldShowHeader]);

  useEffect(() => {
    setShowCheckDoctor(shouldShowCheckDoctor());
  }, [shouldShowCheckDoctor]);

  useEffect(() => {
    setShowAuth(shouldShowAuth());
  }, [shouldShowAuth]);

  const getTitle = useCallback(() => {
    if (location.pathname.includes("/detailed-doctor")) {
      return "detailedDoctor.title";
    }

    if (location.pathname.includes("/detailed-appointment")) {
      return "detailedAppointment.title";
    }

    switch (location.pathname) {
      case "/login":
        return "login.title";
      case "/resend-token":
        return "resend.header";
      case "/register-successful":
        return "register_success.header";
      case "/doctor-register":
        return "registerMedic.title";
      case "/patient-register":
        return "registerPatient.title";
      case "/doctor-profile":
        return "profile.profile";
      case "/patient-profile":
        return "profile.profile";
      case "/change-password":
        return "changePassword.title";
      case "/doctor-dashboard":
        return "doctorDashboard.title";
      case "/my-appointments":
        return "home.myAppointments";
      case "/403":
        return "error.403";
      case "/404":
        return "error.404";
      case "/500":
        return "error.500";
      case "/":
        return "home.home";
      default:
        return "home.home";
    }
  }, [location.pathname]);

  useEffect(() => {
    setTitle(getTitle());
  }, [getTitle]);

  if (loading) {
    // TODO: esta bien esto?
    return (
      <div>
        <Helmet>
          <title>{t(title)}</title>
        </Helmet>
        <Loader />
      </div>
    );
  }

  return (
    <div>
      <Helmet>
        <title>{t(title)}</title>
      </Helmet>
      {showHeader && (
        <Header
          user={user}
          isDoctor={isDoctor}
          hasNotifications={
            !isLoading && !!notifications && notifications.length > 0
          }
          showCheckDoctor={showCheckDoctor}
          showAuth={showAuth}
        />
      )}

      <main>{children}</main>
    </div>
  );
};

export default Layout;
