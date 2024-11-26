import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const UnauthenticatedGuard: React.FC = () => {
    const { authenticated } = useAuth();
    const location = useLocation();

    return !authenticated ? (
        <Outlet />
    ) : (
        <Navigate to="/" state={{ from: location }} replace />
    );
};

export default UnauthenticatedGuard;
