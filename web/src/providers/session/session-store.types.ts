type Session = {
  accessToken: string;
  expiresAt: Date;
  refreshToken: string;
  tokenType: string;
  isExpired: () => boolean;
};

type setSession = Omit<Session, 'expiresAt' | 'isExpired'> & {
  expiresIn: string;
}

export type SessionStore = {
  session: Session | null;
  set: (session: setSession | null) => void;
}