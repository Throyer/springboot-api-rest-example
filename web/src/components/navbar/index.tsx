import { Button, Divider, Flex, Menu, MenuButton, MenuItem, MenuList, Stack, Text, Link as ChakraLink, Avatar } from "@chakra-ui/react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useAuthentication } from "../../hooks/use-authentication/use-authentication";
import { BiChevronDown } from 'react-icons/bi'
import { useUser } from "../../providers/user";

export const Navbar = () => {
  const { user } = useUser()
  const { logout } = useAuthentication();
  const navigate = useNavigate();
  const { pathname } = useLocation();

  return (
    <>
      <Flex
        mt="10"
        py="2"
        gap="2"
        justifyContent="space-between"
        direction="row"
      >
        <Flex
          gap="2"
          justifyContent="space-between"
          direction="row"
        >
          <Link to="/home">
            <Text px="2" fontWeight={pathname === '/home' ? 'bold' : 'normal'} display="block">
              Home
            </Text>
          </Link>
          <Link to="/users">
            <Text px="2" fontWeight={pathname === '/users' ? 'bold' : 'normal'} display="block">
              Users
            </Text>
          </Link>
        </Flex>
        {user && (
          <Menu>
            <MenuButton as={ChakraLink}>
              <Avatar size="sm" name={user.name} src='https://bit.ly/broken-link' />
            </MenuButton>
            <MenuList>
              <MenuItem onClick={() => {
                logout();
                navigate('/');
              }}>
                Logout
              </MenuItem>
            </MenuList>
          </Menu>
        )}
      </Flex>
      <Divider />
    </>
  )
}