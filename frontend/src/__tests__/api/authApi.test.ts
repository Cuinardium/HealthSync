import { axios } from "../../api/axios";
import { getTokens, renewAccessToken } from "../../api/auth/authApi";
import { AuthenticationError, UserNotVerifiedError } from "../../api/auth/auth";

jest.mock("../../api/axios");

const mockedAxios = axios as jest.Mocked<typeof axios>;

describe("Authentication API", () => {
  afterEach(() => {
    jest.clearAllMocks();
  });

  describe("getTokens", () => {
    it("should return access and refresh tokens on success", async () => {
      mockedAxios.get.mockResolvedValueOnce({
        headers: {
          "x-jwt": "mock-access-token",
          "x-refresh": "mock-refresh-token",
        },
      });

      const result = await getTokens("test@example.com", "password123");

      expect(mockedAxios.get).toHaveBeenCalledWith("/doctors", {
        params: { pageSize: 1 },
        headers: {
          Authorization: "Basic dGVzdEBleGFtcGxlLmNvbTpwYXNzd29yZDEyMw==",
        },
      });
      expect(result).toEqual({
        accessToken: "mock-access-token",
        refreshToken: "mock-refresh-token",
      });
    });

    it("should throw UserNotVerifiedError if the user is disabled", async () => {
      mockedAxios.get.mockRejectedValueOnce({
        response: {
          status: 401,
          data: { message: "User is disabled" },
        },
      });

      await expect(
        getTokens("test@example.com", "password123"),
      ).rejects.toThrow(UserNotVerifiedError);

      expect(mockedAxios.get).toHaveBeenCalled();
    });

    it("should throw AuthenticationError for invalid credentials", async () => {
      mockedAxios.get.mockRejectedValueOnce({
        response: {
          status: 401,
          data: { message: "Invalid credentials" },
        },
      });

      await expect(
        getTokens("test@example.com", "wrongpassword"),
      ).rejects.toThrow(AuthenticationError);

      expect(mockedAxios.get).toHaveBeenCalled();
    });

    it("should rethrow unknown errors", async () => {
      const unexpectedError = new Error("Unexpected error");
      mockedAxios.get.mockRejectedValueOnce(unexpectedError);

      await expect(
        getTokens("test@example.com", "password123"),
      ).rejects.toThrow("Unexpected error");

      expect(mockedAxios.get).toHaveBeenCalled();
    });
  });

  describe("renewAccessToken", () => {
    it("should return a new access token on success", async () => {
      mockedAxios.get.mockResolvedValueOnce({
        headers: {
          "x-jwt": "new-mock-access-token",
        },
      });

      const result = await renewAccessToken("mock-refresh-token");

      expect(mockedAxios.get).toHaveBeenCalledWith("/doctors", {
        params: { pageSize: 1 },
        headers: { Authorization: "Bearer mock-refresh-token" },
      });
      expect(result).toBe("new-mock-access-token");
    });
  });
});
