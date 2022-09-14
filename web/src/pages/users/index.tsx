import { Button } from "@chakra-ui/button";
import { useDisclosure } from "@chakra-ui/hooks";
import { Box, Heading, HStack } from "@chakra-ui/layout";
import { Modal, ModalBody, ModalCloseButton, ModalContent, ModalFooter, ModalHeader, ModalOverlay } from "@chakra-ui/modal";
import { Badge, ButtonGroup, Center, Stack } from "@chakra-ui/react";
import { useEffect, useState } from "react";
import { BsFillPenFill, BsTrash2 } from 'react-icons/bs';
import { Template } from "../../components/page";
import { Table } from "../../components/table";
import { User } from "../../services/models/user";
import { UsersApi } from "../../services/users";

export const Users = () => {

  const { isOpen, onOpen, onClose } = useDisclosure()

  const [users, setUsers] = useState<User[]>([]);

  const getUsers = async () => {
    try {
      const { data: page } = await UsersApi.findAll();
      setUsers(page.content);
    } catch (error) {
      console.error('error on find all users');
    }
  }

  useEffect(() => {
    getUsers();
  }, [])

  return (
    <Template title="Users">
      <Box>
        <Heading
          px="1.5"
          my="14"
          as='h1'
          size='2xl'>
          Users
        </Heading>
      </Box>
      <Box>
        <Table
          content={users}
          columns={[
            {
              title: 'Name',
              property: 'name'
            },
            {
              title: 'Email',
              property: "email"
            },
            {
              title: 'Roles',
              render: ({ roles }) => (
                <Stack direction='row'>
                  {roles.map(role => <Badge>{role}</Badge>)}
                </Stack>
              )
            },
            {
              title: 'Active',
              render: ({ active }) => active ?
                <Badge colorScheme='green'>Active</Badge> :
                <Badge colorScheme='red'>Inative</Badge>
            },
            {
              title: 'Actions',
              options: {
                w: '28'
              },
              render: () => (
                <HStack>
                  <Button
                    size="sm"
                    variant="outline">
                    <BsFillPenFill />
                  </Button>
                  <Button
                    size="sm"
                    variant="outline"
                    onClick={onOpen}>
                    <BsTrash2 />
                  </Button>
                </HStack>
              )
            }
          ]}
        />
        <Center mt="12">
          <ButtonGroup size='md' isAttached variant="outline">
            <Button disabled>
              Primeira
            </Button>
            <Button>
              Anterior
            </Button>
            <Button>
              1
            </Button>
            <Button>
              2
            </Button>
            <Button variant="solid">
              3
            </Button>
            <Button>
              4
            </Button>
            <Button>
              5
            </Button>
            <Button>
              Proxima
            </Button>
            <Button>
              Ultima
            </Button>
          </ButtonGroup>
        </Center>
      </Box>

      <Modal isOpen={isOpen} onClose={onClose}>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>Modal Title</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            bla bla bla
          </ModalBody>

          <ModalFooter>
            <Button colorScheme='blue' mr={3} onClick={onClose}>
              Close
            </Button>
            <Button variant='ghost'>Secondary Action</Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </Template>
  )
}