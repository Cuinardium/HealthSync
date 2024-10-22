import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import Loader from "./Loader";

interface GuardProp {
  requiredRole: string | null;
}

const AuthenticatedGuard: React.FC<GuardProp> = ({ requiredRole }) => {
  const { authenticated, role, loading } = useAuth();
  const location = useLocation();

  if (loading) {
    return <Loader/>
  }

  return authenticated && (role === requiredRole || !requiredRole) ? (
    <Outlet />
  ) : authenticated ? (
    <Navigate to="/403" state={{ from: location }} replace />
  ) : (
    <Navigate to="/login" state={{ from: location }} replace />
  );
};

export default AuthenticatedGuard;
