import axios from "axios";

const token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZXMiOiJBRE0sVVNFUiIsImV4cCI6MTY2MzE3NjAxOH0.6SrIoiFAzfrZvtvM2cRNjKKcwhjLlkUbrCws0KsXDJY';

export const api = axios.create({
  baseURL: "http://localhost:8080/api",
  headers: {
    'Authorization': `Bearer ${token}`
  }
});