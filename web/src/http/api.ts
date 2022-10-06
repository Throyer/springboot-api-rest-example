import axios from "axios";

import { useSession } from '../providers/session';

const ENV = {
  BASE_URL: import.meta.env.VITE_API_BASE_URL
}

export const api = axios.create({
  baseURL: ENV.BASE_URL
});

api.interceptors.request.use(async (configs) => {
  const { session, set: setSession } = useSession.getState()

  if (session) {
    if (session.isExpired()) {
      try {        
        const { data: sessionResponse } = await axios.post(`${ENV.BASE_URL}/v1/authentication/refresh`, {
          refreshToken: session.refreshToken
        });
        setSession(sessionResponse);
      } catch (error) {
        setSession(null);
      }
    }

    configs.headers = {
      ...configs.headers,
      'Authorization': `Bearer ${session.accessToken}`,
    }
  }

  return configs;
})