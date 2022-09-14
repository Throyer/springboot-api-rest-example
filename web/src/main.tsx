import { createRoot } from 'react-dom/client';

import { ChakraProvider } from '@chakra-ui/react';

import { App } from './App';
import { BrowserRouter } from 'react-router-dom';

const element = document.getElementById('root') as Element;
const root = createRoot(element);

root.render(
  <BrowserRouter>
    <ChakraProvider>
      <App />
    </ChakraProvider>
  </BrowserRouter>
);
