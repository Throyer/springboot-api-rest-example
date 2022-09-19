import create from "zustand";

import { UserStore } from "./user-store.types";

export const useUser = create<UserStore>((set) => ({
  user: null,
  set: (user) => set((old) => ({ ...old, user }))
}));