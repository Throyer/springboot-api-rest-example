export type Roles = 'ADM' | 'USER'

export type User = {
  id: number;
  name: string;
  email: string;
  roles: Roles[];
};

export type UserStore = {
  user: User | null;
  set: (user: User | null) => void;
}