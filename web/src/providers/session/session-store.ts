import { DateTime } from "luxon";
import create from "zustand";

import { SessionStore } from "./session-store.types";

export const useSession = create<SessionStore>((set) => ({
  session: null,
  set: (session) => {

    if (!session) {
      return set((old) => ({ ...old, session }));
    }

    const expiresAt = new Date(session.expiresIn);
    set((old) => ({
      ...old,
      session: {
        ...session,
        expiresAt,
        isExpired: () => DateTime.fromJSDate(expiresAt) > DateTime.now(),
      }
    }))
  }
}));