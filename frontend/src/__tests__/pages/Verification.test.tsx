import { render, waitFor } from "@testing-library/react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import Verification from "../../pages/auth/Verification";

jest.mock("react-router-dom", () => ({
  useNavigate: jest.fn(),
  useSearchParams: jest.fn(),
}));

jest.mock("../../context/AuthContext", () => ({
  useAuth: jest.fn(),
}));

describe("Verification Component", () => {
  const mockNavigate = jest.fn();
  const mockVerify = jest.fn();
  const mockSearchParams = jest.fn();

  beforeEach(() => {
    jest.clearAllMocks();
    (useNavigate as jest.Mock).mockReturnValue(mockNavigate);
    (useSearchParams as jest.Mock).mockReturnValue([
      {
        get: mockSearchParams,
      },
    ]);
    (useAuth as jest.Mock).mockReturnValue({ verify: mockVerify });
  });

  it("redirects to the successful route when verification succeeds", async () => {
    mockSearchParams.mockImplementation((key: string) => {
      if (key === "token") return "valid-token";
      if (key === "email") return "test@example.com";
      return null;
    });
    mockVerify.mockResolvedValueOnce(undefined);

    render(<Verification />);

    expect(mockVerify).toHaveBeenCalledWith("test@example.com", "valid-token");

    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith("/");
    });
  });

  it("redirects to the failure route when verification fails", async () => {
    mockSearchParams.mockImplementation((key: string) => {
      if (key === "token") return "invalid-token";
      if (key === "email") return "test@example.com";
      return null;
    });
    mockVerify.mockRejectedValueOnce(new Error("Verification failed"));

    render(<Verification />);

    expect(mockVerify).toHaveBeenCalledWith(
      "test@example.com",
      "invalid-token",
    );

    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith("/resend-token", {
        state: { verificationError: true },
      });
    });
  });

  it("does nothing when token or email is missing", () => {
    mockSearchParams.mockImplementation(() => null);

    render(<Verification />);

    expect(mockVerify).not.toHaveBeenCalled();
    expect(mockNavigate).not.toHaveBeenCalled();
  });
});
