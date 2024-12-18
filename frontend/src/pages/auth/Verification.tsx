import { useSearchParams, useNavigate } from "react-router-dom";
import { useCallback, useEffect, useRef } from "react";
import { useAuth } from "../../context/AuthContext";

const SUCCESSFUL_ROUTE = "/";
const FAILED_ROUTE = "/resend-token";

const Verification = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const isVerifying = useRef(false);

  const token = searchParams.get("token");
  const email = searchParams.get("email");

  const { verify } = useAuth();

  const verifyUser = useCallback(
    async (email: string, token: string) => {
      isVerifying.current = true;
      try {
        await verify(email, token);
        navigate(SUCCESSFUL_ROUTE);
      } catch (e) {
        navigate(FAILED_ROUTE, { state: { verificationError: true } });
      }
    },
    [verify, navigate],
  );

  useEffect(() => {
    if (!isVerifying.current && token && email) {
      verifyUser(email, token);
    }
  }, [token, email, verifyUser]);

  return null;
};

export default Verification;
