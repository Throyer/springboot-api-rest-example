import { api } from "../api";

import { Page } from "../models/page";
import { User } from "../models/user";

const RESOURCE_URL = 'users'

export const findAll = (page?: number, size?: number) => {

  const params = new URLSearchParams();

  if (page !== undefined) {
    params.append("page", page.toString());
  }
  
  if (size !== undefined) {
    params.append("size", size.toString());
  }

  return api.get<Page<User>>(RESOURCE_URL, { params });
}

export const UsersApi = {
  findAll,
}