import { Button, Divider, Stack, Text } from "@chakra-ui/react";
import { Link } from "react-router-dom";

export const Navbar = () => (
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
    </Stack>
    <Divider />
  </>
)