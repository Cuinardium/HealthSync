import { useSearchParams, useNavigate } from "react-router-dom";
import { useCallback, useEffect, useState } from "react";
import { useAuth } from "../../context/AuthContext";

const SUCCESSFUL_ROUTE = "/";
const FAILED_ROUTE = "/resend-token";

const Verification = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  const token = searchParams.get("token");
  const email = searchParams.get("email");

  const { verify } = useAuth();

  const verifyUser = useCallback(
    async (email: string, token: string) => {
      try {
        await verify(email, token);
        navigate(SUCCESSFUL_ROUTE);
      } catch (e) {
        console.error(e);
        navigate(FAILED_ROUTE);
      }
    },
    [verify, navigate],
  );

  useEffect(() => {
    if (token && email) {
      verifyUser(email, token);
    }
  }, [token, email, verifyUser]);

  return null;
};

export default Verification;

