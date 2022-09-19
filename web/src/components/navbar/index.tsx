import { Button, Divider, Stack, Text } from "@chakra-ui/react";
import { Link, useNavigate } from "react-router-dom";
import { useAuthentication } from "../../hooks/use-authentication/use-authentication";

export const Navbar = () => {

  const { logout } = useAuthentication()
  const navigate = useNavigate()

  return (
    <>
      <Stack mt="10" direction='row'>
        <Link to="/">
          <Button
            p="3"
            variant="link">
            Home
          </Button>
        </Link>
        <Link to="/users">
          <Button
            p="3"
            variant="link">
            Users
          </Button>
        </Link>
        <Button
          onClick={() => {
            logout();
            navigate('/');
          }}
          p="3"
          variant="link">
          Logout
        </Button>
      </Stack>
      <Divider />
    </>
  )
}