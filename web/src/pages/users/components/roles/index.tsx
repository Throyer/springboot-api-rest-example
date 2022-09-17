import { Badge, Stack } from "@chakra-ui/react";

interface RolesProps {
  roles: string[];
}

export const Roles = ({ roles }: RolesProps) => (
  <Stack direction='row'>
    {roles.map(role => <Badge key={role}>{role}</Badge>)}
  </Stack>
)