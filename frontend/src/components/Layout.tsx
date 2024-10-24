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
  const { t } = useTranslation();

  const {user, isDoctor, loading} = useUser();

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

  if (loading) {
    // TODO: esta bien esto?
    return <Loader/>
  }

  return (
    <div>
      <Helmet>
        <title>{t("home.home")}</title>
      </Helmet>
      {showHeader && <Header user={user} isDoctor={isDoctor} hasNotifications={false} />}

      <main>
        {children}
      </main>
    </div>
  );
};

export default Layout;
