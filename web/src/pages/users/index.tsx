import { Button } from "@chakra-ui/button";
import { useDisclosure } from "@chakra-ui/hooks";
import { Box, Heading, HStack } from "@chakra-ui/layout";
import { Modal, ModalBody, ModalCloseButton, ModalContent, ModalFooter, ModalHeader, ModalOverlay } from "@chakra-ui/modal";
import { Badge, Center, Stack } from "@chakra-ui/react";
import { useEffect, useState } from "react";
import { BsFillPenFill, BsTrash2 } from 'react-icons/bs';
import { Template } from "../../components/page";
import { Pagination, PaginationProps } from "../../components/pagination";
import { Table } from "../../components/table";
import { User } from "../../services/models/user";
import { UsersApi } from "../../services/users";

export const Users = () => {

  const { isOpen, onOpen, onClose } = useDisclosure()

  const [{ page, size, totalPages }, setPagination] = useState<PaginationProps>({
    page: 0,
    size: 10,
    totalPages: 0
  });

  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(false);

  const fetchUsers = async (page?: number, size?: number) => {
    setLoading(true);
    try {
      const { data: { content, ...pagination } } = await UsersApi.findAll(page, size);
      setUsers(content);
      setPagination(pagination);
    } catch (error) {
      console.error('error on find all users');
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    fetchUsers();
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
          loading={loading}
          size="sm"
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
                  {roles.map(role => <Badge key={role}>{role}</Badge>)}
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
              disableSkeleton: true,
              render: () => (
                <HStack>
                  <Button
                    disabled={loading}
                    size="sm"
                    variant="outline">
                    <BsFillPenFill />
                  </Button>
                  <Button
                    disabled={loading}
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
        <Center my="12">
          <Pagination
            page={page}
            size={size}
            totalPages={totalPages}
            onChange={(page, size) => fetchUsers(page, size)}
          />
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