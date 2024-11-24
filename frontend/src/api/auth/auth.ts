export interface Credentials {
  accessToken: string;
  refreshToken: string;
}

export class UserNotVerifiedError extends Error {
  constructor() {
    super("User account is not verified.");
    this.name = "UserNotVerifiedError";
  }
}

export class AuthenticationError extends Error {
  constructor() {
    super("Invalid credentials or user does not exist.");
    this.name = "AuthenticationError";
  }
}
