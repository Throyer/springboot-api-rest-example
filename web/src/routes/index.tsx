import { Routes as ReactDomRoutes, Route } from "react-router-dom";
import { Home } from "../pages/home";
import { Users } from "../pages/users";

export const Routes = () => (
  <ReactDomRoutes>
    <Route path="/" element={<Home />} />
    <Route path="/users" element={<Users />} />
  </ReactDomRoutes>
)