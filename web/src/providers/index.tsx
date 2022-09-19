import { ChakraProvider } from "@chakra-ui/react";
import { PropsWithChildren } from "react";
import { DialogProvider } from "./alert";

export const Providers = ({ children }: PropsWithChildren) => (
  <ChakraProvider>
    <DialogProvider>
      {children}
    </DialogProvider>
  </ChakraProvider>
)