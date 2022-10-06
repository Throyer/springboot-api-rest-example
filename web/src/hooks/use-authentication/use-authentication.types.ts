import { User } from "../../providers/user/user-store.types";

export type SessionsApiResponse = {
  user: User;
  accessToken: string;
  expiresIn: string;
  refreshToken: string;
  tokenType: string;
}

export type Credentials = {
  email: string;
  password: string;
}

export type LoginResult = {
  isAuthenticated: boolean;
}

export type UseAuthentication = {
  login: (credentials: Credentials) => Promise<LoginResult>;
}