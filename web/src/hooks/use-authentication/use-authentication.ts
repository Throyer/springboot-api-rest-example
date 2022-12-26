import { useCallback } from "react";
import { Navigate, useNavigate } from "react-router-dom";
import { api } from "../../http/api";
import { useSession } from "../../providers/session";
import { useUser } from "../../providers/user";
import { Credentials, LoginResult, SessionsApiResponse } from "./use-authentication.types";

export const useAuthentication = () => {
  const { set: setSession } = useSession();
  const { set: setUser } = useUser();

  const navigate = useNavigate()

  const login = useCallback(async ({ email, password }: Credentials, redirectTo = '/home'): Promise<LoginResult> => {
    try {
      const { data } = await api.post<SessionsApiResponse>('authentication', { email, password });
      const { user, ...session } = data;

      setSession(session);
      setUser(user);
      navigate(redirectTo);

      return {
        isAuthenticated: true
      }
    } catch (error: any) {

      console.log(error);
      error.response && console.log(error.response);
      
      return {
        isAuthenticated: false
      }
    }
  }, [])

  const logout = useCallback(() => {
    setSession(null);
    setUser(null);
  }, [])

  return {
    login,
    logout
  }
}