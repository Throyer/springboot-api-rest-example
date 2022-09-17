import { createRoot } from 'react-dom/client';

import { ChakraProvider } from '@chakra-ui/react';

import { App } from './App';
import { BrowserRouter } from 'react-router-dom';
import { Providers } from './hooks';

const element = document.getElementById('root') as Element;
const root = createRoot(element);

root.render(
  <BrowserRouter>
    <Providers>
      <App />
    </Providers>
  </BrowserRouter>
);
