import { Box, Heading } from "@chakra-ui/layout";
import { Template } from "../../components/page";

export const Home = () => {
  return (
    <Template title="Home">
      <Box>
        <Heading px="1.5" my="14" as='h1' size='2xl'>
          Home
        </Heading>
      </Box>
    </Template>
  )
}