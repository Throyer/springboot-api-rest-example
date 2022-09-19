import { Box, Heading } from "@chakra-ui/layout";
import { Button, Center, Code, FormControl, FormLabel, Input, VStack } from "@chakra-ui/react";
import { FormEvent, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuthentication } from "../../hooks/use-authentication/use-authentication";
import { useUser } from "../../providers/user";

export const Login = () => {

  const { login } = useAuthentication();
  const { user } = useUser();
  const navigate = useNavigate()

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    const { isAuthenticated } = await login({ email, password });

    if (!isAuthenticated) {
      console.error('não vai da não!');
    }
  }

  return (
    <Center>
      <form onSubmit={handleSubmit}>
        <VStack>
          <Box>
            <Heading px="1.5" my="14" as='h1' size='2xl'>
              Login
            </Heading>
          </Box>
          <Box>
            <FormControl>
              <FormLabel>Email address</FormLabel>
              <Input value={email} onChange={({ target: { value } }) => setEmail(value)} type='email' />
            </FormControl>

            <FormControl>
              <FormLabel>Password</FormLabel>
              <Input value={password} onChange={({ target: { value } }) => setPassword(value)} type='password' />
            </FormControl>
          </Box>

          <Button type="submit">Submit</Button>

          <Code>
            <pre>
              {JSON.stringify({ email, password }, null, 4)}
            </pre>
          </Code>
          <Code>
            <pre>
              {JSON.stringify(user, null, 4)}
            </pre>
          </Code>
        </VStack>
      </form>
    </Center>
  )
}