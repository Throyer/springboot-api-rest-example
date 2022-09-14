import { Container } from "@chakra-ui/react";
import { PropsWithChildren, useEffect } from "react";
import { Navbar } from "../navbar";

export const Template = ({ children, title }: PropsWithChildren & { title?: string }) => {

  useEffect(() => {
    document.title = title || 'Spring Boot - CRUD';
  }, [])

  return (
    <Container maxW="container.lg">
      <Navbar />
      {children}
    </ Container>
  )
}