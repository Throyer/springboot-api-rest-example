import { Box, Heading } from "@chakra-ui/layout";
import { useEffect, useState } from "react";

import { User } from "../../services/models/user";
import { UsersApi } from "../../services/users";

import { Template } from "../../components/page";
import { PaginationProps } from "../../components/pagination";
import { Table } from "../../components/table";
import { actions } from "../../components/table/actions";
import { Column } from "../../components/table/types";

import { useDialog } from "../../hooks/use-dialog";
import { Roles } from "./components/roles";
import { Status } from "./components/status";

export const Users = () => {

  const { show } = useDialog();

  const [{ page, size, totalPages }, setPagination] = useState<PaginationProps>({
    page: 0,
    size: 10,
    totalPages: 10
  });

  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(false);

  const columns: Column<User>[] = [
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
      render: ({ roles }) => <Roles roles={roles} />
    },
    {
      title: 'Active',
      render: ({ active }) => <Status active={active} />
    },
  ]

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
          px="2"
          my="6"
          as='h1'
          size='md'>
          Users
        </Heading>
      </Box>
      <Table
        size="sm"
        loading={loading}
        content={users}
        pagination={{
          loading: users.length === 0,
          page: page,
          size: size,
          totalPages: totalPages,
          onChange: (page, size) => fetchUsers(page, size),
        }}
        columns={[
          ...columns,
          actions<User>({
            edit: { options: { disabled: loading, } },
            remove: {
              options: { disabled: loading },
              onClick: (row) => show({
                title: 'Delete user',
                content: 'Are you sure? You can\'t undo this action afterwards.',
                accept: {
                  title: 'Delete',
                  onClick: () => console.log('remove: ', row?.name)
                },
                decline: { title: 'Cancel' }
              })
            },
          })
        ]}
      />
    </Template>
  )
}