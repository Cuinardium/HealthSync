import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import React from "react";

interface GuardProp {
  requiredRole: string | null;
}

const AuthenticatedGuard: React.FC<GuardProp> = ({ requiredRole }) => {
  const { authenticated, role } = useAuth();
  const location = useLocation();

  return authenticated && (role === requiredRole || !requiredRole) ? (
    <Outlet />
  ) : authenticated ? (
    <Navigate to="/403" state={{ from: location }} replace />
  ) : (
    <Navigate to="/login" state={{ from: location }} replace />
  );
};

export default AuthenticatedGuard;
