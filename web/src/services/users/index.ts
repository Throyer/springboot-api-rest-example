import { api } from "../api";

import { Page } from "../models/page";
import { User } from "../models/user";

const RESOURCE_URL = 'users'

export const findAll = () => {
  return api.get<Page<User>>(RESOURCE_URL);
}

export const UsersApi = {
  findAll,
}