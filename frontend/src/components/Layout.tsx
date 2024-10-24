import { ReactNode, useCallback, useEffect, useState } from "react";
import { Helmet } from "react-helmet-async";
import { useTranslation } from "react-i18next";
import { useLocation } from "react-router-dom";
import { useUser } from "../context/UserContext";
import Header from "./Header";
import Loader from "./Loader";

interface LayoutProps {
  children: ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  const location = useLocation();
  const [showHeader, setShowHeader] = useState(true);
  const [title, setTitle] = useState("home.home");
  const { t } = useTranslation();

  const { user, isDoctor, loading } = useUser();

  const shouldShowHeader = useCallback(() => {
    return (
      location.pathname !== "/login" &&
      location.pathname !== "/doctor-register" &&
      location.pathname !== "/patient-register"
    );
  }, [location.pathname]);

  useEffect(() => {
    setShowHeader(shouldShowHeader());
  }, [shouldShowHeader]);

  const getTitle = useCallback(() => {
    if (location.pathname.includes("/detailed-doctor")) {
      return "detailedDoctor.title";
    }

    if (location.pathname.includes("/detailed-appointment")) {
      return "detailedAppointment.title";
    }

    switch (location.pathname) {
      case "/login":
        return "login.login";
      case "/doctor-register":
        return "register.title";
      case "/patient-register":
        return "register.title";
      case "/doctor-profile":
        return "profile.profile";
      case "/patient-profile":
        return "profile.profile";
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
        <Header user={user} isDoctor={isDoctor} hasNotifications={false} />
      )}

      <main>{children}</main>
    </div>
  );
};

export default Layout;
