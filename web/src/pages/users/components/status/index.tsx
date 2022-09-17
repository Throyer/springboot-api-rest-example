import { Badge } from "@chakra-ui/react";

interface StatusProps {
  active: boolean;
}

export const Status = ({ active }: StatusProps) => active ?
  <Badge colorScheme='green'>Active</Badge> :
  <Badge colorScheme='red'>Inative</Badge>